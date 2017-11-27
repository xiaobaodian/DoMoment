package threecats.zhang.domoment.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.adapter.NoDateAdapter;
import threecats.zhang.domoment.R;

/**

 */
public class TodoNoDateFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_nodate;
        recyclerViewID = R.id.NoDateRecyclerView;
        tipsTextID = R.id.NullTips;
    }

    public void LinkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.Nodate));
        groupListAdapter = new NoDateAdapter(groupList.getRecyclerViewItems(),this);
        BindDatas();
    }
}
