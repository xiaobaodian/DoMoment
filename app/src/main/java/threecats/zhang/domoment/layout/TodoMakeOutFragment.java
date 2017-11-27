package threecats.zhang.domoment.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.adapter.MakeOutAdapter;
import threecats.zhang.domoment.R;

/**

 */
public class TodoMakeOutFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_makeout;
        recyclerViewID = R.id.MarkOutRecyclerView;
        tipsTextID = R.id.NullTips;
    }

    public void LinkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.Complete));
        groupListAdapter = new MakeOutAdapter(groupList.getRecyclerViewItems(),this);
        BindDatas();
    }
}
