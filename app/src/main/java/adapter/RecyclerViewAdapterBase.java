package adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import DataStructures.AllTasksCategory;
import DataStructures.CategoryBase;
import DataStructures.GroupBase;
import DataStructures.GroupListBase;
import DataStructures.ListItemBase;
import DataStructures.NoCategory;
import DataStructures.TimeLineAfterTomorrowGroup;
import DataStructures.TimeLineTodayGroup;
import DataStructures.TimeLineTomorrowGroup;
import ENUM.GroupType;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;
import DataStructures.Task;
import threecats.zhang.domoment.TaskDisplayActivity;

/**
 * Created by zhang on 2017/8/1.
 */

public abstract class RecyclerViewAdapterBase extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected DateTimeHelper dateTime = DoMoment.getDateTime();
    private GroupListBase groupList;
    private GroupBase group;
    private List<ListItemBase>  items;
    private int itemLayoutID;
    private int groupLayoutID;
    public boolean isChecked = false;
    private AdapterView.OnItemClickListener onItemClickListener;

    public void setGroupList(GroupListBase groupList){
        this.groupList = groupList;
    }
    public void setGroup(GroupBase group){
        this.group = group;
    }
    public void setItemLayoutID(int itemLayoutID){
        this.itemLayoutID = itemLayoutID;
    }
    public void setGroupLayoutID(int groupLayoutID){
        this.groupLayoutID = groupLayoutID;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        View currentItemView;
        CheckBox checkBox;
        public ItemViewHolder(View view){
            super(view);
            currentItemView = view;
            checkBox = view.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(v -> {
                if (checkBox == null){
                    Toast.makeText(v.getContext(),"Check Box is NULL !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Task task = (Task) checkBox.getTag();
                if (checkBox.isChecked()){
                    task.setChecked(true);
                } else {
                    task.setChecked(false);
                }
                //这里可以加入抽象方法，实现用户定制的点击CheckBox动作
                //doClickCheckBox();
            });
        }
        public Task getTask(){
            return (Task)items.get(getAdapterPosition());
        }
        public ItemViewHolder setText(int R, String text){
            TextView textView = currentItemView.findViewById(R);
            textView.setText(text);
            return ItemViewHolder.this;
        }
        public ItemViewHolder setImageResource(int R, int imageResId) {
            ImageView imageView = currentItemView.findViewById(R);
            imageView.setImageResource(imageResId);
            return ItemViewHolder.this;
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        View currentGroupView;
        public GroupViewHolder(View view){
            super(view);
            currentGroupView = view;
        }
        public GroupBase getGroup(){
            return (GroupBase)items.get(getAdapterPosition());
        }
        public GroupViewHolder setText(int R, String text){
            TextView textView = currentGroupView.findViewById(R);
            textView.setText(text);
            return GroupViewHolder.this;
        }
        public GroupViewHolder setImageResource(int R, int imageResId) {
            ImageView imageView = currentGroupView.findViewById(R);
            imageView.setImageResource(imageResId);
            return GroupViewHolder.this;
        }
    }

    public RecyclerViewAdapterBase(List<ListItemBase> items){
        this.items = items;
    }

    protected abstract void OnBindItem(ItemViewHolder holder, Task task, @Nullable GroupType groupType);
    protected abstract void OnBindGroup(GroupViewHolder holder, GroupBase group);
    protected abstract void OpenTips();
    protected abstract void CloseTips();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutID, parent, false);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                itemViewHolder.currentItemView.setOnClickListener(v -> {
                    Task task = itemViewHolder.getTask();
                    DoMoment.getDataManger().setCurrentTask(task);
                    if (isChecked) {
                        itemViewHolder.checkBox.setChecked(! task.getChecked());
                        task.setChecked(itemViewHolder.checkBox.isChecked());
                    } else {
                        //Intent taskIntent = new Intent(DoMoment.getMainActivity(),TaskDetailsActivity.class);
                        Intent taskIntent = new Intent(DoMoment.getMainActivity(),TaskDisplayActivity.class);
                        DoMoment.getMainActivity().startActivity(taskIntent);
                    }
                });
                itemViewHolder.currentItemView.setOnLongClickListener(v -> {
                    if (isChecked) return false;
                    Task task = itemViewHolder.getTask();
                    DoMoment.getDataManger().setCurrentTask(task);
                    DoMoment.getDataManger().getCurrentGroupList().setItemChecked(true);
                    return true;
                });
                return itemViewHolder;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(groupLayoutID, parent, false);
                final GroupViewHolder groupViewHolder = new GroupViewHolder(view);
                groupViewHolder.currentGroupView.setOnClickListener(v -> {
                    GroupBase group = groupViewHolder.getGroup();
                    if (group instanceof TimeLineTodayGroup) {
                        DoMoment.getDataManger().NewTask(Calendar.getInstance());
                    } else if (group instanceof TimeLineTomorrowGroup) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE,1);
                        DoMoment.getDataManger().NewTask(calendar);
                    } else if (group instanceof TimeLineAfterTomorrowGroup) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE,2);
                        DoMoment.getDataManger().NewTask(calendar);
                    }
                });
                return groupViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListItemBase item = items.get(position);
        switch (item.getItemType()){
            case Item:
                ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
                Task task = (Task)item;
                if (isChecked){
                    itemViewHolder.checkBox.setVisibility(View.VISIBLE);
                    itemViewHolder.checkBox.setChecked(task.getChecked());
                } else {
                    itemViewHolder.checkBox.setVisibility(View.GONE);
                }
                itemViewHolder.checkBox.setTag(task);
                GroupType groupType = task.getCurrentGroup(groupList).getGroupType();
                OnBindItem(itemViewHolder, task, groupType);
                break;
            case Group:
                GroupViewHolder groupViewHolder = (GroupViewHolder)holder;
                GroupBase group = (GroupBase)item;
                OnBindGroup(groupViewHolder, group);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType().ordinal();
    }

    @Override
    public int getItemCount() {
        int size = items.size();
        if (size == 0) {
            OpenTips();
        } else {
            CloseTips();
        }
        return size;
    }

}
