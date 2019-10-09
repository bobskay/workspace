package wang.wangby.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.wangby.bookstore.dao.BookInfoDao;
import wang.wangby.bookstore.model.BookInfo;
import wang.wangby.model.dao.Pagination;
import wang.wangby.service.BaseService;

@Service
public class BookInfoService extends BaseService {

    @Autowired
    private BookInfoDao bookInfoDao;


    //生成主键并入库
    @Transactional
    public BookInfo insert(BookInfo bookInfo ) {

        bookInfo.setBookId(newId());
        bookInfoDao.insert(bookInfo );
        return bookInfo ;
    }

    // 通过主键批量删除
    public int deleteById(Long[] bookId) {
        if (bookId.length == 0) {
            return 0;
        }
        return bookInfoDao.deleteById(bookId);
    }

    // 通过主键更新，
    public BookInfo updateById(BookInfo book) {
        bookInfoDao.updateById(book);
        return book;
    }

   /**
    * 分页查询
    * @param  bookInfo 查询条件
    * @param  offset 起始位置偏移量
    * @param  limit 返回条数
    * @return  查询结果
    * */
   public Pagination selectPage(BookInfo bookInfo, Integer offset, Integer limit) {
       return super.selectPage(bookInfo, bookInfoDao, offset, limit);
   }

    public BookInfo get(Long bookId) {
       return bookInfoDao.get(bookId);
    }
}