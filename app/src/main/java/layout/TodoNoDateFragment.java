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
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**

 */
public class TodoNoDateFragment extends TitleFragment {

    private View fragmentView;
    private GroupListBase noDateGroupList = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getTitle(){
        return DoMoment.getRString(R.string.grouplist_nodate_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_todo_nodate, container, false);
        BindDatas();
        return fragmentView;
    }

    public void BindDatas(){
        if (fragmentView == null) return;
        noDateGroupList = DoMoment.getCurrentCategory().getGroupList(GroupListType.Nodate);

        try {
            RecyclerView recyclerView = fragmentView.findViewById(R.id.NoDateRecyclerView);
            //NoDateAdapter GroupListAdapter = new NoDateAdapter(noDateGroupList.getRecyclerViewItems());
            NoDateAdapter groupListAdapter = new NoDateAdapter(noDateGroupList.getRecyclerViewItems());
            noDateGroupList.BindRecyclerView(recyclerView, groupListAdapter, fragmentView);
        } catch (Exception e){
            Toast.makeText(fragmentView.getContext(),"NoDate Groups is Null",Toast.LENGTH_SHORT).show();
        }
    }

    public int getTaskCount(){
        if (noDateGroupList == null) return 0;
        return noDateGroupList.getTaskCount();
    }

}
