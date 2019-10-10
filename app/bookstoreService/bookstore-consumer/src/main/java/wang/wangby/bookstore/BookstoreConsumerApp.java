package wang.wangby.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import wang.wangby.mservice.utils.config.MserviceUtilAutoConfiguration;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "wang.wangby.bookstore")
@Import(MserviceUtilAutoConfiguration.class)
public class BookstoreConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(BookstoreConsumerApp.class, args);
    }


}
