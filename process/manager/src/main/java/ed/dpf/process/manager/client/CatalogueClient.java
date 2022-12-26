package ed.dpf.process.manager.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "catalogue", url = "${dpf.catalogue.url}", configuration = CatalogueClientConfiguration.class)
public interface CatalogueClient {
    @RequestMapping(method = RequestMethod.POST, value = "/product_database/_find")
    CatalogueQueryResult findProduct(Map<String, Object> fields);
}
