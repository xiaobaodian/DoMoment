package threecats.zhang.domoment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;
import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.GroupListBase;
import ENUM.GroupListType;
import adapter.CategoryAdapter;
import adapter.todoFragmentAdapter;
import layout.TodoMakeOutFragment;
import layout.TodoNoDateFragment;
import layout.TodoOverDueFragment;
import layout.TodoTimeLineFragment;
import layout.ViewPageFragment;

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
    private List<ViewPageFragment> fragmentList;
    private ProgressBar progressBar;
    private View thisView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoMoment.getDataManger().setTodoFragment(this);
        //currentCategory = DoMoment.getCurrentCategory();
        //setRetainInstance(true);

        fragmentList = new ArrayList<>();
        timeLineFragment = new TodoTimeLineFragment();
        overDueFragment = new TodoOverDueFragment();
        noDateFragment = new TodoNoDateFragment();
        makeOutFragment = new TodoMakeOutFragment();

        fragmentList.add(timeLineFragment);
        fragmentList.add(overDueFragment);
        fragmentList.add(noDateFragment);
        fragmentList.add(makeOutFragment);

        //DoMoment.getDataManger().LoadDatas();
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
        //AppBarLayout appBarLayout = view.findViewById(R.id.todo_appbar);
        viewTabLayout = view.findViewById(R.id.ViewTabLayout);
        toolbar = view.findViewById(R.id.todo_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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

        toolbar.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
            //Toast.makeText(v.getContext(), "点击了工具栏",Toast.LENGTH_SHORT).show();
//            boolean check = DoMoment.getDataManger().getCurrentGroupList().isItemChecked();
//            if (check){
//                DoMoment.getDataManger().getCurrentGroupList().setItemChecked(false);
//            } else {
//                DoMoment.getDataManger().getCurrentGroupList().setItemChecked(true);
//            }

        });

        collapsingToolbar = view.findViewById(R.id.todo_collapsing_toolbar);
        collapsingToolbar.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);

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
        });

        addActionButton.setOnClickListener(v -> {
            DoMoment.getDataManger().NewTask(Calendar.getInstance());
        });

        viewPager = view.findViewById(R.id.todo_viewpager);
        viewPager.setAdapter(new todoFragmentAdapter(getChildFragmentManager(), fragmentList));
        viewTabLayout.setupWithViewPager(viewPager);
        viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                GroupListBase currentGroupList = null;
                switch (tab.getPosition()){
                    case 0:
                        currentGroupList = currentCategory.getGroupList(GroupListType.TimeLine);
                        DoMoment.getDataManger().setCurrentGroupList(currentGroupList);
                        break;
                    case 1:
                        currentGroupList = currentCategory.getGroupList(GroupListType.Overdue);
                        DoMoment.getDataManger().setCurrentGroupList(currentGroupList);
                        break;
                    case 2:
                        currentGroupList = currentCategory.getGroupList(GroupListType.Nodate);
                        DoMoment.getDataManger().setCurrentGroupList(currentGroupList);
                    case 3:
                        currentGroupList = currentCategory.getGroupList(GroupListType.Complete);
                        DoMoment.getDataManger().setCurrentGroupList(currentGroupList);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        TabLayout.Tab tab = viewTabLayout.getTabAt(1);
//        tab.select();
//        tab = viewTabLayout.getTabAt(0);
//        tab.select();

        BuildsCategoryList(view);

        setCurrentCategory();
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
            case R.id.TodoMenu_RemoveCategory:
                Toast.makeText(thisView.getContext(), "点击了菜单",Toast.LENGTH_SHORT).show();
                break;
            case R.id.TodoMenu_EditCategoryTitle:
                DoMoment.Toast("生成测试数据");
                DoMoment.getDataManger().BuildDatas();
                DoMoment.Toast("测试数据已生成");
                break;
        }
        //return super.onOptionsItemSelected(item);
        return false;
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
        drawerLayout.closeDrawer(GravityCompat.START);
        currentCategory = DoMoment.getCurrentCategory();
        SetGroupListTitle(currentCategory.getTitle());
        ImageView themebackground = (ImageView)thisView.findViewById(R.id.todo_appbar_image);
        themebackground.setImageResource(currentCategory.getThemeBackgroundID());

        timeLineFragment.LinkCategory(currentCategory);
        timeLineFragment.LinkTab(viewTabLayout.getTabAt(0));
        overDueFragment.LinkCategory(currentCategory);
        overDueFragment.LinkTab(viewTabLayout.getTabAt(1));
        noDateFragment.LinkCategory(currentCategory);
        noDateFragment.LinkTab(viewTabLayout.getTabAt(2));
        makeOutFragment.LinkCategory(currentCategory);
        makeOutFragment.LinkTab(viewTabLayout.getTabAt(3));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tablayout",viewTabLayout.getSelectedTabPosition());
    }

    // 构建侧滑的类目列表
    private void BuildsCategoryList(View v){
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.CategoryRecyclerView);
        CategoryAdapter categoryAdapter = new CategoryAdapter(DoMoment.getDataManger().getCategoryList().getAllCategorys());
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
    }

    public void setProgressBarVisibility(int Visibility){
        if (progressBar != null) progressBar.setVisibility(Visibility);
    }

}
