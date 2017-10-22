package DataStructures;

import android.support.annotation.Nullable;

import java.util.Calendar;

import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
 */

public class TimeLineThisWeekGroup extends GroupBase {

    public TimeLineThisWeekGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_thisweek_title));
        groupType = GroupType.WithinThisWeek;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        this.timePoint = DateTime.BuildTimePoint(3);
    }
}
