package layout;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.security.DomainCombiner;

import DataStructures.CategoryBase;
import DataStructures.GroupListBase;
import adapter.RecyclerViewAdapterBase;
import adapter.TimeLineAdapter;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/10/23.
 */

public abstract class ViewPageFragment extends TitleFragment {
    protected View fragmentView;
    protected String title;
    protected int tipsTextID = 0;
    protected int fragmentLayoutID = 0;
    protected int recyclerViewID = 0;
    protected RecyclerViewAdapterBase groupListAdapter;
    protected GroupListBase groupList = null;
    private TextView TipsText;

    public abstract void LinkCategory(CategoryBase category);
    public void OpenTips(){
        if (TipsText != null) TipsText.setVisibility(View.VISIBLE);
    }
    public void CloseTips(){
        if (TipsText != null) TipsText.setVisibility(View.GONE);
    }

    protected void setGroupList(GroupListBase groupList){
        this.groupList = groupList;
        setTitle(groupList.getTitle());
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        if (groupList == null) return "U+";
        return title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(fragmentLayoutID, container, false);
        BindDatas();
        return fragmentView;
    }

    public void LinkTab(TabLayout.Tab tab){
        tab.setText(groupList.getTitle());
    }

    public void BindDatas(){
        if (fragmentView == null || groupList == null) return;
        TipsText = fragmentView.findViewById(tipsTextID);
        RecyclerView recyclerView = fragmentView.findViewById(recyclerViewID);
        try {
            groupList.BindRecyclerView(recyclerView, groupListAdapter, fragmentView);
            //TouchMode（即手机触摸屏模式）下默认没有Focus，只有设置如下才能获取OnFocusChange事件
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
            DoMoment.Toast("TimeLine Groups is Null");
        }
    }

    public int getTaskCount(){
        if (groupList == null) return 0;
        return groupList.getTaskCount();
    }
}
