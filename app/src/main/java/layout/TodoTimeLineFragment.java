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
import ENUM.GroupListType;
import adapter.TimeLineAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

public class TodoTimeLineFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_timeline;
        recyclerViewID = R.id.TimeLineRecyclerView;
    }

    public void linkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.TimeLine));
        groupListAdapter = new TimeLineAdapter(groupList.getRecyclerViewItems());
        BindDatas();
    }
}
