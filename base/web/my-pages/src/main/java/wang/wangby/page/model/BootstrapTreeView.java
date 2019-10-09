package wang.wangby.page.model;

import java.util.*;
import java.util.function.Function;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
@Remark("bootstrap.treeView显示所需对象")
public class BootstrapTreeView {

    @Remark("节点唯一标识")
    private String id;
    @Remark("下级节点")
    private List<BootstrapTreeView> nodes;
    @Remark("显示文本")
    private String text;
    @Remark("url")
    private String url;
    @Remark("上级节点id")
    private String parnetId;
    @Remark("显示序号")
    private Integer index;
    @Remark("节点状态")
    //expanded 是否展开
    private Map<String,String>  state=new HashMap<>();

    public void setExpanded(boolean bool){
        state.put("expanded",bool+"");
    }

    public static BootstrapTreeView newInstance(String id, String text, String url) {
        BootstrapTreeView view = new BootstrapTreeView();
        view.id = id;
        view.text = text;
        view.url = url;
        return view;
    }

    public void addNode(String id, String text, String url) {
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        BootstrapTreeView node = newInstance(id, text, url);
        this.nodes.add(node);
    }

    public static <T> List<BootstrapTreeView> createTrees(List<T> list, Function<T, List<BootstrapTreeView>> toBootstrapTreeView) {
        Map<String, BootstrapTreeView> all = new TreeMap<>();
        //将对象转为BootstrapTreeView并放入map
        for (T t : list) {
            List<BootstrapTreeView> views = toBootstrapTreeView.apply(t);
            for(BootstrapTreeView view:views){
                if (view.index == null) {
                    view.index = 0;
                }
                all.put(view.id, view);
            }
        }
        return formate(all);
    }

    //将list组装成树,并排序
    private static List<BootstrapTreeView> formate(Map<String, BootstrapTreeView> all) {
        List<BootstrapTreeView> roots = new ArrayList();
        //按上下级组装成树,没有上级的当做跟节点
        for (BootstrapTreeView view : all.values()) {
            BootstrapTreeView parent = all.get(view.getParnetId());
            if (parent == null || parent == view) {
                roots.add(view);
                continue;
            }
            if (parent.nodes == null) {
                parent.nodes = new ArrayList<>();
            }
            parent.nodes.add(view);
        }
        //排序
        Comparator<BootstrapTreeView> comparator=(v1,v2)->{
            return v1.index - v2.index;
        };
        for (BootstrapTreeView view : all.values()) {
            if (view.getNodes() != null) {
                Collections.sort(view.getNodes(),comparator);
            }
        }
        Collections.sort(roots,comparator);
        return roots;
    }

    public static <T> List<BootstrapTreeView> createTree(List<T> list, Function<T, BootstrapTreeView> toBootstrapTreeView) {
        Map<String, BootstrapTreeView> all = new TreeMap<>();
        //将对象转为BootstrapTreeView并放入map
        for (T t : list) {
            BootstrapTreeView view = toBootstrapTreeView.apply(t);
            if (view.index == null) {
                view.index = 0;
            }
            all.put(view.id, view);
        }
        return formate(all);
    }
}
