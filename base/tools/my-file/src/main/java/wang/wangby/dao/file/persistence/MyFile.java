package wang.wangby.dao.file.persistence;

import java.io.IOException;
import java.util.function.Function;

public interface MyFile {

    //通过主键获取数据
    byte[] getData(String id) throws IOException;

    /**
     * 遍历文件*
     *
     * @param reader<获得的数据,是否继续>
     */
    void iterator(Function<byte[], Boolean> reader);

    //增
    <T> boolean insert(byte[] bs, String id) throws IOException;

    //删
    void delete(String id) throws IOException;

    //改
    void update(byte[] bs, String id) throws IOException;

}
