package DataStructures;

import ENUM.GroupListType;
import ENUM.TaskBasePoint;
import ENUM.TaskPriority;
import ENUM.TimeSeries;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/14.
 */

public class ImportanceGroupList extends GroupListBase {

    public ImportanceGroupList(CategoryBase parent){
        super();
        this.parent = parent;
        this.selfType = GroupListType.Nodate;
        this.timeSeries = TimeSeries.Forward;
        this.taskBasePoint = TaskBasePoint.BeginDate;   //后面加上 开始与结束的日期区间
        setTitle(DoMoment.getContext().getString(R.string.grouplist_timeline_title));
        BuildGroups();
    }

    @Override
    public boolean InGroupList(Task task){
        return task.getPriority() == TaskPriority.Improtant;
    }

    @Override
    public void BuildTimePoint(){
        this.timePoint = DateTime.BuildTimePoint(0);
    }

    @Override
    protected void BuildGroups(){

        //必须按顺序加入建立正确的链表，后面组的区间判断是依靠获取前一组的标记日完成的
        AddGroup(new TimeLineTodayGroup(this));
        AddGroup(new TimeLineTomorrowGroup(this));
        AddGroup(new TimeLineAfterTomorrowGroup(this));
        AddGroup(new TimeLineThisWeekGroup(this));
        AddGroup(new TimeLineNextWeekGroup(this));
        AddGroup(new TimeLineThisMonthGroup(this));
        AddGroup(new TimeLineNextMonthGroup(this));
        AddGroup(new TimeLineWithinThreeMonthsGroup(this));
        AddGroup(new TimeLineWithinSixMonthsGroup(this));
        AddGroup(new TimeLineForwardGroup(this));

        //TimeSeries t = this.getTimeSeries();
        CheckArea();
    }

}
