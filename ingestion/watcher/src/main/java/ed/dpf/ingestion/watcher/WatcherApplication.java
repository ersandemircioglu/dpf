package ed.dpf.ingestion.watcher;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WatcherApplication {

    @Value("${dpf.ingestion.queue}")
    private String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }


    public static void main(String[] args) {
        SpringApplication.run(WatcherApplication.class, args);
    }
}
