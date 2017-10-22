package DataStructures;

import ENUM.ItemType;

/**
 * Created by zhang on 2017/8/7.
 */

public class ListItemBase extends ItemBase {
    protected ItemType itemType;
    protected boolean checked = false;

    public void setItemType(ItemType type){
        this.itemType = type;
    }
    public ItemType getItemType(){
        return itemType;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }
    public boolean getChecked(){
        return checked;
    }
}
