package threecats.zhang.domoment;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import threecats.zhang.domoment.DataStructures.TaskExt;
import threecats.zhang.domoment.DataStructures.TaskItem;
import threecats.zhang.domoment.ENUM.EditorMode;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.EventClass.TaskEditorEvent;
import threecats.zhang.domoment.Helper.MaskDialog;
import threecats.zhang.domoment.Helper.UIHelper;
import threecats.zhang.domoment.adapter.SetTaskCategorysAdapter;
import threecats.zhang.domoment.adapter.TaskFragmentAdapter;
import threecats.zhang.domoment.layout.TitleFragment;


public class TaskDisplayActivity extends AppCompatActivity {

    private TaskItem task = App.getDataManger().getEditorTask();
    private TaskExt taskExt = App.getDataManger().getCurrentTaskExt();
    private int oldCategoryID;
    private TaskPriority oldPriority;
    private EditText etTaskTitle;
    private ImageView doneflag;
    private TextView tvCreatedDateTime;
    private Button btnCategory, btnPriority;
    private View thisView;
    private Context thisContext;
    private TabLayout taskTab;
    private ViewPager viewPager;
    private List<TitleFragment> viewFragmentList;
    private TaskDetailsFragment taskDetailsFragment;
    private TaskCheckListFragment taskCheckListFragment;
    private EditorMode editorMode = EditorMode.Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dispay);

        Toolbar editToolbar = (Toolbar) findViewById(R.id.TaskEditorToolbar);
        setSupportActionBar(editToolbar);

        etTaskTitle = (EditText) findViewById(R.id.editTextTitle);
        tvCreatedDateTime = (TextView)findViewById(R.id.layCreatedDateTime);
        btnCategory = (Button)findViewById(R.id.btnCategory);
        btnPriority = (Button)findViewById(R.id.btnPriority);
        doneflag = (ImageView)findViewById(R.id.doneflag);
        taskTab = (TabLayout)findViewById(R.id.lTaskTab);
        viewPager = (ViewPager)findViewById(R.id.lTaskPager);

        editToolbar.setNavigationOnClickListener(view -> {finish();});

        btnCategory.setOnClickListener(view -> {
            setTaskCategory();
        });
        btnPriority.setOnClickListener(view -> {
            setTaskPriority();
        });

        viewFragmentList = new ArrayList<>();
        taskDetailsFragment = new TaskDetailsFragment();
        taskCheckListFragment = new TaskCheckListFragment();
        viewFragmentList.add(taskDetailsFragment);
        viewFragmentList.add(taskCheckListFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager == null) {
            App.Toast("FragmentManager is null");
        } else {
            viewPager.setAdapter(new TaskFragmentAdapter(fragmentManager, viewFragmentList));
            taskTab.setupWithViewPager(viewPager);
        }

        etTaskTitle.setOnFocusChangeListener(mOnFocusChangeListener);

    }

    private View.OnFocusChangeListener mOnFocusChangeListener = (view, b) -> {
        if (b) {
            App.Toast("has focus");
        } else {
            App.Toast("no focus");
        }
    };

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        thisView = parent;
        thisContext = context;
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.setCurrentActivity(this);
        //oldCategoryID = task.getCategoryID();
        //oldPriority = task.getPriority();
        etTaskTitle.setText(task.getTitle());
        displayTaskItems();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void doEditorStat(TaskEditorEvent editorEvent){
        if (editorEvent.getEditorMode() == EditorMode.Add) {

            etTaskTitle.setFocusable(true);
            etTaskTitle.setFocusableInTouchMode(true);
            etTaskTitle.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            //InputMethodManager imm = (InputMethodManager) thisContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.showSoftInput(etTaskTitle, InputMethodManager.SHOW_FORCED);// 显示输入法
            //etTaskTitle.imeOptions="actionDone";
            //((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
        EventBus.getDefault().removeStickyEvent(editorEvent);
    }

    private void displayTaskItems(){
        if (taskExt.isComplete()) {
            doneflag.setVisibility(View.VISIBLE);
        } else {
            doneflag.setVisibility(View.GONE);
        }
        String createDateTimeStr = "创建于：" + taskExt.getCreatedDateTimeStr();
        tvCreatedDateTime.setText(createDateTimeStr);
        btnCategory.setText(App.getDataManger().getCategoryList().getCategoryTitle(task.getCategoryID()));
        String priorityTitle = "";
        if (taskExt.getPriority() == TaskPriority.Urgent) {
            priorityTitle = "紧急";
        } else if (taskExt.getPriority() == TaskPriority.VeryImprotant){
            priorityTitle = "非常重要";
        } else if (taskExt.getPriority() == TaskPriority.Improtant) {
            priorityTitle = "重要";
        } else if(taskExt.getPriority() == TaskPriority.Focus){
            priorityTitle = "关注";
        } else if (taskExt.getPriority() == TaskPriority.None){
            priorityTitle = "普通";
        } else {
            priorityTitle = "普通";
            taskExt.setPriority(TaskPriority.None);
        }
        btnPriority.setText(priorityTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskeditor, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (taskExt.isComplete()) {
            menu.findItem(R.id.taskeditormenu_makeout).setVisible(false);
        } else {
            menu.findItem(R.id.taskeditormenu_makeout).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.taskeditormenu_remove:
                MaskDialog maskDialog = new MaskDialog(thisContext, "确认需要删除此任务吗");
                maskDialog.setBtnOKOnClickListener(view -> {
                    editorMode = EditorMode.Remove;
                    finish();
                });
                maskDialog.setBtnCancelOnClickListener(view -> {
                    UIHelper.Toast("取消");
                });
                maskDialog.showAtLocation(thisView);
                break;
            case R.id.taskeditormenu_makeout:
                UIHelper.Toast("任务标记完成");
                taskExt.setComplete(true);
                finish();
                break;
        }
        //return super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.setTitle(etTaskTitle.getText().toString());
        App.getDataManger().commitEditorTask(editorMode);
        //EventBus.getDefault().post(new TaskEditorEvent(editorMode));
    }

    private void setTaskCategory(){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_categoryselection, (ViewGroup)findViewById(R.id.CategoryDialog));

        int oldCategoryID = taskExt.getCategoryID();

        AlertDialog.Builder categoryDialog = new AlertDialog.Builder(this);
        categoryDialog.setTitle("设置类目");
        categoryDialog.setView(layout);

        categoryDialog.setNeutralButton("删除", (dialogInterface, i) -> {
            taskExt.setCategoryID(1);
            displayTaskItems();
        });
        categoryDialog.setPositiveButton("确定", (dialogInterface, i) -> {
            displayTaskItems();
        });
        categoryDialog.setNegativeButton("取消", (dialogInterface, i) -> {
            taskExt.setCategoryID(oldCategoryID);
        });

        categoryDialog.setOnCancelListener(view ->{task.setCategoryID(oldCategoryID);});

        RecyclerView recyclerView = layout.findViewById(R.id.CategoryRecyclerView);
        SetTaskCategorysAdapter categoryAdapter = new SetTaskCategorysAdapter(App.getDataManger().getCategoryList().getCustomCategories());
        LinearLayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryDialog.show();
            }
        },200);
    }

    private void setTaskPriority(){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_priorityselection, (ViewGroup)findViewById(R.id.PriorityDialog));

        TaskPriority oldPriority = taskExt.getPriority();
        final CheckedTextView[] currentPriority = {null};

        AlertDialog.Builder priorityDialog = new AlertDialog.Builder(this);
        priorityDialog.setTitle("任务等级");
        priorityDialog.setView(layout);

        priorityDialog.setNeutralButton("删除", (dialogInterface, i) -> {
            taskExt.setPriority(TaskPriority.None);
            displayTaskItems();
        });
        priorityDialog.setPositiveButton("确定", (dialogInterface, i) -> {
            displayTaskItems();
        });
        priorityDialog.setNegativeButton("取消", (dialogInterface, i) -> {
            taskExt.setPriority(oldPriority);
        });

        priorityDialog.setOnCancelListener(view ->{taskExt.setPriority(oldPriority);});

        LinearLayout urgent = layout.findViewById(R.id.Urgent);
        CheckedTextView checkedUrgent = layout.findViewById(R.id.tvUrgent);
        urgent.setTag(TaskPriority.Urgent);
        if (taskExt.getPriority() == TaskPriority.Urgent) currentPriority[0] = checkedUrgent;

        LinearLayout veryImprotant = layout.findViewById(R.id.VeryImprotant);
        CheckedTextView checkedVeryImprotant = layout.findViewById(R.id.tvVeryImprotant);
        veryImprotant.setTag(TaskPriority.VeryImprotant);
        if (taskExt.getPriority() == TaskPriority.VeryImprotant) currentPriority[0] = checkedVeryImprotant;

        LinearLayout improtant = layout.findViewById(R.id.Improtant);
        CheckedTextView checkedImprotant = layout.findViewById(R.id.tvImprotant);
        improtant.setTag(TaskPriority.Improtant);
        if (taskExt.getPriority() == TaskPriority.Improtant) currentPriority[0] = checkedImprotant;

        LinearLayout focus = layout.findViewById(R.id.Focus);
        CheckedTextView checkedFocus = layout.findViewById(R.id.tvFocus);
        focus.setTag(TaskPriority.Focus);
        if (taskExt.getPriority() == TaskPriority.Focus) currentPriority[0] = checkedFocus;

        if (currentPriority[0] != null) currentPriority[0].setChecked(true);

        urgent.setOnClickListener(view -> {
            taskExt.setPriority((TaskPriority) view.getTag());
            if (currentPriority[0] != null) currentPriority[0].setChecked(false);
            currentPriority[0] = checkedUrgent;
            currentPriority[0].setChecked(true);
        });
        veryImprotant.setOnClickListener(view -> {
            taskExt.setPriority((TaskPriority) view.getTag());
            if (currentPriority[0] != null) currentPriority[0].setChecked(false);
            currentPriority[0] = checkedVeryImprotant;
            currentPriority[0].setChecked(true);
        });
        improtant.setOnClickListener(view -> {
            taskExt.setPriority((TaskPriority) view.getTag());
            if (currentPriority[0] != null) currentPriority[0].setChecked(false);
            currentPriority[0] = checkedImprotant;
            currentPriority[0].setChecked(true);
        });
        focus.setOnClickListener(view -> {
            taskExt.setPriority((TaskPriority) view.getTag());
            if (currentPriority[0] != null) currentPriority[0].setChecked(false);
            currentPriority[0] = checkedFocus;
            currentPriority[0].setChecked(true);
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                priorityDialog.show();
            }
        },200);
    }

}
