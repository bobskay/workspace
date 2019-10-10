package wang.wangby.mservice.utils.controller;


import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.mservice.utils.CustomerHealthIndicator;
import wang.wangby.mservice.utils.service.EurekaClientService;
import wang.wangby.page.controller.BaseController;

import java.io.IOException;

//健康管理,用于手动上下线
@RequestMapping("/healthManager")
@RestController
public class HealthManagerController extends BaseController {

    @Autowired
    CustomerHealthIndicator customerHealthIndicator;
    @Autowired
    ApplicationInfoManager applicationInfoManager;
    @Autowired
    EurekaClientService eurekaClientService;

    @RequestMapping("down")
    public InstanceInfo down() throws IOException {
        customerHealthIndicator.down();
        InstanceInfo instanceInfo=applicationInfoManager.getInfo();
        instanceInfo.setStatus(InstanceInfo.InstanceStatus.DOWN);
        eurekaClientService.changeStatus("DOWN");
        return instanceInfo;
    }

    @RequestMapping("up")
    public InstanceInfo up(){
        customerHealthIndicator.down();
        InstanceInfo instanceInfo=applicationInfoManager.getInfo();
        instanceInfo.setStatus(InstanceInfo.InstanceStatus.UP);
        eurekaClientService.changeStatus("UP");
        return instanceInfo;
    }

    @RequestMapping("info")
    public InstanceInfo info(){
       return applicationInfoManager.getInfo();
    }


}
