package wang.wangby.zk.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.exception.Message;
import wang.wangby.utils.cache.CacheMap;
import wang.wangby.zk.model.ZooNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ZooKeeperService {
    private CacheMap<String,ZooKeeper> zooKeeperMap=new CacheMap<>();

    @Autowired
      ZkInfoService zkInfoService;

    public void visit(String address,NodeVistor vistor,String root) throws KeeperException, InterruptedException {
        ZooKeeper zk = getZk(address);
        if(vistor.visit(root,root,root)){
            iterator(zk,vistor,root);
        }
    }

    //获取某个节点下的所有节点
    public List<ZooNode> getNodes(String address,String root) throws KeeperException, InterruptedException {
        List zooNodes=new ArrayList();
        ZooKeeperService.NodeVistor vistor = (String parent, String path,String name) -> {
            ZooNode zNode = new ZooNode();
            zNode.setName(name);
            zNode.setParentPath(parent);
            zNode.setPath(path);
            zooNodes.add(zNode);
            return true;
        };
        visit(address,vistor,root);
        return zooNodes;
    }

    //遍历所有节点
    private void iterator(ZooKeeper zk,NodeVistor vistor,String parent) throws KeeperException, InterruptedException {
        List<String> nodes=zk.getChildren(parent,false);
        for(String node:nodes){
            log.debug("访问节点:"+node);
            String path=parent;
            if(parent.endsWith("/")){
                path=parent+node;
            }else{
                path=parent+"/"+node;
            }
            if(vistor.visit(parent,path,node)){
                iterator(zk,vistor,path);
            }
        }
    }

    public ZooNode getNode(String zkAddress,String path) throws KeeperException, InterruptedException {
        ZooKeeper zk=getZk(zkAddress);

        ZooNode node=new ZooNode();
        node.setPath(path);
        Stat stat=zk.exists(path,false);
        byte[] data=zk.getData(path,false,stat);
        List<ACL> acls=zk.getACL(path,stat);
        node.setStat(stat);
        node.setAcls(acls);
        node.setDataString(new String(data, StandardCharsets.UTF_8));
        node.setZkAddress(zkAddress);
        return node;
    }

    public void delete(String zkAddress, String path,Integer version) throws KeeperException, InterruptedException {
        ZooKeeper zk=getZk(zkAddress);
        zk.delete(path,version);
    }

    public void update(String zkAddress,String path,String data,int version) throws KeeperException, InterruptedException {
        ZooKeeper zk=getZk(zkAddress);
        zk.setData(path,data.getBytes(StandardCharsets.UTF_8),version);
    }

    public void deleteBatch(String zkAddress, String root, Integer version) throws KeeperException, InterruptedException {
        ZooKeeper zk=getZk(zkAddress);
        while (true){
            List<ZooNode> nodes=getNodes(zkAddress,root);
            for(ZooNode n:nodes){
                Stat st=zk.exists(n.getPath(),false);
                if(st.getNumChildren()==0){
                    zk.delete(n.getPath(),st.getVersion());
                }
            }
            Stat st=zk.exists(root,false);
            if(st!=null&&st.getNumChildren()==0){
                zk.delete(root,st.getVersion());
                return;
            }
        }
    }

    public  interface NodeVistor{
         boolean visit(String parent,String path,String name) throws KeeperException, InterruptedException;
    }

    public String insert(String zkAddress,String parent,String name,String data) throws KeeperException, InterruptedException {
        name=name.trim();
        ZooKeeper zk=getZk(zkAddress);
        if(!parent.endsWith("/")){
            parent=parent+"/";
        }
        String path=parent+name;
        String  actualPath =zk.create(path,data.getBytes(StandardCharsets.UTF_8),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
         log.debug("新增节点:"+actualPath);
         return actualPath;
    }

    private ZooKeeper getZk(String address){
        ZooKeeper zooKeeper= zooKeeperMap.get(address,()->{
            ZooKeeper zk=null;
            try{
                 zk = new ZooKeeper(address,5000, new Watcher() {
                    public void process(WatchedEvent event) {
                        log.debug(event.getPath()+"触发了" + event.getType() + "事件！");
                    }
                });
                zk.exists("/",false);
                return zk;
            }catch (KeeperException | IOException | InterruptedException ex){
                if(zk!=null){
                    try {
                        zk.close();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(ex instanceof KeeperException){
                    throw new Message("连接失败:"+address+":"+((KeeperException) ex).code());
                }else{
                    throw new Message("连接失败:"+address+":"+((Exception) ex).getMessage());
                }

            }
        });
        if(!zooKeeper.getState().isAlive()){
            zooKeeperMap.remove(address);
            return getZk(address);
        }
        return zooKeeper;
    }



}
