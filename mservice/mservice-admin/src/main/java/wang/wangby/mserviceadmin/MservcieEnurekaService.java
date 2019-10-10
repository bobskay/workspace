package wang.wangby.mserviceadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wang.wangby.mservice.config.model.AppConfig;
import wang.wangby.mservice.config.service.AppConfigService;
import wang.wangby.mserviceadmin.model.vo.EurekaClientInfo;
import wang.wangby.mserviceadmin.model.vo.InstanceXml;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.XmlUtils;
import wang.wangby.utils.http.HttpUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MservcieEnurekaService {

    @Autowired
    AppConfigService appConfigService;
    @Autowired
    @Value("${eureka.client.service-url.defaultZone}")
    String eurekaUrls;

    //获得系统所有instance
    public List<EurekaClientInfo> getAllClient() {
        return selectClient(null, null);
    }

    public List<EurekaClientInfo> getClientByServiceName(String serviceName) {
        return selectClient(serviceName, null);
    }

    public EurekaClientInfo getClient(String instanceId) {
        List<EurekaClientInfo> list = selectClient(null, instanceId);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }


    //serviceName=Application,instanceId为key=eureka.instance.instance-id的value值
    //如果两个参数都没传就查找所有key=eureka.instance.instance-id的记录
    private List<EurekaClientInfo> selectClient(String serviceName, String instanceId) {
        AppConfig appConfig = new AppConfig();
        appConfig.setVarKey("eureka.instance.instance-id");
        if (!StringUtil.isEmpty(serviceName)) {
            appConfig.setApplication(serviceName);
        }
        if (!StringUtil.isEmpty(instanceId)) {
            appConfig.setVarValue(instanceId);
        }
        List<AppConfig> list = appConfigService.list(appConfig);

        List<EurekaClientInfo> result = new ArrayList<>();
        for (AppConfig c : list) {
            EurekaClientInfo clientInfo = new EurekaClientInfo();
            clientInfo.setInstanceId(c.getVarValue());
            clientInfo.setServiceName(c.getApplication());
            clientInfo.setStatus("down");
            result.add(clientInfo);
        }
        return result;
    }

    public InstanceXml getByApi(String appId,String instanceId) throws IOException, JAXBException {
        String base=eurekaUrls.split(",")[0];
        if(!base.endsWith("/")){
            base+="/";
        }
        String url=base+"/apps/"+appId+"/"+instanceId;
        String xml = HttpUtil.get(url);
        if(StringUtil.isEmpty(xml)){
            return null;
        }
        InstanceXml instanceXml = XmlUtils.toBean(xml, InstanceXml.class);
        return instanceXml;
    }
}

