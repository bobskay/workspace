package wang.wangby.dao.file;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.dao.MyRepository;
import wang.wangby.model.dao.BaseModel;
import wang.wangby.utils.ClassUtil;
import wang.wangby.utils.threadpool.ScheduledFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class AsynRepository implements MyRepository<BaseModel> {

    private BlockingQueue<PersistenceAction> actions;//未持久化的动作
    private Map<Long, BaseModel> data=new ConcurrentHashMap<>();//所有数据
    private int retry=2;


    MyRepository repository;

    /**
     * 初始化存储类,读取所有数据
     *
     * @param repository   实际的持久化类
     * @param maxSize      最多允许保存多少条未删数据
     * @param periodSecond 刷盘间隔
     */
    public void init(MyRepository repository, int maxSize, int periodSecond) throws Exception {
        actions = new ArrayBlockingQueue(maxSize);
        this.repository = repository;
        repository.iterator(model -> {
            BaseModel bs = (BaseModel) model;
            data.put(bs.id(), bs);
            return true;
        });
        ScheduledFactory.newSchedule(this::flush, "", periodSecond, periodSecond, TimeUnit.SECONDS);
    }

    //保存数据到磁盘
    public void flush() {
        while (true) {
            PersistenceAction action = actions.poll();
            if (action == null) {
                return;
            }
            try {
                executeAction(action,1);
            } catch (Exception e) {
                log.error("持久化操作出错",e);
                return;
            }
        }
    }

    private void executeAction(PersistenceAction action,int no) throws InterruptedException {
        if(no>retry){
            throw new RuntimeException("执行"+no+"次都出错,终止本次持久化动作");
        }
        try {
            action.execute();
        } catch (Exception e) {
            log.error("第{}此执行持久化动作出错",no,e);
            long sleep= (long) (Math.pow(no,2)*1000);
            Thread.sleep(sleep);
            executeAction(action,no+1);
        }
    }

    @Override
    public int insert(BaseModel model) throws Exception {
        Long id=model.id();
        if(id==null){
            throw new RuntimeException("主键不能为空,model="+model);
        }
        BaseModel db=data.putIfAbsent(model.id(), model);
        if(db!=null){
            throw new RuntimeException("数据已经存在,model="+db);
        }
        actions.add(() -> repository.insert(model));
        return 1;
    }

    @Override
    //保存被删除数据的id,将数据从temporary和all中移除
    @SneakyThrows
    public int delete(Class<BaseModel> clazz, Long id) {
        data.remove(id);
        actions.add(() -> {
            repository.delete(clazz, id);
        });
        return 1;
    }

    @Override
    public int update(BaseModel model) {
        data.put(model.id(), model);
        actions.add(() -> {
            repository.update(model);
        });
        return 1;
    }

    @Override
    public void iterator(Function<BaseModel, Boolean> visitor) {
        for (BaseModel bs : data.values()) {
            if (!visitor.apply(bs)) {
                return;
            }
        }
    }
    @Override
    public BaseModel get(Class<BaseModel> clazz, Long id) {
        return data.get(id);
    }

    public List getAll(Class clazz){
        return new ArrayList(data.values());
    }
}
