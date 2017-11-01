package adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.GroupListBase;
import ENUM.GroupListType;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/1.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DateTimeHelper DateTime = DoMoment.getDateTime();
    private List<CategoryBase>  itemBases;

    //static
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView titleView;

        public ItemViewHolder(View view){
            super(view);
            this.view = view;
            titleView = (TextView)view.findViewById(R.id.categorytitle);;
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView titleView;
        TextView postView;
        public GroupViewHolder(View view){
            super(view);
            itemView = view;
            titleView = (TextView)view.findViewById(R.id.groupTitle);
        }
    }

    public CategoryAdapter(List<CategoryBase> itemBases){
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
                //Toast.makeText(v.getContext(), "点击了："+category.getTitle(),Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //GroupListBase currentGroupList = DoMoment.getDataManger().getCurrentGroupList();
                        //currentGroupList.setItemChecked(false);
                        //GroupListType type = currentGroupList.getType();
                        DoMoment.getCurrentCategory().UnBind();
                        DoMoment.getDataManger().setCurrentCategory(category);
                        //DoMoment.getDataManger().setCurrentGroupList(category.getGroupList(type));
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
        itemViewHolder.titleView.setText(title);
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
