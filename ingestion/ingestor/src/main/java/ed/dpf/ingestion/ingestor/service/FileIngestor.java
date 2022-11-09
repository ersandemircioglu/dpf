package ed.dpf.ingestion.ingestor.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ed.dpf.ingestion.ingestor.catalogue.CatalogueServiceClient;
import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileIngestor {

    @Autowired
    CatalogueServiceClient catalogueServiceClient;

    @Value("${dpf.ingestion.config_folder}")
    private String configFolderPath;

    private List<IngestorConfiguration> configList = new ArrayList<>();

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
                configList.add(configuration);
            } catch (IOException e) {
                log.error("Config file \"{}\" cannot be parsed.", configFile.getAbsolutePath(), e);
            }
        }
    }

    public void ingestFilename(String filename) {
        for(IngestorConfiguration configuration : configList) {

        }
    }
}
