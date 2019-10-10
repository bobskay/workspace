package wang.wangby.mserviceadmin.model.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "application")
@Data
public class ApplicationXml {
    private String name;
    private List<InstanceXml> instance;
}
