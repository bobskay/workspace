package wang.wangby.mservice.utils.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;
import wang.wangby.utils.StringUtil;

import java.util.Set;

@RequestMapping("environment")
@RestController
public class EnvironmentController extends BaseController {
    @Autowired
    org.springframework.cloud.context.refresh.ContextRefresher contextRefresher;

    @Autowired
    Environment environment;

    @RequestMapping("refresh")
    public Response<Set<String>> refresh(){
       return respone( contextRefresher.refresh());
    }

    @RequestMapping("getValue")
    public String getValue(String key){
        if(StringUtil.isEmpty(key)){
            return "key不能为空";
        }
        return environment.getProperty(key);
    }
}
