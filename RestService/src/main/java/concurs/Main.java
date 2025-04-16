package concurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

@ComponentScan({"concurs.repository.jdbc"})
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Bean(name = "props")
  public Properties getProperties() {
    Properties props = new Properties();
    try (InputStream fis = getClass().getClassLoader().getResourceAsStream("application.properties")) {
      if (fis != null) {
        props.load(fis);
      } else {
        System.err.println("server.properties not found in classpath.");
      }
    } catch (IOException e) {
      System.err.println("Error loading server.properties: " + e.getMessage());
    }
    return props;
  }
}


