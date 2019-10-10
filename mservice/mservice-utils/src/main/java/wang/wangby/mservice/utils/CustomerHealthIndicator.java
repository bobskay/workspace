package wang.wangby.mservice.utils;


import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class CustomerHealthIndicator extends AbstractHealthIndicator {

    private boolean health=true;

    @Override
    protected void doHealthCheck(Health.Builder builder)  {
        if (health) {
            builder.up();
        } else {
            builder.down();
        }
    }

    public void down(){
        this.health=false;
    }
    public void up(){
        this.health=true;
    }
}