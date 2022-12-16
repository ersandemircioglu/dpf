package ed.dpf.ingestion.ingestor.client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArchiveClient {
    @Value("${dpf.archive.directory}")
    private String archiveDirectory;

    public String archive(String incomingFileAbsolutePath) {
        String output = null;
        try {
            Path source = Paths.get(incomingFileAbsolutePath);
            Path destPath = Files.move(source, Paths.get(archiveDirectory).resolve(source.getFileName()));
            output = destPath.toFile().getAbsolutePath();
            log.info("#FILE_ARCHIVED# \"{}\"", incomingFileAbsolutePath);
        } catch (Exception e) {
            log.error("#FILE_ARCHIVE_FAILED# \"{}\" #MSG# : \"{}\"", incomingFileAbsolutePath, e.getMessage(), e);
        }
        return output;
    }
}
