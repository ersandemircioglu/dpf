package ed.dpf.ingestion.ingestor.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ed.dpf.ingestion.ingestor.client.ArchiveClient;
import ed.dpf.ingestion.ingestor.client.CatalogueClient;
import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;
import ed.dpf.ingestion.ingestor.model.ParserType;
import ed.dpf.ingestion.ingestor.util.Ingestor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileIngestor {
    @Value("${dpf.ingestion.config-folder}")
    private String configFolderPath;

    @Value("${dpf.process.queue}")
    private String processQueueName;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    CatalogueClient catalogueClient;

    @Autowired
    ArchiveClient archiveClient;

    private List<Ingestor> ingestorList = new ArrayList<>();

    public FileIngestor() {

    }

    @PostConstruct
    public void init() {
        readConfigurationFiles();
    }

    private void readConfigurationFiles() {
        File configFolder = new File(configFolderPath);
        ObjectMapper objectMapper = new ObjectMapper();
        for (File configFile : configFolder.listFiles()) {
            try {
                log.info("Config file \"{}\" is being parsed.", configFile.getAbsolutePath());
                IngestorConfiguration configuration = objectMapper.readValue(configFile, IngestorConfiguration.class);
                ingestorList.add(new Ingestor(configuration));
            } catch (IOException e) {
                log.error("Config file \"{}\" cannot be parsed.", configFile.getAbsolutePath(), e);
            }
        }
    }

    public void processFile(String incomingFileAbsolutePath) {
        File incomingFile = new File(incomingFileAbsolutePath);
        String archivePath = archiveClient.archive(incomingFileAbsolutePath);
        String filename = incomingFile.getName();

        for (Ingestor ingestor : ingestorList) {
            if (ingestor.getParserType() == ParserType.FILENAME) {
                ingestFilename(ingestor, filename, archivePath);
            }
        }
    }

    private void ingestFilename(Ingestor ingestor, String filename, String archivePath) {
        Map<String, Object> properties = ingestor.parse(filename);
        if (properties != null) {
            properties.put("filename", filename);
            properties.put("archive_path", archivePath);
            properties.put("type", ingestor.getName());
            catalogueClient.addProduct(filename + "@" + ingestor.getName(), properties);
            try {
                template.convertAndSend(processQueueName, properties);
            } catch (AmqpException e) {
                log.error("#PROCESS_FAILED# \"{}\" #MSG# \"MQ is not reachable: {}\"",
                        filename + "@" + ingestor.getName(), e.getMessage(), e);
            }
        }
    }
}
