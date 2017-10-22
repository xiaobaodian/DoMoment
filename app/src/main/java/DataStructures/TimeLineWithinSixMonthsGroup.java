package DataStructures;

import android.support.annotation.Nullable;

import java.util.Calendar;

import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
 */

public class TimeLineWithinSixMonthsGroup extends GroupBase {

    public TimeLineWithinSixMonthsGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_withinsixmonths_title));
        groupType = GroupType.WithinSixMonths;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        Calendar timePoint = Calendar.getInstance();
        timePoint.add(Calendar.MONTH,4);
        timePoint.set(Calendar.DAY_OF_MONTH,1);
        timePoint.set(Calendar.HOUR_OF_DAY,0);
        timePoint.set(Calendar.MINUTE,0);
        timePoint.set(Calendar.SECOND,0);
        this.timePoint = timePoint;
    }
}
