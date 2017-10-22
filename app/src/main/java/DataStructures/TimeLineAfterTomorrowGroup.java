package DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
 */

public class TimeLineAfterTomorrowGroup extends GroupBase {

    public TimeLineAfterTomorrowGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_aftertomorrow_title));
        groupType = GroupType.AfterTomorrow;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        timePoint = DateTime.BuildTimePoint(2);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(timePoint);
    }
}
