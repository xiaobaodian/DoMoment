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
import DataStructures.NoDateGroupList;
import ENUM.GroupListType;
import adapter.NoDateAdapter;
import adapter.OverdueAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**

 */
public class TodoNoDateFragment extends ViewPageFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentLayoutID = R.layout.fragment_todo_nodate;
        recyclerViewID = R.id.NoDateRecyclerView;
    }

    public void LinkCategory(CategoryBase category){
        setGroupList(category.getGroupList(GroupListType.Nodate));
        groupListAdapter = new NoDateAdapter(groupList.getRecyclerViewItems());
        BindDatas();
    }
}
