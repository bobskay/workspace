package wang.wangby.mserviceadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import wang.wangby.autoconfigure.dao.DaoAutoConfiguration;
import wang.wangby.mservice.utils.config.MserviceUtilAutoConfiguration;
import wang.wangby.mserviceadmin.config.MserviceAdminAutoConfiguration;
import wang.wangby.page.config.IndexPageAutoConfiguration;

@SpringBootApplication
@EnableDiscoveryClient
@Import({MserviceAdminAutoConfiguration.class,
        MserviceUtilAutoConfiguration.class,
        IndexPageAutoConfiguration.class, DaoAutoConfiguration.class})
public class MserviceAdminApp {
    public static void main(String args[]) {
        SpringApplication.run(MserviceAdminApp.class, args);
    }

}