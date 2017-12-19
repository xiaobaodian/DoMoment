package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * 由 zhang 于 2017/8/4 创建
 */

public class TimeLineTodayGroup extends GroupBase {

    public TimeLineTodayGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_today_title));
        groupType = GroupType.toDay;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return secTimePoint == null ? taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint())
                : secTimePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void buildTimePoint(){
        this.timePoint = DateTime.BuildTimePoint(0);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

