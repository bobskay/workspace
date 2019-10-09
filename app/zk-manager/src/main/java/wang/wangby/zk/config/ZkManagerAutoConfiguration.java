package wang.wangby.zk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wang.wangby.dao.file.DataSerializer;
import wang.wangby.dao.file.FileDao;

import java.io.IOException;

@Configuration
public class ZkManagerAutoConfiguration {

    @Bean
    public FileDao fileDao() throws IOException {
        DataSerializer dataSerializer=new DataSerializer();
        return new FileDao(dataSerializer,"/opt/data/myfile");
    }
}
