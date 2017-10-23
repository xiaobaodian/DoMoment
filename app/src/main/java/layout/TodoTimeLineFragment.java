package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import DataStructures.TimeLineGroupList;
import ENUM.GroupListType;
import adapter.TimeLineAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

public class TodoTimeLineFragment extends ViewFragment {

    private View timelineFragment;
    private TimeLineGroupList timeLineViewGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.viewgroup_timeline_title);
        String t = getTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        timelineFragment = inflater.inflate(R.layout.fragment_todo_timeline, container, false);
        BindDatas();
        return timelineFragment;
    }

    public void BindDatas(){
        if (timelineFragment == null) return;
        timeLineViewGroup = (TimeLineGroupList) DoMoment.getCurrentCategory().getGroupList(GroupListType.TimeLine);
        try {
            RecyclerView recyclerView = (RecyclerView) timelineFragment.findViewById(R.id.TimeLineRecyclerView);
            TimeLineAdapter viewGroupAdapter = new TimeLineAdapter(timeLineViewGroup.getRecyclerViewItems());
            timeLineViewGroup.BindRecyclerView(recyclerView, viewGroupAdapter, timelineFragment);
            //TouchMode下默认没有Focus，只有设置如下才能获取OnFocusChange事件
            recyclerView.setFocusableInTouchMode(true);
            recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b) {
                        Toast.makeText(DoMoment.getContext(),"get Focus",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DoMoment.getContext(),"lost Focus",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e){
            Toast.makeText(timelineFragment.getContext(),"TimeLine Groups is Null",Toast.LENGTH_SHORT).show();
        }
    }



}
