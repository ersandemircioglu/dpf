package ed.dpf.ingestion.ingestor.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "catalogue", url = "${dpf.catalogue.url}", configuration = CatalogueClientConfiguration.class)
public interface CatalogueClient {
    @PutMapping(value = "/product_database/{id}")
    String addProduct(@PathVariable("id") String id, Map<String, Object> fields);
}
