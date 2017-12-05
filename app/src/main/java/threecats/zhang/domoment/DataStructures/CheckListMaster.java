package threecats.zhang.domoment.DataStructures;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import threecats.zhang.domoment.App;

/**
 * 由 zhang 于 2017/11/30 创建
 */

public class CheckListMaster {

    private Box<CheckItem> checkListBox;

    public CheckListMaster(){
        BoxStore boxStore = App.getBoxStore();
        checkListBox = boxStore.boxFor(CheckItem.class);
    }


    public long put(CheckItem item){
        return checkListBox.put(item);
    }

    public CheckItem get(long id){
        return checkListBox.get(id);
    }

    public void remove(CheckItem item){
        checkListBox.remove(item);
    }
}
