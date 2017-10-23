package threecats.zhang.domoment;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DataStructures.CategoryBase;
import DataStructures.GroupBase;
import DataStructures.GroupListBase;
import DataStructures.Task;
import adapter.TaskFragmentAdapter;
import adapter.todoFragmentAdapter;

public class TaskDisplayActivity extends AppCompatActivity {

    private final Task task = DoMoment.getDataManger().getCurrentTask();
    private EditText etTaskTitle;
    private TextView tvCreatedDateTime;
    private TabLayout taskTab;
    private ViewPager viewPager;
    private List<Fragment> viewFragmentList;
    private List<String> viewFragmentTitle;
    private TaskDetailsFragment taskDetailsFragment;
    private TaskCheckListFragment taskCheckListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dispay);
        Toolbar editToolbar = (Toolbar) findViewById(R.id.TaskEditorToolbar);
        setSupportActionBar(editToolbar);
        etTaskTitle = (EditText) findViewById(R.id.editTextTitle);
        tvCreatedDateTime = (TextView)findViewById(R.id.textViewCreatedDateTime);
        taskTab = (TabLayout)findViewById(R.id.lTaskTab);
        viewPager = (ViewPager)findViewById(R.id.lTaskPager);
        editToolbar.setNavigationOnClickListener(view -> {
            //Toast.makeText(getApplicationContext(),"baceup -> home",Toast.LENGTH_SHORT).show();
            finish();
        });
        viewFragmentList = new ArrayList<>();
        viewFragmentTitle = new ArrayList<>();
        taskDetailsFragment = new TaskDetailsFragment();
        taskCheckListFragment = new TaskCheckListFragment();
        viewFragmentList.add(taskDetailsFragment);
        viewFragmentList.add(taskCheckListFragment);
        viewFragmentTitle.add("详情");
        viewFragmentTitle.add("检查表");
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            Toast.makeText(getApplicationContext(),"FragmentManager is null",Toast.LENGTH_SHORT).show();
        } else {
            viewPager.setAdapter(new TaskFragmentAdapter(fm,viewFragmentList, viewFragmentTitle));
            taskTab.setupWithViewPager(viewPager);
        }

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
}
