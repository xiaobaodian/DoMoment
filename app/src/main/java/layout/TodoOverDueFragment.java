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
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

public class TodoOverDueFragment extends TitleFragment {

    private View fragmentView;
    private GroupListBase overdueGroupList = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getTitle(){
        return DoMoment.getRString(R.string.grouplist_overdue_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_todo_overdue, container, false);
        BindDatas();
        return fragmentView;
    }

    public void BindDatas(){
        if (fragmentView == null) return;
        overdueGroupList = DoMoment.getCurrentCategory().getGroupList(GroupListType.Overdue);

        try {
            RecyclerView recyclerView = fragmentView.findViewById(R.id.OverdueRecyclerView);
            OverdueAdapter groupListAdapter = new OverdueAdapter(overdueGroupList.getRecyclerViewItems());
            overdueGroupList.BindRecyclerView(recyclerView, groupListAdapter, fragmentView);
        } catch (Exception e){
            Toast.makeText(fragmentView.getContext(),"Overdue Groups is Null",Toast.LENGTH_SHORT).show();
        }
    }

    public int getTaskCount(){
        if (overdueGroupList == null) return 0;
        return overdueGroupList.getTaskCount();
    }

}
