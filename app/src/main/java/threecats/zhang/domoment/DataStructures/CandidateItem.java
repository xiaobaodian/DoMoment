package threecats.zhang.domoment.DataStructures;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 由 zhang 于 2017/11/30 创建
 */

@Entity
public class CandidateItem {

    @Id
    private long id;

    private long typeID;
    private int frequencySum;
    private String title;

    public CandidateItem(String title){
        id = 0;
        typeID = 0;
        frequencySum = 0;
        this.title = title;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return this.id;
    }
    public void setTypeID(long typeID){
        this.typeID = typeID;
    }
    public long getTypeID(){
        return this.typeID;
    }
    public void setFrequencySum(int frequencySum){
        this.frequencySum = frequencySum;
    }
    public int getFrequencySum(){
        return this.frequencySum;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

}
