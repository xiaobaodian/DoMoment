package threecats.zhang.domoment.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.List;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.DataStructures.Task;
import threecats.zhang.domoment.DataStructures.TaskExt;
import threecats.zhang.domoment.DataStructures.TaskItem;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.R;

/**
 * 由 zhang 于 2017/8/1 创建
 */

public class SetTaskCategorysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TaskItem task = App.self().getDataManger().getCurrentTask();
    private TaskExt taskExt =App.self().getDataManger().getCurrentTaskExt();
    private List<CategoryBase>  itemBases;
    private int currentChecked = -1;

    //static
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        View view;
        CheckedTextView tvTitle;

        public ItemViewHolder(View view){
            super(view);
            this.view = view;
            tvTitle = (CheckedTextView)view.findViewById(R.id.CategoryTitle);;
        }
    }

    public SetTaskCategorysAdapter(List<CategoryBase> itemBases){
        this.itemBases = itemBases;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskeditor_categoryitems, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        itemViewHolder.view.setOnClickListener(v -> {
            int position = itemViewHolder.getAdapterPosition();
            CategoryBase category = itemBases.get(position);
            itemViewHolder.tvTitle.setChecked(true);
            taskExt.setCategoryID(category.getCategoryID());
            if (currentChecked >= 0) {
                notifyItemChanged(currentChecked);
            }
            currentChecked = position;
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryBase item = itemBases.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        String title = item.getTitle();
        itemViewHolder.tvTitle.setText(title);
        if (taskExt.getCategoryID() == item.getCategoryID()) {
            itemViewHolder.tvTitle.setChecked(true);
            currentChecked = position;
        } else {
            itemViewHolder.tvTitle.setChecked(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //ListItemBase item = itemBases.get(position);
        return 0;
    }

    @Override
    public int getItemCount() {
        return itemBases.size();
    }
}
