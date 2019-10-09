package wang.wangby.autoconfigure.dao;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "my.dao")
@Data
public class DaoProperties {

	private String mapperXml = "codetemplate/mapper.xml";

	//生成mapping文件扫描的跟路径路径
	private String daoBasePackage = "wang.wangby";

	private String outPutDir = "/opt/temp";

	private Integer machineNo = 0;

	public Integer getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(Integer machineNo) {
		this.machineNo = machineNo;
	}

}
