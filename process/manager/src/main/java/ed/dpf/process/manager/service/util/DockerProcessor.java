package ed.dpf.process.manager.service.util;

import java.util.Map;

import ed.dpf.process.manager.model.ProcessorConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerProcessor extends Processor {

    public DockerProcessor(ProcessorConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void process(Map<String, Object> record, String inputFolder, String outputFolder) {
        log.info("#RECORD_PROCESSING# \"{}\"", record.get("filename"));
        record.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));
//        ContainerManager containerManager = new ContainerManager();
//        containerManager.start(configuration.getProcessor(), inputFolder, outputFolder);
    }
}
