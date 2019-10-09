package wang.wangby.mytools.controller.vo;

import lombok.Data;

import java.util.List;

@Data
public class JavaClass {
    private List<JavaField> javaFieldList;
    private String packageName;
    private List<String> importList;
    private String name;
}
