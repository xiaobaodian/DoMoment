package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
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
    public void BuildTimePoint(){
        int daySite = DateTime.GetDayOfWeek(Calendar.getInstance());
        this.timePoint = DateTime.BuildTimePoint(7-daySite+1);
    }
}
