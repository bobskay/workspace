package wang.wangby.page.controller.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;
import wang.wangby.page.controller.BaseController;
import wang.wangby.page.model.BootstrapTreeView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ControllerMenuParserTest {

    @Test
    public void createMenus() throws Exception {
        List list=new ArrayList();
        BaseController c=new AutoMenuController();
        c.afterPropertiesSet();
        list.add(c);
        List<BootstrapTreeView> menus= ControllerMenuParser.createMenus(list);
        for(BootstrapTreeView view:menus){
            log.debug(view+"");
        }
    }

    @RequestMapping("/autoMenu")
    public static class AutoMenuController extends BaseController{

        @Menu
        @RequestMapping("/hello")
        @Remark("自动菜单测试")
        public void index(){}

        @Menu("子菜单")
        @RequestMapping("/subMenu")
        public void subMenu(){}
    }
}