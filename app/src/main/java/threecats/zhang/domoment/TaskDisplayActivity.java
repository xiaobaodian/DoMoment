package threecats.zhang.domoment;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.GroupBase;
import DataStructures.GroupListBase;
import DataStructures.Task;
import ENUM.TaskPriority;
import adapter.TaskFragmentAdapter;
import adapter.todoFragmentAdapter;
import layout.TitleFragment;
import layout.ViewFragment;

public class TaskDisplayActivity extends AppCompatActivity {

    private final Task task = DoMoment.getDataManger().getCurrentTask();
    private Bundle savedInstanceState;
    private EditText etTaskTitle;
    private TextView tvCreatedDateTime;
    private View thisView;
    private TabLayout taskTab;
    private ViewPager viewPager;
    private List<TitleFragment> viewFragmentList;
    private TaskDetailsFragment taskDetailsFragment;
    private TaskCheckListFragment taskCheckListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_task_dispay);
        Toolbar editToolbar = (Toolbar) findViewById(R.id.TaskEditorToolbar);
        setSupportActionBar(editToolbar);
        etTaskTitle = (EditText) findViewById(R.id.editTextTitle);
        tvCreatedDateTime = (TextView)findViewById(R.id.layCreatedDateTime);
        Button btnCategory = (Button)findViewById(R.id.btnCategory);
        Button btnPriority = (Button)findViewById(R.id.btnPriority);
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
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etTaskTitle.setText(task.getTitle());
        tvCreatedDateTime.setText("创建于：" + task.getCreatedDateTimeStr());
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
        //task.getStartDateTimeDB() != hashStartDateTime || task.getDueDateTimeDB() != hashDueDateTime || hashIsNoDate != task.IsNoDate()
        if (isChanged) {
            DoMoment.getDataManger().ChangeTask(task);
        } else {
            DoMoment.getDataManger().UpdateTask(task);
        }
        //======
    }

    private void setTaskCategory(){
        DoMoment.Toast("Click Category Button");
    }
    private void setTaskPriority(){
        //DoMoment.Toast("Click Priority Button");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_priorityselection, (ViewGroup)findViewById(R.id.PriorityDialong));
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("任务等级");
        dialog.setView(layout);
        LinearLayout urgent = layout.findViewById(R.id.Urgent);
        urgent.setTag(TaskPriority.Urgent);
        LinearLayout improtant = layout.findViewById(R.id.Improtant);
        improtant.setTag(TaskPriority.Improtant);
        LinearLayout focus = layout.findViewById(R.id.Focus);
        focus.setTag(TaskPriority.Focus);
        LinearLayout none = layout.findViewById(R.id.None);
        none.setTag(TaskPriority.None);
        urgent.setOnClickListener(view -> {setPriority(dialog, view.getTag());});
        improtant.setOnClickListener(view -> {setPriority(dialog, view.getTag());});
        focus.setOnClickListener(view -> {setPriority(dialog, view.getTag());});
        none.setOnClickListener(view -> {setPriority(dialog, view.getTag());});
        //AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.show();
    }
    private void setPriority(AlertDialog dialog, Object priority){
        task.setPriority((TaskPriority)priority);
        dialog.dismiss();
    }
}
