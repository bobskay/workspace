package wang.wangby.page.controller.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;
import wang.wangby.page.controller.BaseController;
import wang.wangby.page.controller.CustomMenuController;
import wang.wangby.page.model.BootstrapTreeView;
import wang.wangby.page.model.vo.ControllerMethod;
import wang.wangby.utils.ClassUtil;
import wang.wangby.utils.CollectionUtil;
import wang.wangby.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ControllerMenuParser {

    public static List<BootstrapTreeView> createMenus(Collection<BaseController> baseControllers) {
        List<BaseController> controllers = new ArrayList<>(baseControllers);
        CollectionUtil.sort(controllers, BaseController::order);

        List<BootstrapTreeView> views = new ArrayList<>();
        for (BaseController controller : controllers) {
            if(controller instanceof CustomMenuController){
                ((CustomMenuController) controller).addMenu(views);
                continue;
            }

            String className=controller.getPath();
            className=className.replaceAll("/","");

            List<ControllerMethod> controllerMethods = new ArrayList();
            ControllerMethod index = null;
            for (Method method : ClassUtil.getMethodsByAnnotation(controller.getClass(),Menu.class)) {
                ControllerMethod md=createMethod(method);
                controllerMethods.add(md);
                if (method.getName().equalsIgnoreCase("index")) {
                    index = md;
                }
            }
            //如果没有方法标记menu直接返回
            if (controllerMethods.size() == 0) {
                log.debug("类上未标记任何菜单,跳过:" + controller.getClass().getName());
                continue;
            }
            //只有一个方法加在主菜单上
            if (controllerMethods.size() == 1) {
                ControllerMethod md = controllerMethods.get(0);
                log.debug("找到唯一菜单:" + showName(md)+"="+url(md));
                views.add(BootstrapTreeView.newInstance(className, showName(md), url(md)));
                continue;
            }
            //将index作为目录,其它的做为子目录
            BootstrapTreeView view = null;
            if (index != null) {
                log.debug("找到首页:" + name(index) + "=" + url(index));
                view = BootstrapTreeView.newInstance(className, showName(index), url(index));
            } else {
                log.debug("找到目录:" + className(controller.getClass()));
                view = BootstrapTreeView.newInstance(className, className(controller.getClass()), null);
            }
            view.setNodes(new ArrayList());
            for (ControllerMethod md : controllerMethods) {
                if (md == index) {
                    continue;
                }
                log.debug("---添加菜单:" + showName(md) + "=" + url(md));
                view.addNode(name(md), showName(md), url(md));
            }
            views.add(view);
        }
        return views;
    }

    //名称,调用菜单时的标识
    public static String name(ControllerMethod controllerMethod) {
        return controllerMethod.getMethod().getDeclaringClass().getSimpleName() + "_" + controllerMethod.getMethod().getName();
    }

    //显示用菜单时名称
    public static String showName(ControllerMethod controllerMethod) {
        if (controllerMethod.getMenu()!=null&&StringUtil.isNotEmpty(controllerMethod.getMenu().value())) {
            return controllerMethod.getMenu().value();
        }
        return getRemark(controllerMethod.getRemark(), controllerMethod.getMethod().getName());
    }

    //提取remark注释里逗号前的内容
    public static String getRemark(Remark remark, String defaultName) {
        if (remark == null || StringUtil.isEmpty(remark.value())) {
            return defaultName;
        }
        String value = remark.value();
        int i = value.indexOf(',');
        if (i == -1) {
            return value;
        }
        return value.substring(0, i);
    }

    public static String className(Class clazz) {
        Remark remark = (Remark) clazz.getAnnotation(Remark.class);
        String name = clazz.getSimpleName();
        name = StringUtil.firstLower(name);
        return getRemark(remark, name);
    }

    //方法对应的访问地址
    public static String url(ControllerMethod method) {
        String dir = getPath(method.getClassRequestMapping());
        String name = getPath(method.getRequestMapping());
        if (StringUtil.isEmpty(dir)) {
            return "/" + name;
        }
        return "/" + dir + "/" + name;
    }

    private static String getPath(RequestMapping requestMapping) {
        if (requestMapping == null) {
            return "";
        }
        String[] value = requestMapping.value();
        if (value == null || value.length == 0) {
            return "";
        }
        String path = value[0];
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static ControllerMethod createMethod(Method method) {
        Menu menu = AnnotationUtils.getAnnotation(method, Menu.class);
        ControllerMethod md = new ControllerMethod();
        md.setClassRequestMapping(AnnotationUtils.getAnnotation(method.getDeclaringClass(), RequestMapping.class));
        md.setMethod(method);
        md.setClassRemark(AnnotationUtils.getAnnotation(method.getDeclaringClass(), Remark.class));
        md.setMenu(menu);
        md.setRemark(AnnotationUtils.getAnnotation(method, Remark.class));
        md.setRequestMapping(AnnotationUtils.getAnnotation(method, RequestMapping.class));
        return md;
    }
}
