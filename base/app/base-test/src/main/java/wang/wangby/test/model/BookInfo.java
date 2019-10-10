package wang.wangby.test.model;

import lombok.Data;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.model.dao.BaseModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@XmlRootElement(name = "Book")
public class BookInfo extends BaseModel {

	@Id
	private Long bookId;
	private String bookName;
	private Date publication;
	private Integer price;
	private Date createTime;
	private Boolean valid;
	private User user;

	@Data
	public static class User{
		private String userId;
	}
}
