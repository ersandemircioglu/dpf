package ed.dpf.ingestion.ingestor.util;

import ed.dpf.ingestion.ingestor.model.IngestorConfiguration;

public class IngestorFactory {

    public Ingestor getIngestor(IngestorConfiguration configuration) {
        switch (configuration.getParser()) {
            case FILENAME:
                return new FilenameIngestor(configuration);
            case METADATA:
            default:
                throw new UnsupportedOperationException("Unsupported ingestor type");
        }
    }

}
