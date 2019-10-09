package wang.wangby.mytools.controller;

import org.junit.Test;
import wang.wangby.test.TestBase;

import java.util.List;

public class AlertReceiverControllerTest extends TestBase {

    @Test
    public void insert() {
        AlertReceiverController c=new AlertReceiverController();
        c.insert("第1条消息");
        c.insert("第2条消息");
        List list=c.print();
        assertNotEmpty(list);
    }
}