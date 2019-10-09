package wang.wangby.page.model;

import lombok.Data;

import java.util.List;

//为显示select准备的对象
@Data
public class SelectItem {
    private List<SelectItem> optgroup;//分组
    private String text;//显示文本
    private String key;//对应value
    private Object source;//来源对象,有些时候需要用到原始对象的属性
}
