package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;

/**
 * 由 zhang 于 2017/8/4 创建
 */

public class DueLongTimeAgoGroup extends GroupBase {

    public DueLongTimeAgoGroup(GroupListBase parent, String title){
        super(parent);
        super.setTitle(title);
        groupType = GroupType.LongTimeAgo;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {;
        return taskBasePoint.before(timePoint);
    }

    @Override
    public void buildTimePoint(){
        Calendar timePoint = Calendar.getInstance();
        timePoint.add(Calendar.MONTH,-6);
        timePoint.set(Calendar.DAY_OF_MONTH,1);
        timePoint.set(Calendar.HOUR_OF_DAY,0);
        timePoint.set(Calendar.HOUR,0);
        timePoint.set(Calendar.MINUTE,0);
        this.timePoint = timePoint;
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

