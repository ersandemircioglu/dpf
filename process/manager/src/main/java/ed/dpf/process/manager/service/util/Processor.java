package ed.dpf.process.manager.service.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ed.dpf.process.manager.client.ArchiveClient;
import ed.dpf.process.manager.client.CatalogueClient;
import ed.dpf.process.manager.client.CatalogueDoc;
import ed.dpf.process.manager.client.CatalogueQueryResult;
import ed.dpf.process.manager.model.InputConfiguration;
import ed.dpf.process.manager.model.ProcessorConfiguration;

public abstract class Processor {
    protected ProcessorConfiguration configuration;

    public Processor(ProcessorConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean matches(Map<String, Object> record) {
        for (Entry<String, Object> filter : configuration.getFilter().entrySet()) {
            if (!record.containsKey(filter.getKey())) {
                return false;
            } else if (!record.get(filter.getKey()).equals(filter.getValue())) {
                return false;
            }
        }
        return true;
    }

    public File retrieveInputs(CatalogueClient catalogueClient, Map<String, Object> record, ArchiveClient archiveClient,
            String sharedFolderPath) {
        File inputFolder = new File(sharedFolderPath, configuration.getName() + "_" + System.currentTimeMillis());
        inputFolder.mkdirs();
        for (InputConfiguration inputConfiguration : configuration.getInputs().values()) {
            Map<String, Object> query = injectQueryArguments(record, inputConfiguration.getQuery());
            CatalogueQueryResult queryResult = catalogueClient.findProduct(query);
            for (CatalogueDoc doc : queryResult.getDocs()) {
                archiveClient.retrieve(doc.getArchive_path(), inputFolder.getAbsolutePath());
            }
        }
        return inputFolder;
    }

    private Map<String, Object> injectQueryArguments(Map<String, Object> record, Map<String, Object> template) {
        Map<String, Object> query = new HashMap<>();
        for (Entry<String, Object> entry : template.entrySet()) {
            if (entry.getValue() instanceof Map) {
                query.put(entry.getKey(), injectQueryArguments(record, (Map<String, Object>) entry.getValue()));
            } else if (entry.getValue() instanceof String && ((String) entry.getValue()).startsWith("@")) {
                String fieldName = ((String) entry.getValue()).substring(1);
                query.put(entry.getKey(), record.get(fieldName));
            } else {
                query.put(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    public abstract void process(Map<String, Object> record, String inputFolder, String outputFolder);

}
