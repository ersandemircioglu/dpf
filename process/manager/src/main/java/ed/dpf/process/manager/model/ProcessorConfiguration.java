package ed.dpf.process.manager.model;

import lombok.Data;

@Data
public class ProcessorConfiguration {
    private String name;
    private String triggerFileRegex;
    private String dockerImage;
}
