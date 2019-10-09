package wang.wangby.page.controller;

import wang.wangby.page.model.BootstrapTreeView;

import java.util.List;

//自己生成菜单的controller
public interface CustomMenuController {

    /**
     * 添加新菜单，创建菜单的时候会依次遍历controller，如果是CustomMenuController就会调用这个方法加入菜单
     * 其它controller采用默认规则
     * @param views 菜单列表
     * */
    void addMenu(List<BootstrapTreeView> views);

}
