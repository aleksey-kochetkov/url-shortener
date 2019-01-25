package url_shortener;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        SpringApplication.run(Application.class, args);
    }
}