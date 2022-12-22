package ed.dpf.process.manager.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ProcessorConfiguration {
    private String name;
    private Map<String, Object> filter = new HashMap<>();
    private ProcessorType processorType;
    private String processor;
    private Map<String, InputConfiguration> inputs = new HashMap<>();
}
