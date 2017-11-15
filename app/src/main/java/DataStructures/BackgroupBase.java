package DataStructures;

/**
 * Created by zhang on 2017/11/15.
 */

public class BackgroupBase {
    private int ID;
    private String title;

    public BackgroupBase(int ID, String title){
        this.ID = ID;
        this.title = title;
    }

    public int getID(){
        return ID;
    }

    public String getTitle(){
        return title;
    }
}
