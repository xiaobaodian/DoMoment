package threecats.zhang.domoment;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import DataStructures.CategoryBase;
import DataStructures.GroupBase;
import DataStructures.GroupListBase;
import DataStructures.Task;
import ENUM.DATEFORMAT;
import ENUM.OneDayBase;

public class TaskDetailsActivity extends AppCompatActivity {

    private final Task task = DoMoment.getDataManger().getCurrentTask();;
    private final DateTimeHelper DateTime = DoMoment.getDateTime();
    private final int editStartDate = 1;
    private final int editDueDate = 2;
    private final int editStartTime = 3;
    private final int editDueTime = 4;
    private int EditType = editStartDate;

    private EditText titleBox;
    private EditText noteBox;
    private EditText placeBox;
    private TextView startDateBox;
    private TextView dueDateBox;
    private TextView startTimeBox;
    private TextView dueTimeBox;
    private TextView recyclerDateBox;
    private ConstraintLayout timeLinearLayout;
    private ImageButton addDateButton;
    private ImageButton addTimeButton;
    private View aLine;
    private View bLine;

    public TaskDetailsActivity(){
        super();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        Toolbar editToolbar = (Toolbar) findViewById(R.id.TaskEditorToolbar1);
        setSupportActionBar(editToolbar);
        //==========setNavigationOnClickListener
        titleBox = (EditText)findViewById(R.id.etTaskTitle);
        noteBox = (EditText)findViewById(R.id.etTaskNote);
        placeBox = (EditText)findViewById(R.id.etTaskPlace);
        startDateBox = (TextView)findViewById(R.id.tvStartDate);
        dueDateBox = (TextView)findViewById(R.id.tvDueDate);
        startTimeBox = (TextView)findViewById(R.id.tvStartTime);
        dueTimeBox = (TextView)findViewById(R.id.tvDueTime);
        recyclerDateBox = (TextView)findViewById(R.id.tvRecyclerDate);
        timeLinearLayout = (ConstraintLayout)findViewById(R.id.TimeLinearLayout);
        addDateButton = (ImageButton) findViewById(R.id.AddDateButton);
        addTimeButton = (ImageButton) findViewById(R.id.AddTimeButton);
        aLine = findViewById(R.id.ALine);
        bLine = findViewById(R.id.BLine);
        //==========
        editToolbar.setNavigationOnClickListener(view -> {
            //Toast.makeText(getApplicationContext(),"baceup -> home",Toast.LENGTH_SHORT).show();
            finish();
        });
        //==========
        startDateBox.setOnClickListener(view -> {
            EditType = editStartDate;
            //EditDate();
        });
        dueDateBox.setOnClickListener(view -> {
            EditType = editDueDate;
            //EditDate();
        });
        addDateButton.setOnClickListener(view -> {
            EditType = editDueDate;
            //EditDate();
        });
        //==========
        startTimeBox.setOnClickListener(view -> {
            EditType = editStartTime;
            //EditTime();
        });
        dueTimeBox.setOnClickListener(view -> {
            EditType = editDueTime;
            //EditTime();
        });
        addTimeButton.setOnClickListener(view -> {
            EditType = editDueTime;
            //EditTime();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskeditor, menu);
        return true;  //super.onCreateOptionsMenu(menu)
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayTextFields();
        DisplayDateTimeFields();
    }

    private void DisplayTextFields(){
        titleBox.setText(task.getTitle());
        noteBox.setText(task.getNote());
        placeBox.setText(task.getPlace());
    }

    private void DisplayDateTimeFields(){
        if (task.IsNoDate()) {
            startDateBox.setText("");
            dueDateBox.setVisibility(View.GONE);
            addDateButton.setVisibility(View.GONE);
            HideTimeRecylerItems();
        } else if (task.IsOneDay()){
            startDateBox.setText(task.getBeginDateString(DATEFORMAT.China));
            dueDateBox.setVisibility(View.GONE);
            addDateButton.setVisibility(View.VISIBLE);
            DisplayTimeRecyclerItems();
        } else {
            startDateBox.setText(task.getBeginDateString(DATEFORMAT.China));
            dueDateBox.setText(task.getEndDateString(DATEFORMAT.China));
            dueDateBox.setVisibility(View.VISIBLE);
            addDateButton.setVisibility(View.GONE);
        }
        if (task.IsAllDay()) {
            startTimeBox.setText("");
            dueTimeBox.setVisibility(View.GONE);
            addTimeButton.setVisibility(View.GONE);
        } else if (task.IsOneTime()) {
            startTimeBox.setText(task.getStartTime());
            dueTimeBox.setVisibility(View.GONE);
            addTimeButton.setVisibility(View.VISIBLE);
            //dueTimeBox.setText(task.getDueTime());
        } else {
            startTimeBox.setText(task.getStartTime());
            dueTimeBox.setText(task.getDueTime());
            dueTimeBox.setVisibility(View.VISIBLE);
            addTimeButton.setVisibility(View.GONE);
        }
    }

    private void HideTimeRecylerItems(){
        timeLinearLayout.setVisibility(View.GONE);
        recyclerDateBox.setVisibility(View.GONE);
        aLine.setVisibility(View.GONE);
        bLine.setVisibility(View.GONE);
    }
    private void DisplayTimeRecyclerItems(){
        timeLinearLayout.setVisibility(View.VISIBLE);
        recyclerDateBox.setVisibility(View.VISIBLE);
        aLine.setVisibility(View.VISIBLE);
        bLine.setVisibility(View.VISIBLE);
    }

    private void EditDate(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_dateselection, (ViewGroup)findViewById(R.id.DateDialong));
        DatePicker datePicker = layout.findViewById(R.id.datePicker);
        datePicker.setFirstDayOfWeek(Calendar.MONDAY);
        Calendar date;
        String title;
        switch (EditType) {
            case editStartDate:
                title = task.IsOneDay() ? "设置日期" : "设置开始日期";
                date = task.IsNoDate() ? Calendar.getInstance() : (Calendar)task.getStartDateTime().clone();
                break;
            case editDueDate:
                title = "设置结束日期";
                date = (Calendar)task.getDueDateTime().clone();
                if (task.IsOneDay()) date.add(Calendar.DATE, 1);
                break;
            default:
                title = "类型错误";
                date = Calendar.getInstance();
        }
        datePicker.init(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title).setView(layout);
        dialog.setNeutralButton("删除", (dialogInterface, i) -> {
            final Task task = DoMoment.getDataManger().getCurrentTask();
            switch (EditType) {
                case editStartDate:
                    if (task.IsOneDay()) {
                        task.setNoDate();
                    } else {
                        task.setOneDay(OneDayBase.Due);
                    }
                    break;
                case editDueDate:
                    task.setOneDay(OneDayBase.Start);
                    break;
            }
            DisplayDateTimeFields();
        });
        dialog.setPositiveButton("确定", (dialogInterface, i) -> {
            final Task task = DoMoment.getDataManger().getCurrentTask();
            Calendar SelectedDate;
            switch (EditType) {
                case editStartDate:
                    SelectedDate = (Calendar)task.getDueDateTime().clone();
                    DateTime.BlendCalendar(SelectedDate, datePicker);
                    if (SelectedDate.after(task.getDueDateTime())) {
                        long timeSpan = task.getDueDateTimeDB() - task.getStartDateTimeDB();
                        task.setStartDate(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());
                        task.setDueDateTimeDB(task.getStartDateTimeDB() + timeSpan);
                    } else {
                        task.setStartDate(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());
                    }
                    task.setStartDate(datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                    break;
                case editDueDate:
                    SelectedDate = (Calendar)task.getStartDateTime().clone();
                    DateTime.BlendCalendar(SelectedDate, datePicker);
                    Calendar tmpStartDate = (Calendar)task.getStartDateTime().clone();
                    if (SelectedDate.before(task.getStartDateTime())) {
                        if (task.IsOneDay()) {
                            task.setStartDate(datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                            task.setDueDate(tmpStartDate.get(Calendar.YEAR),tmpStartDate.get(Calendar.MONTH)+1,tmpStartDate.get(Calendar.DAY_OF_MONTH));
                        } else {
                            long timeSpan = task.getDueDateTimeDB() - task.getStartDateTimeDB();
                            task.setDueDate(datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                            task.setStartDateTimeDB(task.getDueDateTimeDB()-timeSpan);
                        }
                    } else {
                        task.setDueDate(datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                    }
                    break;
            }
            DisplayDateTimeFields();
        });
        dialog.setNegativeButton("取消", (dialogInterface, i) -> Toast.makeText(getApplicationContext(),"display task",Toast.LENGTH_SHORT).show());
        dialog.setOnDismissListener((v) -> {
            //onResume();
        });
        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void EditTime(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.taskeditor_timeselection,(ViewGroup) findViewById(R.id.TimeDialong));
        TimePicker timePicker = layout.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        Calendar date;
        String title;
        switch (EditType) {
            case editStartTime:
                title = task.IsOneTime() ? "设置时间" : "设置开始时间";
                date = task.IsAllDay() ? Calendar.getInstance() : (Calendar)task.getStartDateTime().clone();
                break;
            case editDueTime:
                title = "设置结束时间";
                date = (Calendar)task.getDueDateTime().clone();
                if (task.IsOneTime()) date.add(Calendar.MINUTE, 5);
                break;
            default:
                title = "类型错误";
                date = Calendar.getInstance();
        }
        timePicker.setHour(date.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(date.get(Calendar.MINUTE));
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title).setView(layout);
        dialog.setNeutralButton("删除", (dialogInterface, i) -> {
            final Task task = DoMoment.getDataManger().getCurrentTask();
            switch (EditType) {
                case editStartTime:
                    if (task.IsOneTime()) {
                        task.setAllDay(true);
                    } else {
                        task.setOneTime(OneDayBase.Due);
                    }
                    break;
                case editDueTime:
                    task.setOneTime(OneDayBase.Start);
                    break;
            }
            DisplayDateTimeFields();
        });
        dialog.setPositiveButton("确定", (dialogInterface, i) -> {
            final Task task = DoMoment.getDataManger().getCurrentTask();
            switch (EditType) {
                case editStartTime:
                    task.setStartTime(timePicker.getHour(), timePicker.getMinute());
                    break;
                case editDueTime:
                    task.setDueTime(timePicker.getHour(), timePicker.getMinute());
                    break;
            }
            DisplayDateTimeFields();
        });
        dialog.setNegativeButton("取消", (dialogInterface, i) -> {

        });
        dialog.show();
        //dialog.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.setTitle(titleBox.getText().toString());
        task.setNote(noteBox.getText().toString());
        task.setPlace(placeBox.getText().toString());
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
