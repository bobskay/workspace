package wang.wangby.bookstore.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.wangby.bookstore.model.BookInfo;

@FeignClient(value="BOOKSTORE-PROVIDER",fallbackFactory = BookstoreApiFallback.class)
public interface BookstoreApi {
    @RequestMapping("/bookInfo/getBook")
    BookInfo getBook(@RequestParam("id") Long id);

    @RequestMapping("/bookInfo/test")
    String test(@RequestParam("millis") Long millis) throws InterruptedException;
}
