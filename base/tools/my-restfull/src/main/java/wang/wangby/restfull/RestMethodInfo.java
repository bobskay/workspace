package wang.wangby.restfull;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.utils.http.HttpRequestMethod;

import java.util.List;

@Data
public class RestMethodInfo {
    private String url;
    @Remark("方法名")
    private String name;
    private HttpRequestMethod method;
    @Remark("地址里面是否包含变量,如果包含解析url的时候需要特殊处理")
    private boolean urlContentVariable=false;
    @Remark("参数名称")
    private List<String> parameterNames;
    @Remark("是否包含body")
    private boolean hasBody=false;
}
