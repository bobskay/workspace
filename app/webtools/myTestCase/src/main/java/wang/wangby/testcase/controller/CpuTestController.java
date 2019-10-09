package wang.wangby.testcase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.page.controller.BaseController;
import wang.wangby.model.request.Response;
import wang.wangby.testcase.controller.vo.CpuTestInfo;
import wang.wangby.testcase.service.CpuTestService;

@RestController
@RequestMapping("/cpuTest")
public class CpuTestController extends BaseController {

    @Autowired
    CpuTestService cpuTestService;

    @RequestMapping("index")
    @Menu("cpu测试")
    public String index(){
        return $("index",cpuTestService.getInfo());
    }

    @RequestMapping("start")
    public Response<CpuTestInfo> start(Integer count){
        return respone(cpuTestService.start(count));
    }

    @RequestMapping("stop")
    public Response<CpuTestInfo> stop(Integer count){
        return respone(cpuTestService.stop());
    }
}
