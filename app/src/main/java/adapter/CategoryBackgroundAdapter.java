package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;

import java.util.List;

import DataStructures.BackgroundBase;
import DataStructures.CategoryBase;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;
import threecats.zhang.domoment.TodoFragment;

/**
 * Created by zhang on 2017/8/1.
 */

public class CategoryBackgroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DateTimeHelper DateTime = DoMoment.getDateTime();
    private TodoFragment fragment;
    private List<BackgroundBase>  itemBases;
    private int currentCheckedSite = -1;

    //static
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageView CheckImage;
        ImageView backgroundImage;

        public ItemViewHolder(View view){
            super(view);
            this.view = view;
            CheckImage = view.findViewById(R.id.CheckImage);
            backgroundImage = view.findViewById(R.id.backgroupImage);
        }
    }

    public CategoryBackgroundAdapter(List<BackgroundBase> itemBases, TodoFragment fragment){
        this.itemBases = itemBases;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_todo_categorybackgrounditem, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        itemViewHolder.view.setOnClickListener(v -> {
            int position = itemViewHolder.getAdapterPosition();
            BackgroundBase background = itemBases.get(position);
            CategoryBase currentCategory = DoMoment.getCurrentCategory();
            currentCategory.setThemeBackgroundID(background.getID());
            itemViewHolder.CheckImage.setVisibility(View.VISIBLE);
            if (currentCheckedSite >= 0) {
                notifyItemChanged(currentCheckedSite);
            }
            currentCheckedSite = position;
            fragment.setBackgroundImage(currentCategory);
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BackgroundBase background = itemBases.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        //itemViewHolder.backgroundImage.setImageResource(backgroup.getID());
        Glide.with(fragment).load(background.getID()).into(itemViewHolder.backgroundImage);
        CategoryBase currentCategory = DoMoment.getCurrentCategory();
        if (currentCategory.getThemeBackgroundID() == background.getID()) {
            itemViewHolder.CheckImage.setVisibility(View.VISIBLE);
            currentCheckedSite = position;
        } else {
            itemViewHolder.CheckImage.setVisibility(View.GONE);
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
