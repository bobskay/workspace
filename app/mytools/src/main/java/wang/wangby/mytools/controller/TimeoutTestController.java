package wang.wangby.mytools.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;

@RestController
@RequestMapping("timeoutTest")
public class TimeoutTestController extends BaseController {

    @RequestMapping("/sleep")
    @HystrixCommand(fallbackMethod = "sleepFallback")
    public Response<String> sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
        return Response.success("ok");
    }

    public Response<String> sleepFallback(int millis) throws InterruptedException {
        return Response.success("sleep超时");
    }

}
