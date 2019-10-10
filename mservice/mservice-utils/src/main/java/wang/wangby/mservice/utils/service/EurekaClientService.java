package wang.wangby.mservice.utils.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wang.wangby.utils.http.HttpConfig;
import wang.wangby.utils.http.HttpUtil;

import java.io.IOException;

@Service
@Slf4j
public class EurekaClientService {

    @Value("${eureka.client.service-url.defaultZone}")
    String eurekaUrls;
    @Value("${spring.application.name}")
    String applicationName;
    @Value("${eureka.instance.instance-id}")
    String instanceId;
    CloseableHttpClient httpClient=HttpUtil.createClient(new HttpConfig());


    //http://eureka1:7000/eureka/apps/BOOKSTORE-PROVIDER/bookstoreProvider3
    public void removeCurrent() throws IOException {
        iteratorInvoke((url,httpClient)->{
            url+="apps/"+applicationName+"/"+instanceId;
            HttpUtil.delete(url, null,httpClient);
        });
    }

    //http://eureka1:7000/eureka/apps/mservice-admin/mservice-admin1/status?value=DOWN
    public void changeStatus(String status){
        iteratorInvoke((url,httpClient)->{
            url+="apps/"+applicationName+"/"+instanceId+"/status?value="+status;
            HttpUtil.put(url,"",null,httpClient);
        });

    }

    //遍历eureka服务器
    private void iteratorInvoke(Invoke invoke){
        for(String base:eurekaUrls.split(",")){
            String url=base.trim();
            if(!url.endsWith("/")){
                url+="/";
            }
            try{
                invoke.doInvoke(url,httpClient);
            }catch (Exception ex){
                log.warn("调用eureka服务器出错url={},错误信息:{}",url,ex.getMessage(),ex);
            }
        }
    }

    public interface Invoke{
         void doInvoke(String  url, CloseableHttpClient httpClient) throws Exception;
    }
}
