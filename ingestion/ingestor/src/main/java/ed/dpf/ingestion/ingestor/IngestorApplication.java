package ed.dpf.ingestion.ingestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IngestorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IngestorApplication.class, args);
    }
}
