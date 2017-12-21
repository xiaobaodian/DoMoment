package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.R;

/**
 * 由 zhang 于 2017/8/4 创建
 */

public class TimeLineNextWeekGroup extends GroupBase {

    public TimeLineNextWeekGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_nextweek_title));
        groupType = GroupType.WithinNextWeek;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void buildTimePoint(){
        int daySite = DateTimeHelper.getDayOfWeek(Calendar.getInstance());
        this.timePoint = DateTimeHelper.buildTimePoint(7-daySite+1);
    }
}
