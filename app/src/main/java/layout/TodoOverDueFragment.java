package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import DataStructures.CategoryBase;
import DataStructures.GroupListBase;
import DataStructures.OverdueGroupList;
import ENUM.GroupListType;
import adapter.OverdueAdapter;
import adapter.TimeLineAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

public class TodoOverDueFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_overdue;
        recyclerViewID = R.id.OverdueRecyclerView;
    }

    public void LinkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.Overdue));
        groupListAdapter = new OverdueAdapter (groupList.getRecyclerViewItems());
        BindDatas();
    }
}
