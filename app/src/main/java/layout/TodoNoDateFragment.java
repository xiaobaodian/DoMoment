package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import DataStructures.NoDateGroupList;
import ENUM.GroupListType;
import adapter.NoDateAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**

 */
public class TodoNoDateFragment extends ViewFragment {

    private View nodateFragment;
    private String title = DoMoment.getRString(R.string.viewgroup_nodate_title);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.viewgroup_nodate_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nodateFragment = inflater.inflate(R.layout.fragment_todo_nodate, container, false);
        BindDatas();
        return nodateFragment;
    }

    public void BindDatas(){
        if (nodateFragment == null) return;
        NoDateGroupList noDateViewGroup = (NoDateGroupList) DoMoment.getDataManger().getCurrentCategory().getGroupList(GroupListType.Nodate);
        try {
            RecyclerView recyclerView = (RecyclerView) nodateFragment.findViewById(R.id.NoDateRecyclerView);
            //NoDateAdapter GroupListAdapter = new NoDateAdapter(noDateViewGroup.getRecyclerViewItems());
            NoDateAdapter viewGroupAdapter = new NoDateAdapter(noDateViewGroup.getRecyclerViewItems());
            noDateViewGroup.BindRecyclerView(recyclerView, viewGroupAdapter, nodateFragment);
        } catch (Exception e){
            Toast.makeText(nodateFragment.getContext(),"NoDate Groups is Null",Toast.LENGTH_SHORT).show();
        }
    }

}
