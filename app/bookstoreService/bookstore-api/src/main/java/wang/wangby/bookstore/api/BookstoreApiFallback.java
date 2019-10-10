package wang.wangby.bookstore.api;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wang.wangby.bookstore.model.BookInfo;

@Component
@Slf4j
public class BookstoreApiFallback  implements FallbackFactory<BookstoreApi> {
    @Override
    public BookstoreApi create(Throwable throwable) {
        return new BookstoreApi() {
            @Override
            public BookInfo getBook(Long id) {
                BookInfo bookInfo=new BookInfo();
                bookInfo.setBookName("feign异常回调:"+throwable.getMessage());
                log.error(throwable.getMessage(),throwable);
                return bookInfo;
            }

            @Override
            public String test(Long millis) throws InterruptedException {
                return "feign异常";
            }
        };
    }
}
