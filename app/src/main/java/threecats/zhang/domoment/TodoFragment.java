package threecats.zhang.domoment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v7.app.AlertDialog;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.DataStructures.CustomCategory;
import threecats.zhang.domoment.DataStructures.GroupListBase;
import threecats.zhang.domoment.DataStructures.TaskExt;
import threecats.zhang.domoment.DataStructures.TaskItem;
import threecats.zhang.domoment.ENUM.EditorMode;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.Helper.MaskDialog;
import threecats.zhang.domoment.Helper.UIHelper;
import threecats.zhang.domoment.adapter.CategoryBackgroundAdapter;
import threecats.zhang.domoment.adapter.CategorySelectedAdapter;
import threecats.zhang.domoment.adapter.todoFragmentAdapter;
import threecats.zhang.domoment.layout.TodoMakeOutFragment;
import threecats.zhang.domoment.layout.TodoNoDateFragment;
import threecats.zhang.domoment.layout.TodoOverDueFragment;
import threecats.zhang.domoment.layout.TodoTimeLineFragment;
import threecats.zhang.domoment.layout.ViewPageFragment;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by zhang on 2017/7/25.
 */

public class TodoFragment extends Fragment {

    //private Bundle savedInstanceState;
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
    private Button btnAddCategory;
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
        //this.savedInstanceState = savedInstanceState;
        App.self().getDataManger().setTodoFragment(this);
        //currentCategory = App.getCurrentCategory();
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

        //App.getDataManger().loadToDoDatas();
        //new loadToDoDatas().execute();
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
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //if (slideOffset > 0.02) App.getMainActivity().setNavigationState(View.GONE);
                //if (slideOffset < 0.02) App.getMainActivity().setNavigationState(View.VISIBLE);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                App.self().setDrawerMenu(drawerLayout);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                App.self().setDrawerMenu(null);
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


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
                int id = App.self().getDataManger().getCategoryList().getUsabilityID();
                App.self().getDataManger().categoryEditor.newCategory(id);
                popupCategoryEditor();
            },150);
        });

        toolbar.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
            //Toast.makeText(v.getContext(), "点击了工具栏",Toast.LENGTH_SHORT).show();
