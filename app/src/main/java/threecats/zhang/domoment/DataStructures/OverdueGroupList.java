package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.TaskBasePoint;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.ENUM.GroupListType;
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
        setTitle(App.getContext().getString(R.string.grouplist_overdue_title));
        buildGroups();
    }

    @Override
    public boolean inGroupList(TaskItem task){
        TaskExt taskExt = new TaskExt(task);
        if (taskExt.isNoDate() || taskExt.isComplete()) return false;
        return taskExt.getDueDateTime().before(timePoint);
    }

    @Override
    public void buildTimePoint(){
        this.timePoint = DateTime.BuildTimePoint(0);
    }

    @Override
    protected void buildGroups(){

        //必须按顺序加入建立正确的链表，后面组的区间判断是依靠获取前一组的标记日完成的
        addGroup(new DueYesterdayGroup(this, App.getContext().getString(R.string.overdue_yesterday_title)));                //今天零时
        addGroup(new DueBeforeYesterdayGroup(this, App.getContext().getString(R.string.overdue_beforeyesterday_title)));   //昨天零时
        addGroup(new DueThisWeekGroup(this, App.getContext().getString(R.string.overdue_thisweek_title)));                 //前天零时
        addGroup(new DueLastWeekGroup(this, App.getContext().getString(R.string.overdue_lastweek_title)));                 //上周一零时
        addGroup(new DueThisMonthGroup(this, App.getContext().getString(R.string.overdue_thismonth_title)));               //
        addGroup(new DueLastMonthGroup(this, App.getContext().getString(R.string.overdue_lastmonth_title)));
        addGroup(new DueWithinThreeMonthsGroup(this, App.getContext().getString(R.string.overdue_withinthreemonths_title)));
        addGroup(new DueWithinSixMonthsGroup(this, App.getContext().getString(R.string.overdue_withinsixmonths_title)));
        addGroup(new DueLongTimeAgoGroup(this, App.getContext().getString(R.string.overdue_longtimeage_title)));
        checkArea();
    }

}
