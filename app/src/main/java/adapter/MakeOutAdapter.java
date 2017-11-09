package adapter;

import java.util.List;

import DataStructures.GroupBase;
import DataStructures.ListItemBase;
import DataStructures.Task;
import ENUM.GroupType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/9/7.
 */

public class MakeOutAdapter extends RecyclerViewAdapterBase {

    public MakeOutAdapter(List<ListItemBase> items){
        super(items);
        setGroupLayoutID(R.layout.taskgroup_show);
        setItemLayoutID(R.layout.taskitem_show);
    }
    @Override
    protected void OnBindItem(ItemViewHolder holder, Task task, GroupType groupType) {
        holder
                .setText(R.id.taskTitle, task.getTitle())
                .setText(R.id.taskPlace, " "+groupType.toString())
                .setText(R.id.taskDateTime, "完成于：" + task.getCompleteDateTimeStr());

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
