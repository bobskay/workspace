package wang.wangby.bookstore.dao;

import org.apache.ibatis.annotations.Mapper;
import wang.wangby.bookstore.model.BookInfo;
import wang.wangby.dao.BaseDao;

@Mapper
public interface BookInfoDao extends BaseDao<BookInfo> {
}
