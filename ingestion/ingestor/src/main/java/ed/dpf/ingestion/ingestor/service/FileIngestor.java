package ed.dpf.ingestion.ingestor.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ed.dpf.ingestion.ingestor.client.CatalogueClient;
import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;
import ed.dpf.ingestion.ingestor.util.Ingestor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileIngestor {
    @Value("${dpf.ingestion.config_folder}")
    private String configFolderPath = "/home/user/dpf/ingestion/config/";

    @Value("${dpf.process.queue}")
    private String processQueueName;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    CatalogueClient catalogueClient;

    private List<Ingestor> ingestorList = new ArrayList<>();

    public FileIngestor() {
        readConfigurationFiles();
    }

    private void readConfigurationFiles() {
        File configFolder = new File(configFolderPath);
        ObjectMapper objectMapper = new ObjectMapper();
        for(File configFile : configFolder.listFiles()) {
            try {
                log.info("Config file \"{}\" is being parsed.", configFile.getAbsolutePath());
                IngestorConfiguration configuration = objectMapper.readValue(configFile, IngestorConfiguration.class);
                ingestorList.add(new Ingestor(configuration));
            } catch (IOException e) {
                log.error("Config file \"{}\" cannot be parsed.", configFile.getAbsolutePath(), e);
            }
        }
    }

    public void ingestFilename(String filename) {
        for(Ingestor ingestor : ingestorList) {
            Map<String, Object> properties = ingestor.parse(filename);
            if (properties != null) {
                properties.put("filename", filename);
                catalogueClient.addProduct(filename+"@"+ingestor.getName(), properties);
                try {
                    template.convertAndSend(processQueueName, properties);
                } catch (AmqpException e) {
                    log.error("#PROCESS_FAILED# \"{}\" #MSG# MQ is not reachable: \"{}\"",filename+"@"+ingestor.getName(), e.getMessage(), e);
                }
            }
        }
    }
}
