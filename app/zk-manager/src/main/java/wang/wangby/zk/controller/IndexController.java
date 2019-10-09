package wang.wangby.zk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;
import wang.wangby.page.model.BootstrapTreeView;
import wang.wangby.zk.model.ZkInfo;
import wang.wangby.zk.service.ZkInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IndexController extends BaseController {

    @Autowired
    ZkInfoService zkInfoService;

    @RequestMapping({"/","/index"})
    @ResponseBody
    public String index(String forward) {
        Map map=new HashMap();
        map.put("title", "zookeeper");
        map.put("forward", forward);
        return $("/index",map);
    }

    @RequestMapping("/treeView")
    @ResponseBody
    public Response<List<BootstrapTreeView>> treeView() throws Exception {
        List list=new ArrayList(super.getDefaultMenu());
        for(ZkInfo zkInfo:zkInfoService.getAll()){
            list.add(BootstrapTreeView.newInstance(zkInfo.getName(),zkInfo.getName(),"/zkTree/index?id="+zkInfo.getId()));
        }
        return respone(list);
    }
}
