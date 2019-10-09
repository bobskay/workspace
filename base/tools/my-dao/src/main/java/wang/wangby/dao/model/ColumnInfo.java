package wang.wangby.dao.model;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
@Remark("数据库列信息")
public class ColumnInfo {

	@Remark("表名")
	private String tableName;
	@Remark("列名")
	private String  columnName;
	@Remark("是否允许为空(YES/NO)")
	private String nullable;
	@Remark("数据库类型")
	private String dataType;
	@Remark("允许的最大长度")
	private Long maxLength;
	@Remark("列备注")
	private String columnComment;

}
