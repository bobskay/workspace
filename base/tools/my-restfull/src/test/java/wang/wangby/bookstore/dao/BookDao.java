package wang.wangby.bookstore.dao;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import wang.wangby.bookstore.config.TestConfig;
import wang.wangby.bookstore.model.Book;
import wang.wangby.restfull.annotation.RestDao;

import java.util.List;

//配置此dao所使用的工厂类,不同的工厂类访问的基地址不同
@RestDao(TestConfig.BookstoreApiDaoFactory.class)
public interface BookDao {

    //新增或修改
    @PutMapping("/book/${book.id}")
    Book add(Book book);

    //删除
    @DeleteMapping("book/${id}")
    Book delete(String id);

    //通过id查询
    @GetMapping("/book/${id}")
    Book get(String id);

    //获取全部
    @GetMapping("/book/getAll")
    List<Book> getAll();
}