package ed.dpf.ingestion.ingestor.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${dpf.ingestion.queue}")
public class FileListener {

    @RabbitHandler
    public void receiveMessage(String message) {

    }
}
