package adapter;

import android.widget.TextView;

import java.util.List;

import DataStructures.GroupBase;
import DataStructures.ListItemBase;
import DataStructures.Task;
import ENUM.GroupType;
import layout.ViewPageFragment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class TimeLineAdapter extends RecyclerViewAdapterBase {
    private ViewPageFragment viewPageFragment;

    public TimeLineAdapter(List<ListItemBase> items, ViewPageFragment viewPageFragment){
        super(items);
        this.viewPageFragment = viewPageFragment;
        setGroupLayoutID(R.layout.taskgroup_show);
        setItemLayoutID(R.layout.taskitem_show);
    }
    @Override
    protected void OnBindItem(ItemViewHolder holder, Task task, GroupType groupType) {
        holder.setText(R.id.taskTitle, task.getTitle()).setText(R.id.taskPlace, task.getPlace()+task.getParentGroup().size()+","+task.getPriority().ordinal());
        String datetimeRange = "";
        if (groupType == GroupType.toDay || groupType == GroupType.Tomorrow ||groupType == GroupType.AfterTomorrow) {
            datetimeRange = task.getTimeRange();
        } else {
            datetimeRange =  task.getDateRange();
        }
        if (datetimeRange.length() > 0) datetimeRange += "   ";
        holder.setText(R.id.taskDateTime, datetimeRange);
    }

    @Override
    protected void OnBindGroup(GroupViewHolder holder, GroupBase group) {
        holder.setText(R.id.groupDate, group.getDateArea());
        holder.setText(R.id.groupTitle, group.getTitle());
        switch (group.getGroupType()){
            case toDay:

                break;
            case Tomorrow:

                break;
            case AfterTomorrow:

                break;
            case WithinThisWeek:
                break;
            case WithinNextWeek:
                break;
            case WithinThisMonth:
                break;
            case WithinNextMonth:
                break;
            case WithinThreeMonths:
                break;
            case WithinSixMonths:
                break;
            case Forwards:
                break;
        }

    }

    @Override
    protected void OpenTips(){
        viewPageFragment.OpenTips();
    }
    @Override
    protected void CloseTips(){
        viewPageFragment.CloseTips();
    }
}
