package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.security.DomainCombiner;

import DataStructures.CategoryBase;
import DataStructures.GroupListBase;
import DataStructures.TimeLineGroupList;
import ENUM.GroupListType;
import adapter.TimeLineAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

public class TodoTimeLineFragment extends TitleFragment {

    private View fragmentView;
    private GroupListBase timeLineGroupList = null;
    //private TimeLineGroupList timeLineGroupList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("DoMoment","set Title");
    }

    @Override
    public String getTitle(){
        return DoMoment.getRString(R.string.grouplist_timeline_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_todo_timeline, container, false);
        BindDatas();
        return fragmentView;
    }

    public void BindDatas(){
        if (fragmentView == null) return;
        CategoryBase currentCategory = DoMoment.getCurrentCategory();
        //DoMoment.Toast(currentCategory.getTitle());
        timeLineGroupList = DoMoment.getCurrentCategory().getGroupList(GroupListType.TimeLine);
        if (timeLineGroupList == null) return;
        try {
            RecyclerView recyclerView = fragmentView.findViewById(R.id.TimeLineRecyclerView);
            TimeLineAdapter groupListAdapter = new TimeLineAdapter(timeLineGroupList.getRecyclerViewItems());
            timeLineGroupList.BindRecyclerView(recyclerView, groupListAdapter, fragmentView);
            //TouchMode下默认没有Focus，只有设置如下才能获取OnFocusChange事件
            recyclerView.setFocusableInTouchMode(true);
            recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b) {
                        //Toast.makeText(DoMoment.getContext(),"get Focus",Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(DoMoment.getContext(),"lost Focus",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e){
            Toast.makeText(fragmentView.getContext(),"TimeLine Groups is Null",Toast.LENGTH_SHORT).show();
        }
    }

    public int getTaskCount(){
        if (timeLineGroupList == null) return 0;
        return timeLineGroupList.getTaskCount();
    }

}
