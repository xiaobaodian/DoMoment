package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.ENUM.TaskBasePoint;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/14.
 */

public class OverdueGroupList extends GroupListBase {

    public OverdueGroupList(CategoryBase parent){
        super();
        this.parent = parent;
        this.selfType = GroupListType.Overdue;
        this.timeSeries = TimeSeries.Afterward;
        this.taskBasePoint = TaskBasePoint.EndDate;     //后面改为endDate
        setTitle(DoMoment.getContext().getString(R.string.grouplist_overdue_title));
        BuildGroups();
    }

    @Override
    public boolean InGroupList(Task task){
        if (task.IsNoDate() || task.IsComplete()) return false;
        return task.getDueDateTime().before(timePoint);
    }

    @Override
    public void BuildTimePoint(){
        this.timePoint = DateTime.BuildTimePoint(0);
    }

    @Override
    protected void BuildGroups(){

        //必须按顺序加入建立正确的链表，后面组的区间判断是依靠获取前一组的标记日完成的
        AddGroup(new DueYesterdayGroup(this, DoMoment.getContext().getString(R.string.overdue_yesterday_title)));                //今天零时
        AddGroup(new DueBeforeYesterdayGroup(this, DoMoment.getContext().getString(R.string.overdue_beforeyesterday_title)));   //昨天零时
        AddGroup(new DueThisWeekGroup(this, DoMoment.getContext().getString(R.string.overdue_thisweek_title)));                 //前天零时
        AddGroup(new DueLastWeekGroup(this, DoMoment.getContext().getString(R.string.overdue_lastweek_title)));                 //上周一零时
        AddGroup(new DueThisMonthGroup(this, DoMoment.getContext().getString(R.string.overdue_thismonth_title)));               //
        AddGroup(new DueLastMonthGroup(this, DoMoment.getContext().getString(R.string.overdue_lastmonth_title)));
        AddGroup(new DueWithinThreeMonthsGroup(this, DoMoment.getContext().getString(R.string.overdue_withinthreemonths_title)));
        AddGroup(new DueWithinSixMonthsGroup(this, DoMoment.getContext().getString(R.string.overdue_withinsixmonths_title)));
        AddGroup(new DueLongTimeAgoGroup(this, DoMoment.getContext().getString(R.string.overdue_longtimeage_title)));
        CheckArea();
    }

}