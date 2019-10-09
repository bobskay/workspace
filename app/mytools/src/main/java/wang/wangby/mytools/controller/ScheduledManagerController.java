package wang.wangby.mytools.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("scheduledManager")
public class ScheduledManagerController  extends BaseController {

    @RequestMapping("/index")
    @Menu("计划任务管理")
    public String index( ) {
        Collection collection= ScheduledFactory.secheduleMap().values();
        List<JobInfo> jobs=new ArrayList<>(collection);
        return $("index",jobs);
    }


    @RequestMapping("/remove")
    public Response<String> stopJob(String name) {
        ScheduledFactory.stopJob(name);
        return respone("ok");
    }
}
