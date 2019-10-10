package wang.wangby.mservice.utils.service;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MyRule extends AbstractLoadBalancerRule  {

    //每次调用就+1
    AtomicInteger count=new AtomicInteger(0);

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        return this.choose(this.getLoadBalancer(), key);
    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        while (true){
            List<Server> upList = lb.getReachableServers();
            if(upList.size()==0){
                return null;
            }
            int i=count.incrementAndGet()%upList.size();
            Server server=upList.get(Math.abs(i));
            if(server.isAlive()){
                return server;
            }
            Thread.yield();
        }

    }

}
