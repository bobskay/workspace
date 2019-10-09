package wang.wangby.echars4j.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;

@Slf4j
public class CharUtilTest extends TestBase {

    @Test
    public void createxAxisByNo() {
        long[] xs=CharUtil.createxAxis(0,10,1);
        assertNotEmpty(xs);

        xs=CharUtil.createxAxis(0,11,3);
        assertNotEmpty(xs);
    }
}