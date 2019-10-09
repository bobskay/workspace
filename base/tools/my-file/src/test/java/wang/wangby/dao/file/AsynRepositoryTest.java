package wang.wangby.dao.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.test.model.BookInfo;
import wang.wangby.utils.IdWorker;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AsynRepositoryTest extends TestBase {

    @Test
    public void iterator() throws Exception {
        DataSerializer dataSerializer=new DataSerializer();
        FileDao fileDao=new FileDao(dataSerializer,"/opt/data/filedao");
        AsynRepository repository=new AsynRepository();
        repository.init(fileDao,100,60);
        AtomicInteger integer=new AtomicInteger(0);
        perSecond(()->{
            BookInfo bk=new BookInfo();
            bk.setBookId(IdWorker.nextLong());
            bk.setBookName("book:"+integer.incrementAndGet());
            repository.insert(bk);

            log.debug("-------------------------------");
            List list= repository.getAll(BookInfo.class);
            List disk= fileDao.getAll(BookInfo.class);
            log.debug("内存个数={},磁盘文件数={}:",list.size(),disk.size());

            if(integer.get()==5){
                repository.flush();
            }
            if(integer.get()>10){
                return false;
            }
            return true;
        },60);

    }
}