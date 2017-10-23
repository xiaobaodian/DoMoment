package threecats.zhang.domoment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.NoDateGroupList;
import DataStructures.OverdueGroupList;
import DataStructures.Task;
import DataStructures.TimeLineGroupList;
import ENUM.GroupListType;
import adapter.CategorysAdapter;
import adapter.todoFragmentAdapter;
import layout.TodoMakeOutFragment;
import layout.TodoNoDateFragment;
import layout.TodoOverDueFragment;
import layout.TodoTimeLineFragment;
import layout.ViewFragment;

/**
 * Created by zhang on 2017/7/25.
 */

public class TodoFragment extends Fragment {

    private DateTimeHelper DateTime = DoMoment.getDateTime();
    private Toolbar toolbar = null;
    private CollapsingToolbarLayout collapsingToolbar = null;

    //private TimeLineAdapter adapter;
    //AppCompatActivity mAppCompatActivity;

    private CategoryBase currentCategory;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout viewTabLayout;
    private TabLayout tabLayout;
    private TodoTimeLineFragment timeLineFragment;
    private TodoOverDueFragment overDueFragment;
    private TodoNoDateFragment noDateFragment;
    private TodoMakeOutFragment makeOutFragment;
    private List<ViewFragment> viewFragmentList;
    private List<String> viewFragmentTitle;
    private ProgressBar progressBar;
    private View thisView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoMoment.getDataManger().setTodoFragment(this);

        //setRetainInstance(true);

        viewFragmentList = new ArrayList<>();
        viewFragmentTitle = new ArrayList<>();

        timeLineFragment = new TodoTimeLineFragment();
        overDueFragment = new TodoOverDueFragment();
        noDateFragment = new TodoNoDateFragment();
        makeOutFragment = new TodoMakeOutFragment();

        viewFragmentList.add(timeLineFragment);
        viewFragmentList.add(overDueFragment);
        viewFragmentList.add(noDateFragment);
        viewFragmentList.add(makeOutFragment);

        String a = viewFragmentList.get(0).getTitle();
        String b = viewFragmentList.get(1).getTitle();
        String c = viewFragmentList.get(2).getTitle();
        String d = viewFragmentList.get(3).getTitle();

        viewFragmentTitle.add(DoMoment.getContext().getString(R.string.viewgroup_timeline_title));
        viewFragmentTitle.add(DoMoment.getContext().getString(R.string.viewgroup_overdue_title));
        viewFragmentTitle.add(DoMoment.getContext().getString(R.string.viewgroup_nodate_title));
        viewFragmentTitle.add(DoMoment.getContext().getString(R.string.viewgroup_completed_title));

        currentCategory = DoMoment.getCurrentCategory();

        DoMoment.getDataManger().LoadDatas();
        //new LoadDatas().execute();


