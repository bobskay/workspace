package wang.wangby.mytools.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import wang.wangby.annotation.web.Menu;
import wang.wangby.page.controller.BaseController;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.mytools.service.websocket.MyWebSocketHandler;
import wang.wangby.mytools.service.websocket.SocketClient;
import wang.wangby.mytools.service.websocket.SocketTask;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("filePush")
@Slf4j
public class FilePushController  extends BaseController {
    @RequestMapping("/index")
    @Menu("读取文件")
    public String index(String fileName) {
        if(StringUtil.isNotEmpty(fileName)){
            Map map=new HashMap<>();
            map.put("fileName",fileName);
            return $("index",map);
        }
        return $("index");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        ScheduledFactory.newSchedule(this::iteratorClient,"FilePushController",1,1, TimeUnit.SECONDS);

    }

    public void iteratorClient(){
       MyWebSocketHandler.iterator(this::sendMessage);
    }

    public void sendMessage(SocketClient client){
        while(true){
            TextMessage msg= client.getReceive().poll();
            if(msg==null){
                return;
            }
            String payload=msg.getPayload();
            String pix="file:";
            if(payload.startsWith(pix)){
                client.stop();
                String fileName=payload.substring(pix.length()).trim();
                log.debug("收到读取文件请求:"+payload+",客户端:"+client.getWebSocketSession().getId());
                FileReadTask task=new FileReadTask(fileName,client);
                client.addTask(task);
                continue;
            }
            if(payload.startsWith("stop:")){
                client.stop();
                continue;
            }
            log.debug("无效消息:"+payload);
        }
    }

    public static class FileReadTask implements SocketTask {
        private String fileName;
        private long lastModify=0L;
        private long lastPointer=0;
        int once=1024*10;
        private SocketClient client;
        public FileReadTask(String fileName,SocketClient client){
            this.client=client;
            this.fileName=fileName;
            this.lastPointer=new File(fileName).length()-once;
            Runnable run=()->{
                try{
                    read();
                }catch (Exception ex){
                    log.error(ex.getMessage(),ex);
                    client.send("读取文件出错:"+ex.getMessage());
                }
            };
            String id=client.getWebSocketSession().getId()+"fileReadController";
            ScheduledFactory.newSchedule(run,id,1,1,TimeUnit.SECONDS);
        }

        public void read() throws Exception {
            File file=new File(fileName);
            if(file.lastModified()<lastModify){
                return;
            }
            try(RandomAccessFile rf=new RandomAccessFile(file,"r")){
                rf.seek(lastPointer);
                byte[] bytes=new byte[once];
                rf.read(bytes);
                long read=rf.getFilePointer()-lastPointer;
                if(read<once){
                    byte[] newBytes=new byte[(int)read];
                    System.arraycopy(bytes,0,newBytes,0,newBytes.length);
                    bytes=newBytes;
                }
                String str=new String(bytes,"UTF-8");
                if(!StringUtil.isEmpty(str)){
                    client.send(str);
                }
                lastPointer=rf.getFilePointer();
            }
        }

        public void send(String msg){
            client.send(msg);
        }

        public void stop(){
            String id=client.getWebSocketSession().getId()+"fileReadController";
            ScheduledFactory.shutdown(id);
        }
    }
}
