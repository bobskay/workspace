package wang.wangby.page.config;

import lombok.Data;
import wang.wangby.annotation.Remark;

import java.util.Set;

@Data
public class IndexPageProperties {
    @Remark("首页要额外导入的js文件")
    private Set<String> js;
    @Remark("标题")
    private String title;
}
