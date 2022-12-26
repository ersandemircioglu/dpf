package ed.dpf.process.manager.client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArchiveClient {
    public void retrieve(String archivePath, String destinationPath) {
        try {
            Path source = Paths.get(archivePath);
            Files.copy(source, Paths.get(destinationPath).resolve(source.getFileName()));
            log.info("#FILE_RETREIVED# \"{}\"", archivePath);
        } catch (Exception e) {
            log.error("#FILE_RETREIVE_FAILED# \"{}\" #MSG# : \"{}\"", archivePath, e.getMessage(), e);
        }
    }
}
