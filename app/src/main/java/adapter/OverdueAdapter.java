package adapter;

import java.util.List;

import DataStructures.GroupBase;
import DataStructures.ListItemBase;
import DataStructures.Task;
import ENUM.DATEFORMAT;
import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class OverdueAdapter extends RecyclerViewAdapterBase {

    public OverdueAdapter(List<ListItemBase> items){
        super(items);
        setGroupLayoutID(R.layout.taskgroup_show);
        setItemLayoutID(R.layout.taskitem_show);
    }
    @Override
    protected void OnBindItem(ItemViewHolder holder, Task task, GroupType groupType) {
        holder
                .setText(R.id.taskTitle, task.getTitle())
                .setText(R.id.taskPlace, task.getPlace()+"-"+groupType.toString())
                .setText(R.id.taskDateTime, task.getBeginDateString());

    }

    @Override
    protected void OnBindGroup(GroupViewHolder holder, GroupBase group) {
        holder.setText(R.id.groupDate, group.getDateArea());
        holder.setText(R.id.groupTitle, group.getTitle());
        switch (group.getGroupType()){
            case Yesterday:
                break;
            case BeforeYesterday:
                break;
            case WithinThisWeek:
                break;
            case WithinLastWeek:
                break;
            case WithinThisMonth:
                break;
            case WithinLastMonth:
                break;
            case WithinThreeMonths:
                break;
            case WithinSixMonths:
                break;
            case LongTimeAgo:
                break;
        }

    }
}
