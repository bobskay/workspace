package wang.wangby.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class HttpUtilTest extends TestBase {

    @Test
    public void getTest() throws URISyntaxException, IOException {
       String s=HttpUtil.get("https://github.com/");
        log.debug(s);
    }
}
