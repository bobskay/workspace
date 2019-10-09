package wang.wangby.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LogLevelTest {

    @Test
    public void changeLevel(){
        log.debug("可以看见debug");
        LogLevel.info(this.getClass());
        log.debug("leve改为jinfo了,debug消失");
        log.info("可以见info");
    }
}
