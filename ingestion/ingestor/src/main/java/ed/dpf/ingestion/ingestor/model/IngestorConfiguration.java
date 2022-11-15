package ed.dpf.ingestion.ingestor.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class IngestorConfiguration {
    private String name;
    private String regex;
    private Map<String, FieldToValueConfiguration> fieldToValueConfMap = new HashMap<>();
}
