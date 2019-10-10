package wang.wangby.bookstore;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import wang.wangby.autoconfigure.dao.DaoAutoConfiguration;
import wang.wangby.mservice.utils.config.MserviceUtilAutoConfiguration;
import wang.wangby.page.config.IndexPageAutoConfiguration;

@SpringBootApplication
@EnableHystrix
@Import({IndexPageAutoConfiguration.class,
        MserviceUtilAutoConfiguration.class,
        DaoAutoConfiguration.class})
public class BookstoreProviderApp {
    public static void main(String args[]){
        SpringApplication.run(BookstoreProviderApp.class, args);
    }


    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}