package DataStructures;

import android.support.annotation.Nullable;

import java.util.Calendar;

import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
 */

public class TimeLineThisMonthGroup extends GroupBase {

    public TimeLineThisMonthGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_thismonth_title));
        groupType = GroupType.WithinThisMonth;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        //int daySite = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //int monthDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        //return DateTime.BuildTimePoint(monthDays - daySite);
        int daySite = DateTime.GetDayOfWeek(Calendar.getInstance());
        this.timePoint = DateTime.BuildTimePoint(7-daySite+8);
    }
}
