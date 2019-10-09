package wang.wangby.bookstore;

import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import wang.wangby.bookstore.config.TestConfig;
import wang.wangby.bookstore.dao.BookDao;
import wang.wangby.bookstore.model.Book;
import wang.wangby.test.assertutils.MyAssert;


public class RestFullTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        BookDao dao=context.getBean(BookDao.class);
        Book book=dao.get("123");
        MyAssert.stringEqual(book.getBookName(),"图书123",new Exception("判断图书名称是否正确"));
        AbstractAutowireCapableBeanFactory s;
    }
}
