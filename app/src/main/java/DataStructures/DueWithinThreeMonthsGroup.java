package DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ENUM.GroupType;

/**
 * Created by zhang on 2017/8/4.
 */

public class DueWithinThreeMonthsGroup extends GroupBase {

    public DueWithinThreeMonthsGroup(GroupListBase parent, String title){
        super(parent);
        super.setTitle(title);
        groupType = GroupType.WithinThreeMonths;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return taskBasePoint.before(timePoint) && taskBasePoint.after(getNextTimePint());
    }

    @Override
    public void BuildTimePoint(){
        //上月1日零时
        Calendar timePoint = Calendar.getInstance();
        timePoint.add(Calendar.MONTH,-1);
        timePoint.set(Calendar.DAY_OF_MONTH,1);
        timePoint.set(Calendar.HOUR_OF_DAY,0);
        timePoint.set(Calendar.HOUR,0);
        timePoint.set(Calendar.MINUTE,0);
        this.timePoint = timePoint;
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

