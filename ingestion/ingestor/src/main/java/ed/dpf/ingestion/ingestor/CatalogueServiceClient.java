package ed.dpf.ingestion.ingestor;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "catalogue", url = "${dpf.catalogue.url}", configuration = CatalogueServiceConfiguration.class)
public interface CatalogueServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/product_database/_all_docs")
    String getProducts();

    @RequestMapping(method = RequestMethod.PUT, value = "/product_database/{id}")
    String addProduct(@PathVariable("id") String id, Map<String, String> fields);

    @RequestMapping(method = RequestMethod.POST, value = "/product_database/_find", consumes = MediaType.APPLICATION_JSON_VALUE)
    String findProduct(@RequestBody String searchValue);
}
