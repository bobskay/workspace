package wang.wangby.test.model;

import java.util.Date;

import lombok.Data;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.model.dao.BaseModel;

@Data
public class BookInfo extends BaseModel {

	@Id
	private Long bookId;
	private String bookName;
	private Date publication;
	private Integer price;
	private Date createTime;
	private Boolean valid;
	
	
}
