package wang.wangby.testcase.service;

import org.springframework.stereotype.Service;
import wang.wangby.testcase.controller.vo.CpuTestInfo;
import wang.wangby.utils.threadpool.Event;
import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolFactory;

@Service
public class CpuTestService {

    ThreadPool pool= ThreadPoolFactory.newPool("CpuTestService",5);
    volatile boolean running=false;
    volatile int taskCount=0;

    public CpuTestInfo start(int taskCount){
        //如果测试已经在执行,就直接返回
        if(running){
            return getInfo();
        }
        running=true;
        this.taskCount=taskCount;
        for(int i=0;i<taskCount;i++){
            pool.execute(new Event() {
                @Override
                public void execute() throws Exception {
                    int no=0;
                    while (running){
                        no++;
                    }
                }
            });
        }
       return getInfo();
    }

    public CpuTestInfo getInfo(){
        ThreadPool.PoolInfo pf=pool.getInfo();
        CpuTestInfo info=new CpuTestInfo();
        info.setRunning(running);
        info.setTaskCount(taskCount);
        info.setPoolInfo(pf);
        return info;
    }

    public CpuTestInfo stop(){
        running=false;
        return getInfo();
    }

}