//            boolean check = App.getDataManger().getCurrentGroupList().isItemChecked();
//            if (check){
//                App.getDataManger().getCurrentGroupList().setItemChecked(false);
//            } else {
//                App.getDataManger().getCurrentGroupList().setItemChecked(true);
//            }

        });
        toolbar.setNavigationOnClickListener(v -> {drawerLayout.openDrawer(GravityCompat.START);});

        collapsingToolbar = view.findViewById(R.id.todo_collapsing_toolbar);
        collapsingToolbar.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        addActionButton.setOnClickListener(v -> {
            //EventBus.getDefault().postSticky(new TaskEditorEvent(EditorMode.Add));
            //App.getDataManger().newTask(Calendar.getInstance());
            popupSimpleAddTask();
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
                        App.self().getDataManger().setCurrentGroupList(currentGroupList);
                        break;
                    case 1:
                        currentGroupList = currentCategory.getGroupList(GroupListType.Overdue);
                        App.self().getDataManger().setCurrentGroupList(currentGroupList);
                        break;
                    case 2:
                        currentGroupList = currentCategory.getGroupList(GroupListType.Nodate);
                        App.self().getDataManger().setCurrentGroupList(currentGroupList);
                    case 3:
                        currentGroupList = currentCategory.getGroupList(GroupListType.Complete);
                        App.self().getDataManger().setCurrentGroupList(currentGroupList);
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
        App.self().getDataManger().loadToDoDatas();
        setCurrentCategory();

        //EventBus.getDefault().register(this);
        //UIHelper.Toast("注册了EventBus");

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
                //safeRemoveCategory(notice);
                removeCategory();
                break;
            case R.id.TodoMenu_ChangedCategoryBackgroup:
                //setCategoryBackground();
                popupCategoryEditor();
//                CategoryBase currentCategory = App.getCurrentCategory();
//                currentCategory.setThemeBackgroundID(R.drawable.category_themebackground_3);
//                ImageView themebackground = (ImageView)fragmentView.findViewById(R.id.todo_appbar_image);
//                themebackground.setImageResource(currentCategory.getThemeBackgroundID());
//                App.getDataManger().UpdateCustomCategory((CustomCategory)currentCategory);
                break;
            case R.id.TodoMenu_ShowMainMenu:
                App.self().getMainActivity().showNavigation();
                break;
            case R.id.TodoMenu_EditCategoryTitle:
                UIHelper.Toast("生成测试数据");
                App.self().getDataManger().buildDatas();
                UIHelper.Toast("测试数据已生成");
                break;
        }
        //return super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (App.self().getCurrentCategory().getCategoryID() < 10) {
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
        if (DateTimeHelper.isCurrentDateChanged()) {
            App.self().getDataManger().currentDateChange();
            //Toast.makeText(getContext(),"日期<已经>变更", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getContext(),"日期<没有>变更", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

//    @Subscribe  //(sticky = true, threadMode = ThreadMode.MAIN)
//    public void doTaskEditor(TaskEditorEvent taskEditorEvent){
//        if (taskEditorEvent.getEditorMode() == EditorMode.Edit) {
//            App.Toast("调用了编辑模式");
//        } else {
//            App.Toast("调用了其他的模式");
//        }
//    }

    public void setBackgroundImage(CategoryBase currentCategory){
        if (currentCategory == null) return;
        ImageView backgroundImage = fragmentView.findViewById(R.id.todo_appbar_image);
        Glide.with(parentContext).load(currentCategory.getThemeBackgroundID()).transition(withCrossFade()).into(backgroundImage);
    }

    public void setGroupListTitle(String title){
        collapsingToolbar.setTitle(title);
    }

    public void setCurrentCategory(){
        drawerLayout.closeDrawer(GravityCompat.START);
        currentCategory = App.self().getCurrentCategory();
        setGroupListTitle(currentCategory.getTitle());
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
        categoryAdapter = new CategorySelectedAdapter(App.self().getDataManger().getCategoryList().getCategorys());
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

    private void popupSimpleAddTask(){

        View contentView = LayoutInflater.from(parentContext).inflate(R.layout.popupwindow_simple_addtask, null, false);

        PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000)); //new BitmapDrawable(),提示是过期方法
        //点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        //进入退出的动画

        ConstraintLayout root = contentView.findViewById(R.id.Root);
        ConstraintLayout simpleDate = contentView.findViewById(R.id.SimpleDate);

        root.setOnClickListener(v -> {
            if (simpleDate.getVisibility() == View.VISIBLE) {
                simpleDate.setVisibility(View.GONE);
            } else {
                popupWindow.dismiss();
            }
        });

        /*
         * 当前  i->左边  i1->上边  i2->右边  i3->下边
         * 原来  i4->左边  i5->上边  i6->右边  i7->下边
         */
        root.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            Integer tagInt = (Integer) view.getTag();
            //UIHelper.Toast("tagInt : "+tagInt+"  i3 : "+i3+" - i7 : "+i7);
            if (i3 == i7) {
                // 第一次（就是tagInt == null 时） i3 == i7 应该就是最低的底边，以后还有可能
                // i3 == i7 这时候就不是最低的底边了
                if (tagInt == null) {
                    view.setTag(i3);
                }
            } else {
                if (tagInt != null) {
                    if (tagInt == i3 && simpleDate.getVisibility() == View.GONE) {
                        popupWindow.dismiss();
                        //UIHelper.Toast("close input");
                    }
                }
            }
        });

        TaskExt taskExt = new TaskExt(new TaskItem());

        EditText taskTitle = contentView.findViewById(R.id.SimpleTaskTitle);
        UIHelper.showSoftKeyboard(taskTitle);

        Button buttonDate = contentView.findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(view -> {
            //UIHelper.closeSoftKeyboard(taskTitle);
            taskTitle.setFocusable(false);
            simpleDate.setVisibility(View.VISIBLE);
            simpleDate.setFocusable(true);
            simpleDate.setFocusableInTouchMode(true);
            taskTitle.setFocusable(true);
            taskTitle.setFocusableInTouchMode(true);
        });

        Button buttonCategory = contentView.findViewById(R.id.buttonCategory);
        buttonCategory.setOnClickListener(view -> {
            simpleDate.setVisibility(View.GONE);
        });
        int categoryID = App.self().getDataManger().getCategoryList().getUsabilityCategoryID();
        buttonCategory.setText(App.self().getDataManger().getCategoryList().getCategoryTitle(categoryID));
        taskExt.setCategoryID(categoryID);

        Button buttonDetail = contentView.findViewById(R.id.buttonDetail);
        buttonDetail.setOnClickListener(view -> {
            popupWindow.dismiss();
            taskExt.setTitle(taskTitle.getText().toString());
            App.self().getDataManger().newTask(taskExt.getTaskItem());
        });

        Button btnAddTask = contentView.findViewById(R.id.buttonAdd);
        btnAddTask.setOnClickListener(view -> {
            if ( ! taskTitle.getText().toString().isEmpty()) {
                taskExt.setTitle(taskTitle.getText().toString());
                App.self().getDataManger().simpleNewTask(taskExt.getTaskItem());
                taskTitle.setText("");
                buttonDate.setText("日期");
                buttonCategory.setText("未分类");
                taskExt.setTaskItem(new TaskItem());
            }
        });


        taskTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                simpleDate.setVisibility(View.GONE);
            }
        });

        taskTitle.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEND) {
                //UIHelper.Toast("editor action");
                btnAddTask.performClick();
                return true;
            }
            return false;
        });

        taskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    //textInputLayout.setErrorEnabled(true);
                    //textInputLayout.setError("不能为空");
                } else {
                    //textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button buttonToday = contentView.findViewById(R.id.buttonToDay);
        buttonToday.setOnClickListener(view -> {
            buttonDate.setText("今天");
            simpleDate.setVisibility(View.GONE);
            taskExt.setStartDate(Calendar.getInstance());
            //UIHelper.showSoftKeyboard(taskTitle);
        });
        Button buttonTomorrow = contentView.findViewById(R.id.buttonTomorrow);
        buttonTomorrow.setOnClickListener(view -> {
            buttonDate.setText("明天");
            simpleDate.setVisibility(View.GONE);
            taskExt.setStartDate(DateTimeHelper.getTomorrow());
            //UIHelper.showSoftKeyboard(taskTitle);
        });
        Button buttonAfterTomorrow = contentView.findViewById(R.id.buttonAfterTomorrow);
        buttonAfterTomorrow.setOnClickListener(view -> {
            buttonDate.setText("后天");
            simpleDate.setVisibility(View.GONE);
            taskExt.setStartDate(DateTimeHelper.getAfterTomorrow());
            //UIHelper.showSoftKeyboard(taskTitle);
        });
        Button buttonNextWeek = contentView.findViewById(R.id.buttonNextWeek);
        buttonNextWeek.setOnClickListener(view -> {
            buttonDate.setText("下周");
            simpleDate.setVisibility(View.GONE);
            taskExt.setStartDate(DateTimeHelper.getAfterTomorrow());
            UIHelper.showSoftKeyboard(taskTitle);
        });

        //TabLayout.Tab tab = viewTabLayout.getTabAt(2);
        //tab.select();
        App.self().setPopupWindow(popupWindow);
        popupWindow.showAtLocation(fragmentView, Gravity.BOTTOM, 0, 0);
    }

    private void safeRemoveCategory(String notice){
        AlertDialog.Builder removeCategoryDialog = UIHelper.getYNDialog(parentContext, notice);
        removeCategoryDialog.setPositiveButton("确定", (dialogInterface, i) -> {
            App.self().getDataManger().removeCustomCategory((CustomCategory) currentCategory);
            CategoryBase firstCategory = App.self().getDataManger().getCategoryList().getFirstCategory();
            App.self().getDataManger().setCurrentCategory(firstCategory);
            updateDrawerCategoryList();
        });
        removeCategoryDialog.setNegativeButton("取消", (dialogInterface, i) -> {
            UIHelper.Toast("取消");
        });
        removeCategoryDialog.show();
    }
    private void removeCategory(){
        String title = "删除类目《"+currentCategory.getTitle()+"》";
        String notice = "如果确认删除，该类目下所有的任务将成为未分类的任务";
        MaskDialog maskDialog = new MaskDialog(parentContext, title, notice);
        maskDialog.setBtnOKOnClickListener(view -> {
            App.self().getDataManger().removeCustomCategory((CustomCategory) currentCategory);
            CategoryBase firstCategory = App.self().getDataManger().getCategoryList().getFirstCategory();
            App.self().getDataManger().setCurrentCategory(firstCategory);
            updateDrawerCategoryList();
        });
        maskDialog.setBtnCancelOnClickListener(view -> {
            UIHelper.Toast("取消");
        });
        maskDialog.showAtLocation(fragmentView);
    }

    private void popupCategoryEditor(){
        CategoryBase editorCategory = App.self().getDataManger().categoryEditor.getEditorCategory();
        if (editorCategory.getCategoryID() < 10) {
            return;
        }
        View contentView = LayoutInflater.from(parentContext).inflate(R.layout.popupwindow_categroy_editor, null, false);
        PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

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
                    setGroupListTitle(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        popupWindow.setOnDismissListener(() -> {
            if (categoryTitle.getText().toString().isEmpty()) {
                UIHelper.Toast("标题设置错误，恢复到以前");
                setGroupListTitle(editorCategory.getTitle());
            } else {
                editorCategory.setTitle(categoryTitle.getText().toString());
                App.self().getDataManger().categoryEditor.commit(EditorMode.Edit);
                //App.getDataManger().UpdateCustomCategory((CustomCategory)currentCategory);
                updateDrawerCategoryList();
            }
        });

        RecyclerView recyclerView = contentView.findViewById(R.id.RecyclerView);
        CategoryBackgroundAdapter backgroupAdapter = new CategoryBackgroundAdapter(App.self().getCategoryThemebackgrounds(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(backgroupAdapter);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        //popupWindowCategoryEditor = popupWindow;
        App.self().setPopupWindow(popupWindow);
        popupWindow.showAtLocation(fragmentView, Gravity.BOTTOM, 0, 0);
    }

}