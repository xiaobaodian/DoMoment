package threecats.zhang.domoment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import threecats.zhang.domoment.DataStructures.TaskExt;
import threecats.zhang.domoment.DataStructures.TaskItem;
import threecats.zhang.domoment.ENUM.OneDayBase;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.layout.TitleFragment;

public class TaskDetailsFragment extends TitleFragment {

    private TaskItem task = App.getDataManger().getEditorTask();
    private TaskExt taskExt = App.getDataManger().getCurrentTaskExt();
    private final DateTimeHelper DateTime = App.getDateTime();
    private Context parentContext;
    private View taskDetailsView;
    private final int editStartDate = 1;
    private final int editDueDate = 2;
    private final int editStartTime = 3;
    private final int editDueTime = 4;
    private int EditType = editStartDate;

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

    @Override
    public String getTitle(){
        return "任务详情";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        taskDetailsView = inflater.inflate(R.layout.fragment_task_details, container, false);

        //==========setNavigationOnClickListener
        noteBox = taskDetailsView.findViewById(R.id.etTaskNote);
        placeBox = taskDetailsView.findViewById(R.id.etTaskPlace);
        startDateBox = taskDetailsView.findViewById(R.id.tvStartDate);
        dueDateBox = taskDetailsView.findViewById(R.id.tvDueDate);
        startTimeBox = taskDetailsView.findViewById(R.id.tvStartTime);
        dueTimeBox = taskDetailsView.findViewById(R.id.tvDueTime);
        recyclerDateBox = taskDetailsView.findViewById(R.id.tvRecyclerDate);
        timeLinearLayout = taskDetailsView.findViewById(R.id.TimeLinearLayout);
        addDateButton = taskDetailsView.findViewById(R.id.AddDateButton);
        addTimeButton = taskDetailsView.findViewById(R.id.AddTimeButton);
        aLine = taskDetailsView.findViewById(R.id.ALine);
        bLine = taskDetailsView.findViewById(R.id.BLine);

        startDateBox.setOnClickListener(view -> {
            EditType = editStartDate;
            EditDate();
        });
        dueDateBox.setOnClickListener(view -> {
            EditType = editDueDate;
            EditDate();
        });
        addDateButton.setOnClickListener(view -> {
            EditType = editDueDate;
            EditDate();
        });
        //==========
        startTimeBox.setOnClickListener(view -> {
            EditType = editStartTime;
            EditTime();
        });
        dueTimeBox.setOnClickListener(view -> {
            EditType = editDueTime;
            EditTime();
        });
        addTimeButton.setOnClickListener(view -> {
            EditType = editDueTime;
            EditTime();
        });

        return taskDetailsView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayTextFields();
        DisplayDateTimeFields();
    }

