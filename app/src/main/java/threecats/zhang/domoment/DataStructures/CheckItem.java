package threecats.zhang.domoment.DataStructures;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 由 zhang 于 2017/11/30 创建
 */

@Entity
public class CheckItem {

    @Id
    private long id;

    private long taskID;
    private boolean Checked;
    private String title;

    public CheckItem(String title){
        id = 0;
        taskID = 0;
        Checked = false;
        this.title = title;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return this.id;
    }
    public void setTaskID(long taskID){
        this.taskID = taskID;
    }
    public long getTaskID(){
        return this.taskID;
    }
    public void setChecked(boolean checked){
        this.Checked = checked;
    }
    public boolean getChecked(){
        return this.Checked;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

}
