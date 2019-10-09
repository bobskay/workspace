package wang.wangby.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import wang.wangby.autoconfigure.dao.DaoAutoConfiguration;
import wang.wangby.page.config.IndexPageAutoConfiguration;

import java.util.Map;
import java.util.Set;

@SpringBootApplication
@Import({IndexPageAutoConfiguration.class, DaoAutoConfiguration.class})
public class BookstoreApp {
    public static void main(String args[]){
        ApplicationContext applicationContext=SpringApplication.run(BookstoreApp.class, args);


    }
}