    private void DisplayTextFields(){
        noteBox.setText(task.getNote());
        placeBox.setText(task.getPlace());
    }
    @SuppressLint("SetTextI18n")
    private void DisplayDateTimeFields(){
        //taskExt.setTaskItem(task);  //刷新taskExt内部字段
        Log.d(App.TAG,"Display Task , "+taskExt.getTitle()+" is NoDate : "+taskExt.isNoDate());
        if (taskExt.isNoDate()) {
            startDateBox.setText("");
            dueDateBox.setVisibility(View.GONE);
            addDateButton.setVisibility(View.GONE);
            HideTimeRecylerItems();
        } else if (taskExt.isOneDay()){
            startDateBox.setText(taskExt.getBeginDateStr() + "日");
            dueDateBox.setVisibility(View.GONE);
            addDateButton.setVisibility(View.VISIBLE);
            DisplayTimeRecyclerItems();
        } else {
            startDateBox.setText(taskExt.getBeginDateStr() + "日");
            dueDateBox.setText(" -   " + taskExt.getEndDateStr() + "日");
            dueDateBox.setVisibility(View.VISIBLE);
            addDateButton.setVisibility(View.GONE);
        }
        if (taskExt.isAllDay()) {
            startTimeBox.setText("");
            dueTimeBox.setVisibility(View.GONE);
            addTimeButton.setVisibility(View.GONE);
        } else if (taskExt.isOneTime()) {
            startTimeBox.setText(taskExt.getBeginTimeStr());
            dueTimeBox.setVisibility(View.GONE);
            addTimeButton.setVisibility(View.VISIBLE);
            //dueTimeBox.setText(task.getDueTime());
        } else {
            startTimeBox.setText(taskExt.getBeginTimeStr());
            dueTimeBox.setText(" -   " + taskExt.getEndTimeStr());
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

        LayoutInflater layoutInflater = LayoutInflater.from(parentContext);
        View layout = layoutInflater.inflate(R.layout.taskeditor_dateselection, taskDetailsView.findViewById(R.id.DateDialong));
        DatePicker datePicker = layout.findViewById(R.id.datePicker);
        datePicker.setFirstDayOfWeek(Calendar.MONDAY);

        Calendar date;
        String title;

        switch (EditType) {
            case editStartDate:
                title = taskExt.isOneDay() ? "设置日期" : "设置开始日期";
                date = taskExt.isNoDate() ? Calendar.getInstance() : (Calendar)taskExt.getStartDateTime().clone();
                break;
            case editDueDate:
                title = "设置结束日期";
                date = (Calendar)taskExt.getDueDateTime().clone();
                if (taskExt.isOneDay()) date.add(Calendar.DATE, 1);
                break;
            default:
                title = "类型错误";
                date = Calendar.getInstance();
        }

        datePicker.init(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(parentContext);
        dialog.setTitle(title).setView(layout);

        dialog.setNeutralButton("删除", (dialogInterface, i) -> {
            final TaskItem task = App.getDataManger().getEditorTask();
            TaskExt taskExt = App.getDataManger().getCurrentTaskExt();
            switch (EditType) {
                case editStartDate:
                    if (taskExt.isOneDay()) {
                        taskExt.setNoDate();
                    } else {
                        taskExt.setOneDay(OneDayBase.Due);
                    }
                    break;
                case editDueDate:
                    taskExt.setOneDay(OneDayBase.Start);
                    break;
            }
            DisplayDateTimeFields();
        });

        dialog.setPositiveButton("确定", (dialogInterface, i) -> {
            final TaskItem task = App.getDataManger().getEditorTask();
            TaskExt taskExt = App.getDataManger().getCurrentTaskExt();
            Calendar SelectedDate;
            switch (EditType) {
                case editStartDate:

                    taskExt.setShiftStartDate(datePicker.getYear()
                            , datePicker.getMonth()
                            , datePicker.getDayOfMonth());

                    break;
                case editDueDate:

                    taskExt.setShiftDueDate(datePicker.getYear()
                            , datePicker.getMonth()
                            , datePicker.getDayOfMonth());

                    break;
            }

            DisplayDateTimeFields();
        });

        dialog.setNegativeButton("取消", (dialogInterface, i) -> App.Toast("取消编辑"));
        dialog.setOnDismissListener((v) -> {
            //onResume();
        });

        dialog.show();
    }

    //@TargetApi(Build.VERSION_CODES.M)
    private void EditTime(){

        LayoutInflater layoutInflater = LayoutInflater.from(parentContext);
        View layout = layoutInflater.inflate(R.layout.taskeditor_timeselection, taskDetailsView.findViewById(R.id.TimeDialong));
        TimePicker timePicker = layout.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        Calendar date;
        String title;

        switch (EditType) {
            case editStartTime:
                title = taskExt.isOneTime() ? "设置时间" : "设置开始时间";
                date = taskExt.isAllDay() ? Calendar.getInstance() : (Calendar)taskExt.getStartDateTime().clone();
                break;
            case editDueTime:
                title = "设置结束时间";
                date = (Calendar)taskExt.getDueDateTime().clone();
                if (taskExt.isOneTime()) date.add(Calendar.MINUTE, 5);
                break;
            default:
                title = "类型错误";
                date = Calendar.getInstance();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(date.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(date.get(Calendar.MINUTE));
        } else {
            timePicker.setCurrentHour(date.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(date.get(Calendar.MINUTE));
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(parentContext);
        dialog.setTitle(title).setView(layout);

        dialog.setNeutralButton("删除", (dialogInterface, i) -> {
            final TaskItem task = App.getDataManger().getEditorTask();
            TaskExt taskExt = App.getDataManger().getCurrentTaskExt();
            switch (EditType) {
                case editStartTime:
                    if (taskExt.isOneTime()) {
                        taskExt.setAllDay(true);
                    } else {
                        taskExt.setOneTime(OneDayBase.Due);
                    }
                    break;
                case editDueTime:
                    taskExt.setOneTime(OneDayBase.Start);
                    break;
            }
            DisplayDateTimeFields();
        });

        dialog.setPositiveButton("确定", (dialogInterface, i) -> {
            final TaskItem task = App.getDataManger().getEditorTask();
            TaskExt taskExt = App.getDataManger().getCurrentTaskExt();
            switch (EditType) {
                case editStartTime:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        taskExt.setBeginTime(timePicker.getHour(), timePicker.getMinute());
                    } else {
                        taskExt.setBeginTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                    break;
                case editDueTime:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        taskExt.setEndTime(timePicker.getHour(), timePicker.getMinute());
                    } else {
                        taskExt.setEndTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                    break;
            }
            DisplayDateTimeFields();
        });

        dialog.setNegativeButton("取消", (dialogInterface, i) -> {
        });

        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.setNote(noteBox.getText().toString());
        task.setPlace(placeBox.getText().toString());
    }

}
