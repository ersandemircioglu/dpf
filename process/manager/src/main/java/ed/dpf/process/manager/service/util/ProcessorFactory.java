package ed.dpf.process.manager.service.util;

import ed.dpf.process.manager.model.ProcessorConfiguration;

public class ProcessorFactory {
    public Processor getProcessor(ProcessorConfiguration configuration) {
        switch (configuration.getProcessorType()) {
        case DOCKER:
            return new DockerProcessor(configuration);
        case STREAM:
            return new StreamProcessor(configuration);
        case HTC:
            return new HTCProcessor(configuration);
        default:
            throw new UnsupportedOperationException("Unsupported processor type");
        }
    }
}
