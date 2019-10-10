package wang.wangby.utils;

import wang.wangby.test.TestBase;
import wang.wangby.test.model.BookInfo;

import javax.xml.bind.JAXBException;

public class XmlUtilTest extends TestBase {

    public static void main(String[] args) throws JAXBException {
        String xml=FileUtil.getText(XmlUtilTest.class,"bookInfo.xml");
        BookInfo bk=XmlUtils.toBean(xml, BookInfo.class);
        System.out.println(bk.getBookId()+":"+bk.getPrice()+":"+bk.getUser());
    }
}
