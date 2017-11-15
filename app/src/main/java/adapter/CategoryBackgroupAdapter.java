package adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import DataStructures.BackgroupBase;
import DataStructures.CategoryBase;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class CategoryBackgroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DateTimeHelper DateTime = DoMoment.getDateTime();
    private List<BackgroupBase>  itemBases;
    private int currentChecked = -1;

    //static
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        View view;
        RadioButton radioButton;
        ImageView backgroupImage;

        public ItemViewHolder(View view){
            super(view);
            this.view = view;
            radioButton = (RadioButton)view.findViewById(R.id.backgroupSelectedButton);
            backgroupImage = view.findViewById(R.id.backgroupImage);
        }
    }

    public CategoryBackgroupAdapter(List<BackgroupBase> itemBases){
        this.itemBases = itemBases;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_todo_categorybackgroupitem, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        itemViewHolder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = itemViewHolder.getAdapterPosition();
                BackgroupBase backgroup = itemBases.get(position);
                CategoryBase currentCategory = DoMoment.getCurrentCategory();
                currentCategory.setThemeBackgroundID(backgroup.getID());
                itemViewHolder.radioButton.setChecked(true);
                if (currentChecked >= 0) {
                    notifyItemChanged(currentChecked);
                }
                currentChecked = position;
            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BackgroupBase backgroup = itemBases.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        itemViewHolder.backgroupImage.setImageResource(backgroup.getID());
        CategoryBase currentCategory = DoMoment.getCurrentCategory();
        if (currentCategory.getThemeBackgroundID() == backgroup.getID()) {
            itemViewHolder.radioButton.setChecked(true);
            currentChecked = position;
        } else {
            itemViewHolder.radioButton.setChecked(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return itemBases.size();
    }
}
