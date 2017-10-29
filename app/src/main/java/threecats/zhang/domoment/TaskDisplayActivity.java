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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.GroupBase;
import DataStructures.GroupListBase;
import DataStructures.Task;
import ENUM.TaskPriority;
import adapter.CategorysAdapter;
import adapter.SetTaskCategorysAdapter;
import adapter.TaskFragmentAdapter;
import layout.TitleFragment;


public class TaskDisplayActivity extends AppCompatActivity {

    private final Task task = DoMoment.getDataManger().getCurrentTask();
    private int oldCategoryID;
    private EditText etTaskTitle;
    private TextView tvCreatedDateTime;
    private Button btnCategory, btnPriority;
    private View thisView;
    private TabLayout taskTab;
    private ViewPager viewPager;
    private List<TitleFragment> viewFragmentList;
    private TaskDetailsFragment taskDetailsFragment;
    private TaskCheckListFragment taskCheckListFragment;

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
        taskTab = (TabLayout)findViewById(R.id.lTaskTab);
        viewPager = (ViewPager)findViewById(R.id.lTaskPager);
        editToolbar.setNavigationOnClickListener(view -> {
            finish();
        });
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
            DoMoment.Toast("FragmentManager is null");
        } else {
            viewPager.setAdapter(new TaskFragmentAdapter(fragmentManager, viewFragmentList));
            taskTab.setupWithViewPager(viewPager);
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        thisView = parent;
        oldCategoryID = task.getCategoryID();
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayTaskItems();
    }

    private void DisplayTaskItems(){
        etTaskTitle.setText(task.getTitle());
        tvCreatedDateTime.setText("创建于：" + task.getCreatedDateTimeStr());
        btnCategory.setText(DoMoment.getDataManger().getCategoryList().getCategoryTitle(task.getCategoryID()));
        String priorityTitle = "";
        if (task.getPriority() == TaskPriority.Urgent) {
            priorityTitle = "紧急";
        } else if (task.getPriority() == TaskPriority.VeryImprotant){
            priorityTitle = "非常重要";
        } else if (task.getPriority() == TaskPriority.Improtant) {
            priorityTitle = "重要";
        } else if(task.getPriority() == TaskPriority.Focus){
            priorityTitle = "关注";
        } else if (task.getPriority() == TaskPriority.None){
            priorityTitle = "普通";
        } else {
            priorityTitle = "普通";
            task.setPriority(TaskPriority.None);
        }
        btnPriority.setText(priorityTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskeditor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.setTitle(etTaskTitle.getText().toString());
        //======
        boolean isChanged = true;
        if (oldCategoryID == task.getCategoryID()) {
            for (GroupBase group : task.getParentGroups()) {
                GroupListBase groupList = group.getParent();
                CategoryBase category = groupList.getParent();
                if (category.InCategory(task)) {
                    if (groupList.InGroupList(task)) {
                        if (group.InGroup(task)) {
                            isChanged = false;
                        }
                    }
                }
            }
        }

        //task.getStartDateTimeDB() != hashStartDateTime || task.getDueDateTimeDB() != hashDueDateTime || hashIsNoDate != task.IsNoDate()
        if (isChanged) {
            DoMoment.getDataManger().ChangeTask(task);
        } else {
            DoMoment.getDataManger().UpdateTask(task);
        }
        //======
    }

    private void setTaskCategory(){
        //DoMoment.Toast("Click Category Button");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_categoryselection, (ViewGroup)findViewById(R.id.CategoryDialog));
        int oldCategory = task.getCategoryID();
        AlertDialog.Builder categoryDialog = new AlertDialog.Builder(this);
        categoryDialog.setTitle("所属类目");
        categoryDialog.setView(layout);
        categoryDialog.setNeutralButton("删除", (dialogInterface, i) -> {
            task.setCategoryID(1);
            DisplayTaskItems();
        });
        categoryDialog.setPositiveButton("确定", (dialogInterface, i) -> {
            DisplayTaskItems();
        });
        categoryDialog.setNegativeButton("取消", (dialogInterface, i) -> {
            task.setCategoryID(oldCategory);
        });
        categoryDialog.setOnCancelListener(view ->{task.setCategoryID(oldCategory);});
        RecyclerView recyclerView = layout.findViewById(R.id.CategoryRecyclerView);
        SetTaskCategorysAdapter categoryAdapter = new SetTaskCategorysAdapter(DoMoment.getDataManger().getCategoryList().getCustomCategories());
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
        //DoMoment.Toast("Click Priority Button");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_priorityselection, (ViewGroup)findViewById(R.id.PriorityDialog));
        TaskPriority oldPriority = task.getPriority();
        AlertDialog.Builder priorityDialog = new AlertDialog.Builder(this);
        priorityDialog.setTitle("任务等级");
        priorityDialog.setView(layout);
        priorityDialog.setNeutralButton("删除", (dialogInterface, i) -> {
            task.setPriority(TaskPriority.None);
            DisplayTaskItems();
        });
        priorityDialog.setPositiveButton("确定", (dialogInterface, i) -> {
            DisplayTaskItems();
        });
        priorityDialog.setNegativeButton("取消", (dialogInterface, i) -> {
            task.setPriority(oldPriority);
        });
        priorityDialog.setOnCancelListener(view ->{task.setPriority(oldPriority);});
        LinearLayout urgent = layout.findViewById(R.id.Urgent);
        urgent.setTag(TaskPriority.Urgent);
        LinearLayout veryImprotant = layout.findViewById(R.id.VeryImprotant);
        veryImprotant.setTag(TaskPriority.VeryImprotant);
        LinearLayout improtant = layout.findViewById(R.id.Improtant);
        improtant.setTag(TaskPriority.Improtant);
        LinearLayout focus = layout.findViewById(R.id.Focus);
        focus.setTag(TaskPriority.Focus);
        urgent.setOnClickListener(view -> {task.setPriority((TaskPriority) view.getTag());});
        veryImprotant.setOnClickListener(view -> {task.setPriority((TaskPriority) view.getTag());});
        improtant.setOnClickListener(view -> {task.setPriority((TaskPriority) view.getTag());});
        focus.setOnClickListener(view -> {task.setPriority((TaskPriority) view.getTag());});

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                priorityDialog.show();
            }
        },200);
    }

}
