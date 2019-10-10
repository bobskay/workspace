package wang.wangby.mserviceadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.mserviceadmin.MservcieEnurekaService;
import wang.wangby.mserviceadmin.model.vo.EurekaClientInfo;
import wang.wangby.mserviceadmin.model.vo.InstanceXml;
import wang.wangby.page.controller.BaseController;
import wang.wangby.page.model.Ztree;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.http.HttpUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/mserviceEureka")
public class MserviceEurekaController extends BaseController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    MservcieEnurekaService mservcieEnurekaService;

    @RequestMapping("/index")
    @Menu("服务管理")
    public String index(){
        return $("index");
    }

    @RequestMapping("/tree")
    public Response<List<Ztree>> tree() {
        List<Ztree> trees=new ArrayList<>();
        //已经添加的服务
        Set<String> allServiceName=new HashSet<>();
        List<EurekaClientInfo> list=mservcieEnurekaService.getAllClient();
        fillInstance(list);
        for(EurekaClientInfo client:list){
            for(String serviceId:discoveryClient.getServices()){
                for(ServiceInstance serviceInstance :discoveryClient.getInstances(serviceId)){
                    if(client.getInstanceId().equalsIgnoreCase(serviceInstance.getInstanceId())){
                        client.setServiceInstance(serviceInstance);
                        client.setStatus("up");
                        break;
                    }
                }
            }

            if(!allServiceName.contains(client.getServiceName())){
                Ztree serviceName = new Ztree();
                serviceName.setParentId("");
                serviceName.setId(client.getServiceName());
                serviceName.setName(client.getServiceName());
                trees.add(serviceName);
            }

            Ztree instance = new Ztree();
            instance.setParentId(client.getServiceName());
            instance.setId(client.getInstanceId());
            instance.setName(client.getInstanceId());
            trees.add(instance);
        }

        List<Ztree>  tree= Ztree.createTree(trees,t->t);
        return respone(tree);
    }


    @RequestMapping("/get")
    public String get(Ztree ztree) throws IOException, JAXBException {
        //某个目录下所有服务
        if(StringUtil.isEmpty(ztree.getParentId())){
            List<EurekaClientInfo> list=mservcieEnurekaService.getClientByServiceName(ztree.getId());
            fillInstance(list);
            for(EurekaClientInfo eurekaClientInfo:list){
                setStatus(eurekaClientInfo);
            }
            return $("getService",list);
        }
        EurekaClientInfo info=mservcieEnurekaService.getClient(ztree.getId());
        List<EurekaClientInfo> list=new ArrayList();
        list.add(info);
        fillInstance(list);

        EurekaClientInfo eurekaClientInfo=list.get(0);
        setStatus(eurekaClientInfo);
        return $("get",eurekaClientInfo);
    }

    //根据instanceId填充从eureka服务器里发现的实例
    private void fillInstance(List<EurekaClientInfo> clientInfos) {
        for (EurekaClientInfo client : clientInfos) {
            lab:
            for (String serviceId : discoveryClient.getServices()) {
                for (ServiceInstance serviceInstance : discoveryClient.getInstances(serviceId)) {
                    if (client.getInstanceId().equalsIgnoreCase(serviceInstance.getInstanceId())) {
                        client.setServiceInstance(serviceInstance);
                        client.setStatus(EurekaClientInfo.Status.UP+"");
                        break lab;
                    }
                }
            }
        }
    }

    private void setStatus(EurekaClientInfo eurekaClientInfo) throws IOException, JAXBException {
        InstanceXml xml=mservcieEnurekaService.getByApi(eurekaClientInfo.getServiceName(),eurekaClientInfo.getInstanceId());
        if(xml==null){
            eurekaClientInfo.setStatus(EurekaClientInfo.Status.DOWN+"");
        }else{
            eurekaClientInfo.setStatus(xml.getStatus());
        }
    }

    @RequestMapping("down")
    @Remark("将某台服务器下线")
    public Response<String> down(String serviceName,String instanceId) throws IOException, JAXBException {
        InstanceXml instanceXml= mservcieEnurekaService.getByApi(serviceName,instanceId);
        String result=HttpUtil.get(instanceXml.getHomePageUrl()+"healthManager/down");
        return respone(result);
    }

    @RequestMapping("up")
    @Remark("将某台服务器上线")
    public Response<String> up(String serviceName,String instanceId) throws IOException, JAXBException {
        InstanceXml instanceXml= mservcieEnurekaService.getByApi(serviceName,instanceId);
        String result=HttpUtil.get(instanceXml.getHomePageUrl()+"healthManager/up");
        return respone(result);
    }


    private ServiceInstance getInstance(String instanceId){
        for (String serviceId : discoveryClient.getServices()) {
            for (ServiceInstance serviceInstance : discoveryClient.getInstances(serviceId)) {
                if(serviceInstance.getInstanceId().equalsIgnoreCase(instanceId)){
                   return serviceInstance;
                }
            }
        }
        return null;
    }
}
