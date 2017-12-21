package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.Helper.DateTimeHelper;

/**
 * 由 zhang 于 2017/8/4 创建
 */

public class DueLastWeekGroup extends GroupBase {

    public DueLastWeekGroup(GroupListBase parent, String title){
        super(parent);
        super.setTitle(title);
        groupType = GroupType.WithinLastWeek;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.before(timePoint) && taskBasePoint.after(getNextTimePint());
    }

    @Override
    public void buildTimePoint(){
        int daySite = DateTimeHelper.getDayOfWeek(Calendar.getInstance());
        this.timePoint = DateTimeHelper.buildTimePoint(1-daySite);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

