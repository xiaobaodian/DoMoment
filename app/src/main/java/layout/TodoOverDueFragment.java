package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import DataStructures.OverdueGroupList;
import ENUM.GroupListType;
import adapter.OverdueAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

public class TodoOverDueFragment extends TitleFragment {

    private View overdueFragment;
    private OverdueGroupList overdueGroupList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getTitle(){
        return DoMoment.getRString(R.string.viewgroup_overdue_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        overdueFragment = inflater.inflate(R.layout.fragment_todo_overdue, container, false);
        BindDatas();
        return overdueFragment;
    }

    public void BindDatas(){
        if (overdueFragment == null) return;
        overdueGroupList = (OverdueGroupList) DoMoment.getCurrentCategory().getGroupList(GroupListType.Overdue);
        try {
            RecyclerView recyclerView = (RecyclerView) overdueFragment.findViewById(R.id.OverdueRecyclerView);
            OverdueAdapter viewGroupAdapter = new OverdueAdapter(overdueGroupList.getRecyclerViewItems());
            overdueGroupList.BindRecyclerView(recyclerView, viewGroupAdapter, overdueFragment);
        } catch (Exception e){
            Toast.makeText(overdueFragment.getContext(),"Overdue Groups is Null",Toast.LENGTH_SHORT).show();
        }
    }

    public int getTaskCount(){
        if (overdueGroupList == null) return 0;
        return overdueGroupList.getTaskCount();
    }

}
