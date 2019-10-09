package wang.wangby.dao.file;

import org.junit.Before;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.test.model.BookInfo;
import wang.wangby.utils.DateTime;
import wang.wangby.utils.IdWorker;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNull;

public class FileDaoTest extends TestBase {
    FileDao<BookInfo> fileDao;
    @Before
    public void init() throws IOException {
        DataSerializer dataSerializer=new DataSerializer();
        fileDao=new FileDao(dataSerializer,"/opt/data/filedao");
    }

    @Test
    public void insert() throws IOException {
        BookInfo bookInfo=new BookInfo();
        bookInfo.setBookId(IdWorker.nextLong());
        bookInfo.setBookName("insert book");
        bookInfo.setCreateTime(new DateTime("2018-01-01").toDate());
        bookInfo.setPrice(123);
        bookInfo.setValid(true);

        fileDao.insert(bookInfo);
        BookInfo db=fileDao.get(BookInfo.class,bookInfo.getBookId());
        assertNotEmpty(db);
    }

    @Test
    public void delete() throws IOException {
      long id= IdWorker.nextLong();
        BookInfo bookInfo=new BookInfo();
        bookInfo.setBookId(id);
        fileDao.insert(bookInfo);

        BookInfo db= (BookInfo) fileDao.get(BookInfo.class,id);
        stringEqual(bookInfo+"",db+"");

        fileDao.delete(BookInfo.class, id);
        BookInfo deleted=fileDao.get(BookInfo.class,bookInfo.getBookId());
        assertNull(deleted);

    }

    @Test
    public void update() throws IOException {
        Long id=IdWorker.nextLong();
        BookInfo bookInfo=new BookInfo();
        bookInfo.setBookId(id);
        bookInfo.setBookName("myBook");
        fileDao.insert(bookInfo);

        bookInfo.setBookName("updated");
        fileDao.update(bookInfo);
        BookInfo db=fileDao.get(BookInfo.class,id);
        assertNotEmpty(db);
    }

    @Test
    public void get() throws IOException {
        insert();
    }


    @Test
    public void getAll() throws Exception {
        List<BookInfo> list=fileDao.getAll(BookInfo.class);
        assertNotEmpty(list);
    }

}