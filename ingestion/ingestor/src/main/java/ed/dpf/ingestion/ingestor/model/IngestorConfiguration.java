package ed.dpf.ingestion.ingestor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class IngestorConfiguration {
    private String pattern;
    private List<FieldToValueMap> valueMap = new ArrayList<>();
}
