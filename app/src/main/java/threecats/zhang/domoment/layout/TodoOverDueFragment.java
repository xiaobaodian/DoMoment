package threecats.zhang.domoment.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.adapter.OverdueAdapter;
import threecats.zhang.domoment.R;

public class TodoOverDueFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_overdue;
        recyclerViewID = R.id.OverdueRecyclerView;
        tipsTextID = R.id.NullTips;
    }

    public void LinkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.Overdue));
        groupListAdapter = new OverdueAdapter (groupList.getRecyclerViewItems(), this);
        BindDatas();
    }
}
