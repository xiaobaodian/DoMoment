package threecats.zhang.domoment.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.adapter.TimeLineAdapter;
import threecats.zhang.domoment.R;

public class TodoTimeLineFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_timeline;
        recyclerViewID = R.id.TimeLineRecyclerView;
        tipsTextID = R.id.NullTips;
    }

    public void LinkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.TimeLine));
        groupListAdapter = new TimeLineAdapter(groupList.getRecyclerViewItems(), this);
        BindDatas();
    }

}
