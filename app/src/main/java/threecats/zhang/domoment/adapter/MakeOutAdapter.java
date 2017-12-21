package threecats.zhang.domoment.adapter;

import java.util.List;

import threecats.zhang.domoment.DataStructures.GroupBase;
import threecats.zhang.domoment.DataStructures.ListItemBase;
import threecats.zhang.domoment.DataStructures.RecyclerViewItem;
import threecats.zhang.domoment.DataStructures.Task;
import threecats.zhang.domoment.App;
import threecats.zhang.domoment.DataStructures.TaskExt;
import threecats.zhang.domoment.DataStructures.TaskItem;
import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.layout.ViewPageFragment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/9/7.
 */

public class MakeOutAdapter extends RecyclerViewAdapterBase {
    private ViewPageFragment viewPageFragment;

    public MakeOutAdapter(List<RecyclerViewItem> items, ViewPageFragment viewPageFragment){
        super(items);
        this.viewPageFragment = viewPageFragment;
        setGroupLayoutID(R.layout.taskgroup_show);
        setItemLayoutID(R.layout.taskitem_show);
    }
    @Override
    protected void OnBindItem(ItemViewHolder holder, TaskItem task, GroupType groupType) {
        TaskExt taskExt = new TaskExt(task);
        holder.setText(R.id.taskTitle, task.getTitle())
                .setText(R.id.taskPlace, "")
                .setText(R.id.taskDateTime, "完成于：" + taskExt.getCompleteDateTimeStr());
        if (App.self().getCurrentCategory().getCategoryID() == 0) {
            holder.setText(R.id.taskCategory, App.self().getDataManger().getCategoryList().getCategoryTitle(task.getCategoryID()));
        }
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
    protected void OnGroupClick(GroupBase group) {

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
