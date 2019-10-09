package wang.wangby.bookstore.model;

import lombok.Data;

import java.util.Date;

@Data
public class Book {
    private Long bookId;
    private String bookName;
    private Date publish;
}

