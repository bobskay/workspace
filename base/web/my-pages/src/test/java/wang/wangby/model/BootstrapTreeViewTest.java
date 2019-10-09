package wang.wangby.model;

import org.junit.Test;
import wang.wangby.page.model.BootstrapTreeView;
import wang.wangby.test.TestBase;

import java.util.ArrayList;
import java.util.List;

public class BootstrapTreeViewTest extends TestBase {

    @Test
    public void createTree() {
        List<Integer> list=new ArrayList();
        for(int i=0;i<100;i++){
            list.add(100-i);
        }

        List tree= BootstrapTreeView.createTree(list, i->{
           BootstrapTreeView view=new BootstrapTreeView();
           view.setId(i+"");
           view.setText(i+"");
           view.setUrl("url"+i);
           view.setIndex(i);
           view.setParnetId(i/10+"");
           return view;
        });

        assertNotEmpty(tree);
    }
}