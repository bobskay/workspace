package wang.wangby.page.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import wang.wangby.annotation.Remark;
import wang.wangby.page.controller.utils.ControllerMenuParser;
import wang.wangby.page.model.BootstrapTreeView;
import wang.wangby.model.request.Response;
import wang.wangby.utils.EnumUtil;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.template.TemplateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseController implements InitializingBean {

	@Autowired
	private Environment env;
	@Autowired
	private TemplateUtil templateUtil;
	@Autowired
	protected JsonUtil jsonUtil;
	@Autowired
	private ApplicationContext applicationContext;

	//启动的时候设置
	@Remark("当前controller的路径")
	private String path;
	@Remark("当前controller的context")
	private  Map context;

	// 如果不是以/开头,就认为是相对于controller的路径
	public String $(String path) {
		return $(path, null);
	}

	public String $(String path, Object model) {
		if (!path.startsWith("/")) {
			path = this.getPath() + "/" + path;
		}
		return templateUtil.parseTemplate(path + ".html", defaultRoot(model));
	}


	public String json(Object obj) {
		return jsonUtil.toString(obj);
	}

	public Response respone(Object object) {
		if(log.isDebugEnabled()){
			String json = jsonUtil.toString(Response.success(object));
			if (json.length() > 2000) {
				json=json.substring(0, 2000)+"...";

			}
			log.debug("返回json:"+json);
		}
		return Response.success(object);
	}

	private Map defaultRoot(Object obj) {
		String path = getPath();

		Map map = new HashMap(context);
		if (obj == null) {
			return map;
		}
		if (obj instanceof Map) {
			map.putAll((Map) obj);
		}
		map.put(TemplateUtil.defaultName(obj), obj);
		return map;
	}

	public String getPath() {
		return path;
	}

	// 获得配置值,不支持空格
	private String getValue(String key) {
		key = key.replaceAll("\\s+", "");
		if (!key.startsWith("$")) {
			return key;
		}

		key = key.substring(2, key.length() - 1);
		String[] keys = key.trim().split(":");
		if (keys.length == 2) {
			return env.getProperty(keys[0], keys[1]);
		}
		return env.getProperty(key);
	}

	public Response fail(String error) {
		String json = jsonUtil.toString(Response.fail(error));
		log.debug("返回json:" + json);
		return Response.fail(error);
	}

	//顺序,显示菜单树的时候用
	public int order(){
		return 0;
	}

	List<BootstrapTreeView> menus;
	//通过controller方法上的menu标签,创建显示用的菜单
	public List<BootstrapTreeView> getDefaultMenu() {
		if(menus!=null){
			return menus;
		}
		Map map=applicationContext.getBeansOfType(BaseController.class);
		menus= ControllerMenuParser.createMenus(map.values());
		return menus;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化path属性
		path="";
		RequestMapping mapping = AnnotationUtils.getAnnotation(this.getClass(), RequestMapping.class);
		if (mapping != null && mapping.value() != null && mapping.value().length > 0) {
			path = mapping.value()[0];
			path = getValue(path);

		}
		log.debug("新增controller:" + path);
		// 页面需要的路径不应该以/结尾
		if (path.endsWith("/")) {
			path.substring(0, path.length() - 1);
		}

		context=new HashMap();
		context.put("path", path);
		context.put("html",new HtmlUtil(jsonUtil));
		for(Class clz:enumClasses()){
			String name=StringUtil.firstLower(clz.getSimpleName())+"List";
			log.trace(this.getClass().getName()+"添加枚举:"+clz.getName());
			context.put(name, EnumUtil.getEnums(clz));
		}
	}

	//当前controller用到的枚举
	private List<Class> enumClasses(){
		if(applicationContext==null){
			log.debug("当前无"+applicationContext);
			return new ArrayList<>();
		}
		if(applicationContext.containsBean("systemEnums")){
			return (List)applicationContext.getBean("systemEnums");
		}
		return new ArrayList<>();
	}
}
