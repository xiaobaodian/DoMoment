package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import DataStructures.CategoryBase;
import ENUM.GroupListType;
import adapter.MakeOutAdapter;
import adapter.NoDateAdapter;
import threecats.zhang.domoment.DoMoment;
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
