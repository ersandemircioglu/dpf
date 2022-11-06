package ed.dpf.ingestion.ingestor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${dpf.ingestion.queue}")
public class FileIngestor {

    @Autowired
    CatalogueServiceClient catalogueServiceClient;

    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println(catalogueServiceClient.getProducts());

        Map<String, String> map = new HashMap<>();
        map.put("filename", message);
        map.put("prop_1", "product");
        map.put("prop_2", "file");
        map.put("prop_3", "txt");
        map.put("prop_4", "deneme");

        try {
            System.out.println(catalogueServiceClient.addProduct(message, map));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            System.out.println("query");
            System.out.println(catalogueServiceClient.findProduct("{\"selector\": {\"prop_3\": {\"$eq\": \"txt\"},\"prop_4\": {\"$eq\": \"deneme1\"}}}"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Received <" + message + ">");
    }
}
