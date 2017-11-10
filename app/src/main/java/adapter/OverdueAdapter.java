package adapter;

import android.widget.TextView;

import java.util.List;

import DataStructures.GroupBase;
import DataStructures.ListItemBase;
import DataStructures.Task;
import ENUM.DATEFORMAT;
import ENUM.GroupType;
import layout.ViewPageFragment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class OverdueAdapter extends RecyclerViewAdapterBase {
    private ViewPageFragment viewPageFragment;

    public OverdueAdapter(List<ListItemBase> items, ViewPageFragment viewPageFragment){
        super(items);
        this.viewPageFragment = viewPageFragment;
        setGroupLayoutID(R.layout.taskgroup_show);
        setItemLayoutID(R.layout.taskitem_show);
    }
    @Override
    protected void OnBindItem(ItemViewHolder holder, Task task, GroupType groupType) {
        holder
                .setText(R.id.taskTitle, task.getTitle())
                .setText(R.id.taskPlace, task.getPlace())
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

    @Override
    protected void OpenTips(){
        viewPageFragment.OpenTips();
    }
    @Override
    protected void CloseTips(){
        viewPageFragment.CloseTips();
    }
}
