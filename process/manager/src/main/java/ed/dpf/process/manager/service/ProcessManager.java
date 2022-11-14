package ed.dpf.process.manager.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ed.dpf.process.manager.client.CatalogueClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProcessManager {
    @Value("${dpf.process.config_folder}")
    private String configFolderPath = "/home/user/dpf/process/config/";

    @Autowired
    CatalogueClient catalogueClient;


    public ProcessManager() {
    }


    public void process(Map<String, Object> message) {

    }
}
