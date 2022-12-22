package ed.dpf.ingestion.ingestor.util;

import java.io.File;
import java.util.Map;

public interface Ingestor {

    public boolean matches(File file);

    public Map<String, Object> parse(File file);
}
