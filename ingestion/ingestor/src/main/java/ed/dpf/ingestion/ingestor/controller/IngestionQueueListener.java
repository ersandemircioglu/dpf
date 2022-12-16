package ed.dpf.ingestion.ingestor.controller;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ed.dpf.ingestion.ingestor.service.FileIngestor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RabbitListener(queues = "${dpf.ingestion.queue}")
@Slf4j
public class IngestionQueueListener {

    @Autowired
    private FileIngestor fileIngestor;

    @RabbitHandler
    public void receiveMessage(String incomingFileAbsolutePath) {
        log.info("#FILE_RECEIVED# \"{}\"", incomingFileAbsolutePath);
        fileIngestor.processFile(incomingFileAbsolutePath);
    }
}
