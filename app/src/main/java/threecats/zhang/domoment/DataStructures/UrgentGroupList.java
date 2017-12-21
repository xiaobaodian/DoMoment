package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.ENUM.TaskBasePoint;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.Helper.UIHelper;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/14.
 */

public class UrgentGroupList extends GroupListBase {

    public UrgentGroupList(CategoryBase parent){
        super();
        this.parent = parent;
        this.selfType = GroupListType.TimeLine;
        this.timeSeries = TimeSeries.Forward;
        this.taskBasePoint = TaskBasePoint.BeginDate;   //后面加上 开始与结束的日期区间
        setTitle(UIHelper.getRString(R.string.grouplist_urgent_title));
        buildGroups();
    }

    @Override
    public boolean inGroupList(TaskItem task){
        return task.getPriorityID() == TaskPriority.Urgent.ordinal();
    }

    @Override
    public void buildTimePoint(){
        this.timePoint = DateTimeHelper.buildTimePoint(0);
    }

    @Override
    protected void buildGroups(){

        //必须按顺序加入建立正确的链表，后面组的区间判断是依靠获取前一组的标记日完成的
        addGroup(new AllOverdueGroup(this));
        addGroup(new TimeLineTodayGroup(this));
        addGroup(new TimeLineTomorrowGroup(this));
        addGroup(new TimeLineAfterTomorrowGroup(this));
        addGroup(new TimeLineThisWeekGroup(this));
        addGroup(new TimeLineNextWeekGroup(this));
        addGroup(new TimeLineThisMonthGroup(this));
        addGroup(new TimeLineNextMonthGroup(this));
        addGroup(new TimeLineWithinThreeMonthsGroup(this));
        addGroup(new TimeLineWithinSixMonthsGroup(this));
        addGroup(new TimeLineForwardGroup(this));
        checkArea();
    }

}
