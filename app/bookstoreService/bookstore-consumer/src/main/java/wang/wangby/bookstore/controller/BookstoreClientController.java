package wang.wangby.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.bookstore.api.BookstoreApi;
import wang.wangby.bookstore.model.BookInfo;
import wang.wangby.model.request.Response;
import wang.wangby.page.controller.BaseController;
import wang.wangby.utils.LogUtil;

@RestController
public class BookstoreClientController extends BaseController {

    @Autowired
    BookstoreApi bookstoreApi;

    @RequestMapping({"/","/index"})
    public Response<BookInfo> test(Long id,Integer count) {
        //在后台多次调用接口,测试用
        if(count!=null){
            new Thread(){
                public void run(){
                    for(int i=0;i<count;i++){
                        try {
                            if (id == null) {
                                bookstoreApi.getBook(1L);
                            }else{
                                bookstoreApi.getBook(id);
                            }
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        try{
            if (id == null) {
                return respone(bookstoreApi.getBook(1L));
            }else{
                return respone(bookstoreApi.getBook(id));
            }
        }catch (Exception ex){
            String error= LogUtil.getExceptionText(ex);
            return Response.fail(error);
        }
    }
}
