package ed.dpf.ingestion.ingestor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import ed.dpf.ingestion.ingestor.model.FieldToValueConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldToValueParser {
    private FieldToValueConfiguration configuration;

    public FieldToValueParser(FieldToValueConfiguration configuration) {
        this.configuration = configuration;
    }

    public Object parse(String field) {
        Object output = field;
        try {
            switch (configuration.getValueType()) {
                case DATETIME:
                    SimpleDateFormat sf = new SimpleDateFormat(configuration.getFormat());
                    output = sf.parse(field);
                    break;
                case FLOAT:
                    output = Float.valueOf(field);
                    break;
                case INTEGER:
                    output = Integer.valueOf(field);
                    break;
                case STRING:
                default:
                    output = field;
                    break;
            }
        } catch (NumberFormatException | ParseException e) {
            log.error("Failed to parse \"{}\" value as \"{}\" type (with \"{}\" format)", field, configuration.getValueType(), configuration.getFormat(), e);
        }

        return output;
    }
}
