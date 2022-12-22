package ed.dpf.process.manager.service.util;

import java.util.Map;
import java.util.Map.Entry;

import ed.dpf.process.manager.client.CatalogueClient;
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

    public abstract void process(CatalogueClient catalogueClient, Map<String, Object> message);
}
