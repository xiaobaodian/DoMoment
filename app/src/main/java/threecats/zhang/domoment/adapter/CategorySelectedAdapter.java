package threecats.zhang.domoment.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.App;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class CategorySelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DateTimeHelper DateTime = App.getDateTime();
    private List<CategoryBase>  itemBases;

    //static
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView tvCategoryTitle;
        ImageView ivCategoryIcon;

        public ItemViewHolder(View view){
            super(view);
            this.view = view;
            tvCategoryTitle = (TextView)view.findViewById(R.id.categorytitle);
            ivCategoryIcon = view.findViewById(R.id.categoryimage);
        }
    }

    public CategorySelectedAdapter(List<CategoryBase> itemBases){
        this.itemBases = itemBases;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_drawablelayoyt, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        itemViewHolder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = itemViewHolder.getAdapterPosition();
                CategoryBase category = itemBases.get(position);
                new Handler().postDelayed(() -> {
                    CategoryBase oldCategory = App.getCurrentCategory();
                    App.getDataManger().setCurrentCategory(category);
                    oldCategory.UnBind();
                },350);

            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryBase item = itemBases.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;
        itemViewHolder.tvCategoryTitle.setText(item.getTitle());
        itemViewHolder.ivCategoryIcon.setImageResource(item.getIconId());
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
