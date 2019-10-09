package wang.wangby.mytools.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.api.Param;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;

import java.util.Random;

@RequestMapping("requestTest")
@RestController
public class RequesTestController extends BaseController {

    @RequestMapping("index")
    @Menu("模拟调用")
    public String index() {
        return $("index");
    }

    @Remark("模拟耗时调用")
    @RequestMapping("invoke")
    @Param("最短耗时,默认0")
    @Param("最长耗时,默认1000")
    public Response<Long> invoke(Integer min, Integer max) throws InterruptedException {
        long time=getSleep(min,max);
        Thread.sleep(time);
        return respone(time);
    }

    //获取睡眠时间
    long getSleep(Integer min,Integer max){
        if(min==null){
            min=0;
        }
        if(max==null){
            max=10;
        }
        if(max<min){
            return 0;
        }
        int range = max - min;
        if(range==0){
            return max;
        }
        Random rd = new Random();
        return rd.nextInt(range)+min;
    }
}
