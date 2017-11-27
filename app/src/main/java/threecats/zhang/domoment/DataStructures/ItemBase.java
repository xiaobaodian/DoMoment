package threecats.zhang.domoment.DataStructures;

/**
 * Created by zhang on 2017/8/3.
 */

public class ItemBase {
    protected int ID;
    protected String title;
    protected String note;

    public void setID(int ID){
        this.ID = ID;
    }
    public int getID(){
        return ID;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setNote(String note){ this.note = note; }
    public String getNote(){ return note; }

}
