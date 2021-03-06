package threecats.zhang.domoment.adapter;

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

import java.util.List;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.DataStructures.GroupBase;
import threecats.zhang.domoment.DataStructures.GroupListBase;
import threecats.zhang.domoment.DataStructures.RecyclerViewItem;
import threecats.zhang.domoment.DataStructures.TaskItem;
import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.R;
import threecats.zhang.domoment.DataStructures.Task;
import threecats.zhang.domoment.TaskDisplayActivity;

/**
 * Created by zhang on 2017/8/1.
 */

public abstract class RecyclerViewAdapterBase extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private GroupListBase groups;
    private GroupBase group;
    private List<RecyclerViewItem>  items;
    private int itemLayoutID;
    private int groupLayoutID;
    public boolean isChecked = false;
    private AdapterView.OnItemClickListener onItemClickListener;

    public void setGroups(GroupListBase groups){
        this.groups = groups;
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
        public TaskItem getTask(){
            return (TaskItem)items.get(getAdapterPosition());
        }
        public ItemViewHolder setText(int R, String text){
            TextView textView = currentItemView.findViewById(R);
            if (text.length() == 0) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(text);
            }
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

    public RecyclerViewAdapterBase(List<RecyclerViewItem> items){
        this.items = items;
    }

    protected abstract void OnBindItem(ItemViewHolder holder, TaskItem task, @Nullable GroupType groupType);
    protected abstract void OnBindGroup(GroupViewHolder holder, GroupBase group);
    protected abstract void OnGroupClick(GroupBase group);
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
                    TaskItem task = itemViewHolder.getTask();
                    App.self().getDataManger().setCurrentTask(task);
                    if (isChecked) {
                        itemViewHolder.checkBox.setChecked(! task.getChecked());
                        task.setChecked(itemViewHolder.checkBox.isChecked());
                    } else {
                        //Intent taskIntent = new Intent(App.getMainActivity(),TaskDetailsActivity.class);
                        Intent taskIntent = new Intent(App.self().getMainActivity(),TaskDisplayActivity.class);
                        App.self().getMainActivity().startActivity(taskIntent);
                    }
                });
                itemViewHolder.currentItemView.setOnLongClickListener(v -> {
                    if (isChecked) return false;
                    TaskItem task = itemViewHolder.getTask();
                    App.self().getDataManger().setCurrentTask(task);
                    //暂时关闭长安多选功能
                    //App.getDataManger().getCurrentGroupList().setItemChecked(true);
                    return true;
                });
                return itemViewHolder;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(groupLayoutID, parent, false);
                final GroupViewHolder groupViewHolder = new GroupViewHolder(view);
                groupViewHolder.currentGroupView.setOnClickListener(v -> {
                    OnGroupClick(groupViewHolder.getGroup());
                });
                return groupViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItem item = items.get(position);
        switch (item.getItemType()){
            case Item:
                ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
                TaskItem task = (TaskItem)item;
                if (isChecked){
                    itemViewHolder.checkBox.setVisibility(View.VISIBLE);
                    itemViewHolder.checkBox.setChecked(task.getChecked());
                } else {
                    itemViewHolder.checkBox.setVisibility(View.GONE);
                }
                itemViewHolder.checkBox.setTag(task);
                GroupType groupType = task.getCurrentGroup(groups).getGroupType();
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
