package adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.Task;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class SetTaskCategorysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DateTimeHelper DateTime = DoMoment.getDateTime();
    private Task task = DoMoment.getDataManger().getCurrentTask();
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

        itemViewHolder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = itemViewHolder.getAdapterPosition();
                CategoryBase category = itemBases.get(position);
                itemViewHolder.tvTitle.setChecked(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        task.setCategoryID(category.getID());
                        if (currentChecked >= 0) {
                            notifyItemChanged(currentChecked);
                        }
                        currentChecked = position;
                    }
                },350);

            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryBase item = itemBases.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        String title = item.getTitle();
        itemViewHolder.tvTitle.setText(title);
        if (task.getCategoryID() == item.getID()) {
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
