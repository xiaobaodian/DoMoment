package threecats.zhang.domoment.EventClass;

/**
 * 由 zhang 于 2017/12/22 创建
 */

public class InitEvent {
    private int type;
    public InitEvent(int type){
        this.type = type;
    }
    public int getType(){
        return type;
    }
}
