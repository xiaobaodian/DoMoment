package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.ENUM.TaskBasePoint;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/14.
 */

public class NoDateGroupList extends GroupListBase {

    public NoDateGroupList(CategoryBase parent){
        super();
        this.parent = parent;
        this.selfType = GroupListType.Nodate;
        this.taskBasePoint = TaskBasePoint.CreateDate;
        this.timeSeries = TimeSeries.Afterward;
        setTitle(DoMoment.getContext().getString(R.string.grouplist_nodate_title));
        BuildGroups();
    }

    @Override
    public boolean InGroupList(Task task){
        if (task.IsComplete()) return false;
        return task.IsNoDate();
    }

    @Override
    public void BuildTimePoint(){
        timePoint = null;
    }

    @Override
    protected void BuildGroups(){

        //必须按顺序加入建立正确的链表，后面组的区间判断是依靠获取前一组的标记日完成的
        AddGroup(new DueTodayGroup(this, DoMoment.getContext().getString(R.string.create_today_title)));
        AddGroup(new DueYesterdayGroup(this, DoMoment.getContext().getString(R.string.create_yesterday_title)));
        AddGroup(new DueBeforeYesterdayGroup(this, DoMoment.getContext().getString(R.string.create_beforeyesterday_title)));
        AddGroup(new DueThisWeekGroup(this, DoMoment.getContext().getString(R.string.create_thisweek_title)));
        AddGroup(new DueLastWeekGroup(this, DoMoment.getContext().getString(R.string.create_lastweek_title)));
        AddGroup(new DueThisMonthGroup(this, DoMoment.getContext().getString(R.string.create_thismonth_title)));
        AddGroup(new DueLastMonthGroup(this, DoMoment.getContext().getString(R.string.create_lastmonth_title)));
        AddGroup(new DueWithinThreeMonthsGroup(this, DoMoment.getContext().getString(R.string.create_withinthreemonths_title)));
        AddGroup(new DueWithinSixMonthsGroup(this, DoMoment.getContext().getString(R.string.create_withinsixmonths_title)));
        AddGroup(new DueLongTimeAgoGroup(this, DoMoment.getContext().getString(R.string.create_longtimeage_title)));
        CheckArea();
    }

}
