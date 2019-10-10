package wang.wangby.mserviceadmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.api.Param;
import wang.wangby.annotation.api.Return;
import wang.wangby.annotation.web.Menu;
import wang.wangby.exception.Message;
import wang.wangby.model.dao.Pagination;
import wang.wangby.model.request.Response;
import wang.wangby.mservice.config.model.AppConfig;
import wang.wangby.mservice.config.service.AppConfigService;
import wang.wangby.page.controller.BaseController;

@RestController
@RequestMapping("mserviceConfig")
public class MserviceConfigController extends BaseController {

    @Autowired
    AppConfigService appConfigService;

    @Menu("变量配置")
    @RequestMapping("index")
    public String index(){
        return $("index");
    }

    @RequestMapping("/select")
    @Remark("查询系统配置")
    @Param("查询条件")
    @Param("起始条数,从0开始")
    @Param("返回条数")
    @Return("分页后的查询结果")
    public Response<Pagination> select(AppConfig appConfig, Integer offset, Integer limit) {
        appConfig.getExt().put("orderBy","application,profile,varLabel");
        Pagination page = appConfigService.selectPage(appConfig, offset, limit);
        return respone(page);
    }

    @RequestMapping("/prepareInsert")
    @Remark("进入新增页面")
    public String prepareInsert() {
        return $("prepareInsert");
    }


    @RequestMapping("/insert")
    @Remark("新增")
    @Param("要插入数据库的对象")
    @Return("新增成功后的数据,填充了主键")
    public Response<AppConfig> insert(AppConfig appConfig) {
        appConfigService.insert(appConfig);
        return respone(appConfig);
    }

    @Remark("进入修改页面")
    @RequestMapping("/prepareUpdate")
    @Param("主键")
    public  String prepareUpdate(Long appConfigId) {
        if (appConfigId == null) {
            throw new Message("主键不能为空");
        }
        AppConfig model = appConfigService.get(appConfigId);
        return $("prepareUpdate",model);
    }

    @Remark("通过主键更新")
    @RequestMapping("/updateById")
    public Response<AppConfig> updateById(AppConfig appConfig) {
        appConfigService.updateById(appConfig);
        return respone(appConfig);
    }

    @Remark("通过主键删除")
    @RequestMapping("/deleteById")
    @Param("要删除的图书Id")
    @Return("删除记录数")
    public Response<Integer> deleteById(Long[] appConfigId) {
        return respone(appConfigService.deleteById(appConfigId));
    }


}
