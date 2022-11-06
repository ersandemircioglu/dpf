package ed.dpf.ingestion.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
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
                            System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                            template.convertAndSend(queueName, event.context().toString());
                        }
                        key.reset();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

}
