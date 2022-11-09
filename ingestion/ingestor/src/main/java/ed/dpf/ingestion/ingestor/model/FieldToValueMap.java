package ed.dpf.ingestion.ingestor.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldToValueMap {
    private String field;
    private ValueType valueType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String format;
}
