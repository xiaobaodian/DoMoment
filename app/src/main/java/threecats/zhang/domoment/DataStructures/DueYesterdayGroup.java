package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.Helper.DateTimeHelper;

/**
 * 由 zhang 于 2017/8/4 创建
 */

public class DueYesterdayGroup extends GroupBase {

    public DueYesterdayGroup(GroupListBase parent, String title){
        super(parent);
        super.setTitle(title);
        groupType = GroupType.Yesterday;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.before(timePoint) && taskBasePoint.after(getNextTimePint());
    }

    @Override
    public void buildTimePoint(){
        this.timePoint = DateTimeHelper.buildTimePoint(0);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

