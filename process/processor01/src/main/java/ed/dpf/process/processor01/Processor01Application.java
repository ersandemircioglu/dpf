package ed.dpf.process.processor01;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Processor01Application {

    public static void main(String[] args) {
        SpringApplication.run(Processor01Application.class, args);
        System.out.println("Burada");
        File output = new File("/out/deneme.txt");
        try(PrintWriter pw = new PrintWriter(output)){
            pw.println(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
