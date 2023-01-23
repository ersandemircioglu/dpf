package ed.dpf.process.processor01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Processor01Application {

    public static void main(String[] args) {
        SpringApplication.run(Processor01Application.class, args);
        try (PrintWriter pw = new PrintWriter("/output/" + System.currentTimeMillis())) {
            pw.println("ouput");
            File inFolder = new File("/input/");
            for (String a : inFolder.list()) {
                pw.println(a);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
