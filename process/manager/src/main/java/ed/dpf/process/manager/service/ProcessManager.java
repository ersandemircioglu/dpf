package ed.dpf.process.manager.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ed.dpf.process.manager.client.ArchiveClient;
import ed.dpf.process.manager.client.CatalogueClient;
import ed.dpf.process.manager.model.ProcessorConfiguration;
import ed.dpf.process.manager.service.util.Processor;
import ed.dpf.process.manager.service.util.ProcessorFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessManager {
    @Value("${dpf.process.config-folder}")
    private String configFolderPath;

    @Value("${dpf.process.shared-folder}")
    private String sharedFolderPath;

    @Autowired
    CatalogueClient catalogueClient;

    @Autowired
    ArchiveClient archiveClient;

    private List<Processor> processorList = new ArrayList<>();

    public ProcessManager() {
    }

    @PostConstruct
    public void init() {
        readConfigurationFiles();
    }

    private void readConfigurationFiles() {
        File configFolder = new File(configFolderPath);
        ObjectMapper objectMapper = new ObjectMapper();
        ProcessorFactory processorFactory = new ProcessorFactory();
        for (File configFile : configFolder.listFiles()) {
            try {
                log.info("Config file \"{}\" is being parsed.", configFile.getAbsolutePath());
                ProcessorConfiguration configuration = objectMapper.readValue(configFile, ProcessorConfiguration.class);
                processorList.add(processorFactory.getProcessor(configuration));
            } catch (Exception e) {
                log.error("Config file \"{}\" cannot be parsed.", configFile.getAbsolutePath(), e);
            }
        }
    }

    public void process(Map<String, Object> record) {
        processorList.stream().filter(p -> p.matches(record)).forEach(p -> p.process(record));
    }
}
