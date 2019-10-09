package wang.wangby.hystrixdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Import;
import wang.wangby.page.config.IndexPageAutoConfiguration;

@SpringBootApplication
@EnableHystrixDashboard
@Import(IndexPageAutoConfiguration.class)
public class HystrixDashboardApplication {
	public static void main(String[] args) throws ClassNotFoundException {
		SpringApplication.run(HystrixDashboardApplication.class, args);
	}
}
