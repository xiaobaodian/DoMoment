package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.TaskBasePoint;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.ENUM.GroupListType;
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
        setTitle(App.getContext().getString(R.string.grouplist_nodate_title));
        buildGroups();
    }

    @Override
    public boolean inGroupList(TaskItem task){
        TaskExt taskExt = new TaskExt(task);
        if (taskExt.isComplete()) return false;
        return taskExt.isNoDate();
    }

    @Override
    public void buildTimePoint(){
        timePoint = null;
    }

    @Override
    protected void buildGroups(){

        //必须按顺序加入建立正确的链表，后面组的区间判断是依靠获取前一组的标记日完成的
        addGroup(new DueTodayGroup(this, App.getContext().getString(R.string.create_today_title)));
        addGroup(new DueYesterdayGroup(this, App.getContext().getString(R.string.create_yesterday_title)));
        addGroup(new DueBeforeYesterdayGroup(this, App.getContext().getString(R.string.create_beforeyesterday_title)));
        addGroup(new DueThisWeekGroup(this, App.getContext().getString(R.string.create_thisweek_title)));
        addGroup(new DueLastWeekGroup(this, App.getContext().getString(R.string.create_lastweek_title)));
        addGroup(new DueThisMonthGroup(this, App.getContext().getString(R.string.create_thismonth_title)));
        addGroup(new DueLastMonthGroup(this, App.getContext().getString(R.string.create_lastmonth_title)));
        addGroup(new DueWithinThreeMonthsGroup(this, App.getContext().getString(R.string.create_withinthreemonths_title)));
        addGroup(new DueWithinSixMonthsGroup(this, App.getContext().getString(R.string.create_withinsixmonths_title)));
        addGroup(new DueLongTimeAgoGroup(this, App.getContext().getString(R.string.create_longtimeage_title)));
        checkArea();
    }

}
