package ed.dpf.ingestion.ingestor.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@RabbitListener(queues = "${dpf.ingestion.queue}")
@Slf4j
public class IngestionQueueListener {

    @Autowired
    private FileIngestor fileIngestor;

    @RabbitHandler
    public void receiveMessage(String message) {
        log.info(message);
        fileIngestor.ingestFilename(message);
    }
}
