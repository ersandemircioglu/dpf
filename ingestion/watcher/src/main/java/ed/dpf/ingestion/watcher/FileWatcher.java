package ed.dpf.ingestion.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileWatcher {

    @Value("${dpf.ingestion.incoming_folder}")
    private String incomingFolder;

    @Value("${dpf.ingestion.queue}")
    private String queueName;

    @Autowired
    private RabbitTemplate template;

    public FileWatcher() {
        run();
    }

    public void run() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();

                    Path path = Paths.get(incomingFolder);

                    path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

                    WatchKey key;
                    while ((key = watchService.take()) != null) {
                        for (WatchEvent<?> event : key.pollEvents()) {
                            log.info("#FILE_RECEIVED# \"{}\"", event.context());
                            try {
                                template.convertAndSend(queueName, event.context().toString());
                            } catch (AmqpException e) {
                                log.error("#PROCESS_FAILED# \"{}\" #MSG# MQ is not reachable: \"{}\"",event.context(), e.getMessage(), e);
                            }
                        }
                        key.reset();
                    }
                } catch (IOException | InterruptedException e) {
                    log.error("FileWatcher Thread is stopped", e);
                }

            }
        });
        thread.setUncaughtExceptionHandler((th, e) -> log.error("FileWatcher Thread is stopped (uncaughtException)", e));
        thread.start();
    }

}
