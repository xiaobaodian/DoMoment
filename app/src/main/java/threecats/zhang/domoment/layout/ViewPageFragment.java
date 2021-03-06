package threecats.zhang.domoment.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.DataStructures.GroupListBase;
import threecats.zhang.domoment.Helper.UIHelper;
import threecats.zhang.domoment.adapter.RecyclerViewAdapterBase;

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
        if (groupList == null) return "";
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
            groupList.bindRecyclerView(recyclerView, groupListAdapter, fragmentView);
            //TouchMode（即手机触摸屏模式）下默认没有Focus，只有设置如下才能获取OnFocusChange事件
            recyclerView.setFocusableInTouchMode(true);
            recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b) {
                        //Toast.makeText(App.getContext(),"get Focus",Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(App.getContext(),"lost Focus",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e){
            UIHelper.Toast("TimeLine Groups is Null");
        }
    }

    public int getTaskCount(){
        if (groupList == null) return 0;
        return groupList.getTaskCount();
    }
}
