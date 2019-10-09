package wang.wangby.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "wang.wangby.bookstore")
public class BookstoreConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(BookstoreConsumerApp.class, args);
    }

}
