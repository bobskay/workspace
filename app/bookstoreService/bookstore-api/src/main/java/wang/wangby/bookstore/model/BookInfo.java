package wang.wangby.bookstore.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.*;
import wang.wangby.model.dao.BaseModel;

import java.util.Date;

@Data
@Table("t_bookInfo")
@Remark("图书信息表")
public class BookInfo extends BaseModel {
    @Id
    @NotNull
    @Length(20)
    @Remark("主键")
    private Long bookId;

    @NotNull
    @Length(64)
    @Remark("书名")
    private String bookName;

    @DateOnly
    @Remark("发行日期")
    private Date publication;

    @Length(11)
    @Remark("售价")
    private Integer price;

    @Length(32)
    @Remark("ISBN")
    private String isbn;

    @Length(4)
    @Remark("有效标识,0无效,1有效")
    private Integer valid;

    @Length(11)
    @Remark("库存")
    private Integer remain;

    @Remark("创建时间")
    private Date createTime;

}
