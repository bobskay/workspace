package wang.wangby.zk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;
import wang.wangby.utils.StringUtil;
import wang.wangby.zk.model.ZkInfo;
import wang.wangby.zk.service.ZkInfoService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/zkInfo")
public class ZkInfoController extends BaseController {

    @Autowired
    ZkInfoService zkInfoService;

    @RequestMapping("/index")
    @Menu("zookeeper配置")
    public String index() {
        return $("index");
    }

    @RequestMapping("/insert")
    @Remark("新增")
    public Response<ZkInfo> insert(ZkInfo zkInfo) throws Exception {
        if(StringUtil.isEmpty(zkInfo.getName())){
            zkInfo.setName(zkInfo.getAddress());
        }
        zkInfoService.insert(zkInfo);
        return respone(zkInfo);
    }

    @RequestMapping("/prepareInsert")
    @Remark("进入新增页面")
    public String prepareInsert() {
        return $("prepareInsert");
    }

    @RequestMapping("/getAll")
    @Remark("获得所有配置")
    public Response<List<ZkInfo>> getAll() throws Exception {
        List list= zkInfoService.getAll();
        return respone(list);
    }

    @RequestMapping("/deleteById")
    @Remark("删除配置")
    public Response<Integer> deleteById(Long[] id) throws IOException {
        for(long i:id){
            zkInfoService.delete(i);
        }
        return respone(id.length);
    }
}
