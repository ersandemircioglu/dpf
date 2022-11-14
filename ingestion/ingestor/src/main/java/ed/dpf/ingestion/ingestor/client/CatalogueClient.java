package ed.dpf.ingestion.ingestor.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "catalogue", url = "${dpf.catalogue.url}", configuration = CatalogueClientConfiguration.class)
public interface CatalogueClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/product_database/{id}")
    String addProduct(@PathVariable("id") String id, Map<String, Object> fields);
}
