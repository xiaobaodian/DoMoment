package threecats.zhang.domoment.DataStructures;

import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/4.
 */

public class AllOverdueGroup extends GroupBase {

    public AllOverdueGroup(GroupListBase parent){
        super(parent);
        super.setTitle(context.getString(R.string.overdue_all_title));
        groupType = GroupType.AllOverdue;
    }

    @Override
    protected boolean isGroupMember(Calendar taskBasePoint, @Nullable Calendar secTimePoint) {
        return secTimePoint.before(getNextTimePint());
    }

    @Override
    public void buildTimePoint(){
        this.timePoint = DateTimeHelper.BuildTimePoint(0);
    }

    public String getDateTitle(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-d日 EEE");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}

