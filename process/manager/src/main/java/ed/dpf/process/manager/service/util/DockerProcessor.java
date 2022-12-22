package ed.dpf.process.manager.service.util;

import java.util.Map;
import java.util.function.Consumer;

import ed.dpf.process.manager.client.CatalogueClient;
import ed.dpf.process.manager.model.InputConfiguration;
import ed.dpf.process.manager.model.ProcessorConfiguration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerProcessor extends Processor {

    public DockerProcessor(ProcessorConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void process(CatalogueClient catalogueClient, Map<String, Object> record) {
        log.info("#RECORD_PROCESSING# \"{}\"", record.get("filename"));
        record.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));
        configuration.getInputs().values().forEach(new Consumer<InputConfiguration>() {
            @Override
            public void accept(InputConfiguration v) {
                System.out.println(v.getQuery().toString());
                System.out.println(catalogueClient.findProduct(v.getQuery()));
            }
        });

    }
}
