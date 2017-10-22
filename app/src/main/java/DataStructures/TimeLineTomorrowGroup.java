package DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
 */

public class TimeLineTomorrowGroup extends GroupBase {

    public TimeLineTomorrowGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.timeline_tomorrow_title));
        groupType = GroupType.Tomorrow;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.after(timePoint) && taskBasePoint.before(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        this.timePoint = DateTime.BuildTimePoint(1);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(timePoint);
    }
}
