package ed.dpf.process.manager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "catalogue", url = "${dpf.catalogue.url}", configuration = CatalogueClientConfiguration.class)
public interface CatalogueClient {
    @RequestMapping(method = RequestMethod.GET, value = "/product_database/_all_docs")
    String getProducts();

    @RequestMapping(method = RequestMethod.POST, value = "/product_database/_find", consumes = MediaType.APPLICATION_JSON_VALUE)
    String findProduct(@RequestBody String searchValue);
}