        if (savedInstanceState != null) {
            //Toast.makeText(getActivity(),"TabID:"+savedInstanceState.getInt("tablayout"),Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "on Create", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        thisView = view;
        drawerLayout = view.findViewById(R.id.drawer_layout);
        progressBar = view.findViewById(R.id.LoadDatasprogressBar);
        AppBarLayout appBarLayout = view.findViewById(R.id.todo_appbar);
        viewTabLayout = view.findViewById(R.id.ViewTabLayout);
        toolbar = view.findViewById(R.id.todo_toolbar);
        toolbar.inflateMenu(R.menu.todo_toolbar);
        FloatingActionButton addActionButton = view.findViewById(R.id.AddActionButton);


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //tv.setText(slideOffset+"");
                if (slideOffset > 0.02) DoMoment.getMainActivity().setNavigationState(View.GONE);
                if (slideOffset < 0.02) DoMoment.getMainActivity().setNavigationState(View.VISIBLE);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                //DoMoment.getMainActivity().setNavigationState(View.GONE);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                //DoMoment.getMainActivity().setNavigationState(View.VISIBLE);
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        //mAppCompatActivity.setSupportActionBar(toolbar);
        //setHasOptionsMenu(true);
        toolbar.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "点击了工具栏",Toast.LENGTH_SHORT).show();
            boolean check = DoMoment.getDataManger().getCurrentViewGroup().isItemChecked();
            if (check){
                DoMoment.getDataManger().getCurrentViewGroup().setItemChecked(false);
            } else {
                DoMoment.getDataManger().getCurrentViewGroup().setItemChecked(true);
            }

        });
        toolbar.setOnLongClickListener(v -> {
            Toast.makeText(v.getContext(), "生成测试数据",Toast.LENGTH_SHORT).show();
            DoMoment.getDataManger().BuildDatas();
            return true;
        });

        collapsingToolbar = view.findViewById(R.id.todo_collapsing_toolbar);
        if (currentCategory != null){
            SetGroupListTitle(currentCategory.getTitle());
            ImageView themebackground = view.findViewById(R.id.todo_appbar_image);
            themebackground.setImageResource(currentCategory.getThemeBackground());
        }


        collapsingToolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);

                //Task task = DoMoment.getDataManger().getCurrentTask();
                //Toast.makeText(DoMoment.getContext(),"Click "+task.getTitle()+" -> "+task.getParentGroups().size(),Toast.LENGTH_SHORT).show();

                //更新记录测试
                //if (DoMoment.getDataManger().getCurrentTask() == null) return;
                //Task t = DoMoment.getDataManger().getCurrentTask();
                //Date date = t.getStartDateTime();
                //t.setStartDateTime(DateTime.SetDate(date,2017,8,29));
                //t.isNoDate=false;
                //DoMoment.getDataManger().ChangeTask(t);

                //删除测试
                //DoMoment.getDataManger().RemoveTask(DM.getCurrentTask());

                //排序测试
                //timeLineGroups.SortAllGroup();
            }
        });

        //addActionButton.setOnClickListener11(new View.OnClickListener(){
        //    @Override
        //    public void onClick(View v){
        //        //插入记录测试
        //        Task t = new Task("插入新纪录====================","电脑");
        //        Date date = t.getStartDateTime();
        //        t.setStartDateTime(DateTime.SetDate(date,2017,8,27));
        //        currentCategory.AddTask(t);
        //    }
        //});

        addActionButton.setOnClickListener(v -> {
            //插入记录测试
            Task t = new Task("插入新纪录====================","电脑");
            t.setStartDate(2017,8,4);
            DoMoment.getDataManger().AddTask(t);
        });

        viewPager = view.findViewById(R.id.todo_viewpager);
        viewPager.setAdapter(new todoFragmentAdapter(getChildFragmentManager(),viewFragmentList, viewFragmentTitle));

        //tabLayout = view.findViewById(R.id.todo_tabLayout);
        //tabLayout.setupWithViewPager(viewPager);

        viewTabLayout.setupWithViewPager(viewPager);

        viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        //Toast.makeText(DoMoment.getContext(), "tab 1",Toast.LENGTH_SHORT).show();
                        TimeLineGroupList timeLineViewGroup = (TimeLineGroupList) DoMoment
                                .getCurrentCategory()
                                .getGroupList(GroupListType.TimeLine);
                        DoMoment.getDataManger().setCurrentViewGroup(timeLineViewGroup);
                        break;
                    case 1:
                        OverdueGroupList OverdueGroups = (OverdueGroupList) DoMoment.getCurrentCategory().getGroupList(GroupListType.Overdue);
                        DoMoment.getDataManger().setCurrentViewGroup(OverdueGroups);
                        break;
                    case 2:
                        NoDateGroupList inBoxGroups = (NoDateGroupList) DoMoment.getCurrentCategory().getGroupList(GroupListType.Nodate);
                        DoMoment.getDataManger().setCurrentViewGroup(inBoxGroups);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.Tab tab = viewTabLayout.getTabAt(1);
        tab.select();
        tab = viewTabLayout.getTabAt(0);
        tab.select();

        BuildsCategoryList(view);
        DoMoment.getDataManger().LoadDatas();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.todo_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.todo_setup:
                Toast.makeText(thisView.getContext(), "点击了工具栏",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        //检查是不是当前日期是新的一天
        if (DoMoment.getDateTime().IsCurrentDateChanged()) {
            DoMoment.getDataManger().CurrentDateChange();
            DoMoment.getDateTime().MarkToday();
            //Toast.makeText(getContext(),"日期<已经>变更", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getContext(),"日期<没有>变更", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(self.getContext(),"ToolBar Title is "+toolbar.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void SetGroupListTitle(String title){
        collapsingToolbar.setTitle(title);
    }

    public void setCurrentCategory(){
        currentCategory = DoMoment.getCurrentCategory();
        SetGroupListTitle(currentCategory.getTitle());
        ImageView themebackground = (ImageView)thisView.findViewById(R.id.todo_appbar_image);
        themebackground.setImageResource(currentCategory.getThemeBackground());
        drawerLayout.closeDrawer(GravityCompat.START);
        timeLineFragment.BindDatas();
        overDueFragment.BindDatas();
        noDateFragment.BindDatas();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tablayout",viewTabLayout.getSelectedTabPosition());
    }

    // 构建侧滑的类目列表
    private void BuildsCategoryList(View v){
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.CategoryRecyclerView);
        CategorysAdapter categoryAdapter = new CategorysAdapter(DoMoment.getDataManger().getCategorys());
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
    }

    public void setProgressBarVisibility(int Visibility){
        if (progressBar != null) progressBar.setVisibility(Visibility);
    }

}
