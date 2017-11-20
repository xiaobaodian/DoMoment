package threecats.zhang.domoment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.CustomCategory;
import DataStructures.GroupListBase;
import ENUM.EditorMode;
import ENUM.GroupListType;
import adapter.CategoryBackgroundAdapter;
import adapter.CategorySelectedAdapter;
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
    private Bundle savedInstanceState;
    private Context parentContext;
    private Toolbar toolbar = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private CategorySelectedAdapter categoryAdapter;

    //private TimeLineAdapter adapter;
    //AppCompatActivity mAppCompatActivity;

    private CategoryBase currentCategory;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout viewTabLayout;
    private ImageButton btnAddCategory;
    private TabLayout tabLayout;
    private TodoTimeLineFragment timeLineFragment;
    private TodoOverDueFragment overDueFragment;
    private TodoNoDateFragment noDateFragment;
    private TodoMakeOutFragment makeOutFragment;
    private List<ViewPageFragment> fragmentList;
    private ProgressBar progressBar;
    private View fragmentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
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
        fragmentView = view;
        drawerLayout = view.findViewById(R.id.drawer_layout);
        progressBar = view.findViewById(R.id.LoadDatasprogressBar);
        //AppBarLayout appBarLayout = view.findViewById(R.id.todo_appbar);
        viewPager = view.findViewById(R.id.todo_viewpager);
        viewTabLayout = view.findViewById(R.id.ViewTabLayout);
        toolbar = view.findViewById(R.id.todo_toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        FloatingActionButton addActionButton = view.findViewById(R.id.AddActionButton);

        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(() -> {
                //popupCategoryEditor();
                int id = DoMoment.getDataManger().getCategoryList().getUsabilityID();
                DoMoment.getDataManger().categoryEditor.newCategory(id);
                popupCategoryEditor();
            },200);
        });

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
        toolbar.setNavigationOnClickListener(v -> {drawerLayout.openDrawer(GravityCompat.START);});

        collapsingToolbar = view.findViewById(R.id.todo_collapsing_toolbar);
        collapsingToolbar.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        addActionButton.setOnClickListener(v -> {
            DoMoment.getDataManger().NewTask(Calendar.getInstance());
        });

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

        buildsDrawerCategoryList(view);

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
                Toast.makeText(fragmentView.getContext(), "点击了菜单",Toast.LENGTH_SHORT).show();
                break;
            case R.id.TodoMenu_ChangedCategoryBackgroup:
                //setCategoryBackground();
                popupCategoryEditor();
//                CategoryBase currentCategory = DoMoment.getCurrentCategory();
//                currentCategory.setThemeBackgroundID(R.drawable.category_themebackground_3);
//                ImageView themebackground = (ImageView)fragmentView.findViewById(R.id.todo_appbar_image);
//                themebackground.setImageResource(currentCategory.getThemeBackgroundID());
//                DoMoment.getDataManger().UpdateCustomCategory((CustomCategory)currentCategory);
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
    public void onPrepareOptionsMenu(Menu menu) {
        if (DoMoment.getCurrentCategory().getCategoryID() < 10) {
            menu.findItem(R.id.TodoMenu_ChangedCategoryBackgroup).setVisible(false);
            menu.findItem(R.id.TodoMenu_RemoveCategory).setVisible(false);
            //menu.findItem(R.id.TodoMenu_EditCategoryTitle).setVisible(false);
        } else {
            menu.findItem(R.id.TodoMenu_ChangedCategoryBackgroup).setVisible(true);
            menu.findItem(R.id.TodoMenu_RemoveCategory).setVisible(true);
            menu.findItem(R.id.TodoMenu_EditCategoryTitle).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackgroundImage(currentCategory);
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

    public void setBackgroundImage(CategoryBase currentCategory){
        if (currentCategory == null) return;
        ImageView backgroundImage = fragmentView.findViewById(R.id.todo_appbar_image);
        Glide.with(parentContext).load(currentCategory.getThemeBackgroundID()).into(backgroundImage);
    }

    public void SetGroupListTitle(String title){
        collapsingToolbar.setTitle(title);
    }

    public void setCurrentCategory(){
        drawerLayout.closeDrawer(GravityCompat.START);
        currentCategory = DoMoment.getCurrentCategory();
        SetGroupListTitle(currentCategory.getTitle());
        setBackgroundImage(currentCategory);
        //ImageView themebackground = (ImageView)fragmentView.findViewById(R.id.todo_appbar_image);
        //themebackground.setImageResource(currentCategory.getThemeBackgroundID());

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
    private void buildsDrawerCategoryList(View v){
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.CategoryRecyclerView);
        categoryAdapter = new CategorySelectedAdapter(DoMoment.getDataManger().getCategoryList().getCategorys());
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
    }
    private void updateDrawerCategoryList(){
        categoryAdapter.notifyDataSetChanged();
    }

    public void setProgressBarVisibility(int Visibility){
        if (progressBar != null) progressBar.setVisibility(Visibility);
    }

    private void popupCategoryEditor(){
        CategoryBase editorCategory = DoMoment.getDataManger().categoryEditor.getEditorCategory();
        if (editorCategory.getCategoryID() < 10) {
            return;
        }
        View contentView = LayoutInflater.from(parentContext).inflate(R.layout.popupwindow_categroy_editor, null, false);
        //实例化PopupWindow并设置宽高
        PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        //进入退出的动画
        //popupWindow.setAnimationStyle(R.style.MyPopWindowAnim);

        ConstraintLayout root = contentView.findViewById(R.id.Root);
        root.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        TextInputLayout textInputLayout = contentView.findViewById(R.id.TextInputLayout);
        TextInputEditText categoryTitle = contentView.findViewById(R.id.CategoryTitle);
        categoryTitle.setText(editorCategory.getTitle());
        categoryTitle.setOnFocusChangeListener((v, hasFocus) -> {

        });
        categoryTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("不能为空");
                } else {
                    textInputLayout.setErrorEnabled(false);
                    SetGroupListTitle(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        popupWindow.setOnDismissListener(() -> {
            if (categoryTitle.getText().toString().isEmpty()) {
                DoMoment.Toast("标题设置错误，恢复到以前");
                SetGroupListTitle(editorCategory.getTitle());
            } else {
                editorCategory.setTitle(categoryTitle.getText().toString());
                DoMoment.getDataManger().categoryEditor.Commit(EditorMode.Edit);
                //DoMoment.getDataManger().UpdateCustomCategory((CustomCategory)currentCategory);
                updateDrawerCategoryList();
            }
        });

        RecyclerView recyclerView = contentView.findViewById(R.id.RecyclerView);
        CategoryBackgroundAdapter backgroupAdapter = new CategoryBackgroundAdapter(DoMoment.getCategoryThemebackgrounds(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(backgroupAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        //popupWindowCategoryEditor = popupWindow;
        DoMoment.setPopupWindow(popupWindow);
        popupWindow.showAtLocation(fragmentView, Gravity.BOTTOM, 0, 0);
    }

}
