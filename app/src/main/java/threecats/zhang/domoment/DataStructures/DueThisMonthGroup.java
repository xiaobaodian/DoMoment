package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;

/**
 * Created by zhang on 2017/8/4.
 */

public class DueThisMonthGroup extends GroupBase {

    public DueThisMonthGroup(GroupListBase parent, String title){
        super(parent);
        super.setTitle(title);
        groupType = GroupType.WithinThisMonth;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.before(timePoint) && taskBasePoint.after(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        int daySite = DateTime.GetDayOfWeek(Calendar.getInstance());
        //上周一的零时
        this.timePoint = DateTime.BuildTimePoint(1-daySite-7);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

