package wang.wangby.bookstore.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.api.Param;
import wang.wangby.annotation.api.Return;
import wang.wangby.annotation.web.Menu;
import wang.wangby.bookstore.api.BookstoreApi;
import wang.wangby.bookstore.model.BookInfo;
import wang.wangby.bookstore.service.BookInfoService;
import wang.wangby.exception.Message;
import wang.wangby.model.dao.Pagination;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;


@RestController
@RequestMapping("/bookInfo")
public class BookInfoController extends BaseController implements BookstoreApi {

    @Value("${eureka.instance.instance-id}")
    private String instanceId;

    @Autowired
    BookInfoService bookInfoService;

    @RequestMapping("/index")
    @Remark("图书管理首页")
    @Menu("图书管理")
    public String index() {
        return $("index");
    }

    @RequestMapping("/prepareInsert")
    @Remark("进入新增页面")
    public String prepareInsert() {
        return $("prepareInsert");
    }


   @RequestMapping("/insert")
   @Remark("新增图书")
   @Param("要插入数据库的对象")
   @Return("新增成功后的数据,填充了主键")
   public Response<BookInfo> insert(BookInfo bookInfo) {
       bookInfoService.insert(bookInfo);
       return respone(bookInfo);
   }

    @RequestMapping("/select")
    @Remark("查询图书")
    @Param("查询条件")
    @Param("起始条数,从0开始")
    @Param("返回条数")
    @Return("分页后的查询结果")
    public Response<Pagination> select(BookInfo bookInfo, Integer offset, Integer limit) {
        Pagination page = bookInfoService.selectPage(bookInfo, offset, limit);
        return respone(page);
    }

    @Remark("通过主键删除图书")
    @RequestMapping("/deleteById")
    @Param("要删除的图书Id")
    @Return("删除记录数")
    public Response<Integer> deleteById(Long[] bookId) {
        return respone(bookInfoService.deleteById(bookId));
    }

    @Remark("通过主键删除图书")
    @RequestMapping("/updateById")
    public Response<BookInfo> updateById(BookInfo book) {
        bookInfoService.updateById(book);
        return respone(book);
    }


    @Remark("进入修改页面")
    @RequestMapping("/prepareUpdate")
    @Param("主键")
    public  String prepareUpdate(Long bookId) {
        if (bookId == null) {
            throw new Message("主键不能为空");
        }
        BookInfo model = bookInfoService.get(bookId);
        return $("prepareUpdate",model);
    }

    @RequestMapping("/getBook")
    @HystrixCommand(fallbackMethod = "getBookFail")
    public BookInfo getBook(Long id) {
        if(id==1){
            throw new RuntimeException("未知异常");
        }
        BookInfo bk=new BookInfo();
        bk.setBookId(id);
        bk.setBookName("调用成功,当前服务器:"+instanceId);
        return bk;
    }

    @RequestMapping("/test")
    @HystrixCommand
    public String test() {
        return instanceId;
    }

    public BookInfo getBookFail(Long id){
        BookInfo bk=new BookInfo();
        bk.setBookName("Hystrix错误回调");
        return bk;
    }
}
