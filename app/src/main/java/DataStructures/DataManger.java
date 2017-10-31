package DataStructures;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import Sqlite.SQLManger;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.TodoFragment;

/**
 * Created by zhang on 2017/8/14.
 */

public class DataManger {
    private SQLManger sqlDB;
    private ContentValues values = new ContentValues();
    private CategoryList categoryList;
    private CategoryBase currentCategory;
    private GroupListBase currentGroupList;
    private GroupBase currentGroup;
    private Task currentTask;
    private TodoFragment todoFragment;
    private boolean isDataloaded;

    public DataManger(){
        sqlDB = new SQLManger(DoMoment.getContext(), "IdoMoment.db", null, 1);
        isDataloaded = false;
        BuildCategorys();
    }

    private void BuildCategorys(){
        categoryList = new CategoryList();
        categoryList.AddCustomCategory(new CustomCategory("学习进修",10));
        categoryList.AddCustomCategory(new CustomCategory("宠物",11));
        categoryList.AddCustomCategory(new CustomCategory("公司事务",12));
        categoryList.AddCustomCategory(new CustomCategory("休闲娱乐",13));
        categoryList.AddCustomCategory(new CustomCategory("App应用开发",14));
        currentCategory = categoryList.getFirstCategory();
        currentGroupList = categoryList.getFirstGroupList();
    }

    public CategoryList getCategoryList(){
        return categoryList;
    }

    public void LoadDatas(){
        if (isDataloaded) return;
        //new LoadDatas().execute();
        Load();
        isDataloaded = true;
    }

    public void setTodoFragment(TodoFragment todoFragment){
        this.todoFragment = todoFragment;
    }
    public TodoFragment getTodoFragment(){
        return todoFragment;
    }

    public void setCurrentCategory(CategoryBase currentCategory){
        this.currentCategory = currentCategory;
        if( this.todoFragment != null) todoFragment.setCurrentCategory();
    }
    public CategoryBase getCurrentCategory(){
        return currentCategory;
    }

    public void setCurrentGroupList(GroupListBase groupList){
        this.currentGroupList = groupList;
    }
    public GroupListBase getCurrentGroupList(){
        return currentGroupList;
    }

    public void setCurrentGroup(GroupBase group){
        this.currentGroup = group;
    }
    public GroupBase getCurrentGroup(){
        return currentGroup;
    }

    public void setCurrentTask(Task task){
        this.currentTask = task;
    }
    public Task getCurrentTask(){
        return currentTask;
    }

    public Task NewTask(){
        Task task = new Task();
        return task;
    }

    public void AddTask(Task task){
        categoryList.AddTask(task);
    }

    public void InsertTask(Task task){
        categoryList.InsertTask(task);
    }

    public void RemoveTask(Task task){
        categoryList.RemoveTask(task);
    }

    public void ChangeTask(Task task){
        categoryList.ChangeTask(task);
        UpdateTaskToDataBase(task);
    }

    public void UpdateTask(Task task){
        categoryList.UpdateTask(task);
        UpdateTaskToDataBase(task);
    }

    public void CurrentDateChange(){
        categoryList.CurrentDateChange();
    }

    private void AddTaskToDataBase(Task task){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        AddTaskDBItem(db, task);
        db.close();
    }

    private void UpdateTaskToDataBase(Task task){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        UpdateTaskDBItem(db, task);
        db.close();
    }

    private void AddTaskDBItem(SQLiteDatabase db, Task task){
        PutValues(task);
        db.insert("Tasks", null, values);
    }

    private void UpdateTaskDBItem(SQLiteDatabase db, Task task){
        PutValues(task);
        db.update("Tasks",values,"id = ?", new String[]{task.getID()+""});
    }

    private void RemoveTaskDBItem(SQLiteDatabase db, Task task){
        db.delete("Tasks", "id = ?", new String[]{task.getID()+""});
    }
    public void RemoveTasksDB(List<Task> tasks){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        for (Task task : tasks) {
            RemoveTaskDBItem(db, task);
        }
        db.close();
    }

    private void PutValues(Task task){
        values.clear();
        values.put("CategoryID", task.getCategoryID());
        values.put("Title", task.getTitle());
        values.put("Place", task.getPlace());
        values.put("Note", task.getNote());
        values.put("Priority", task.getPriorityDB());
        values.put("CreateDateTime", task.getCreateDateTimeDB());
        values.put("StartDateTime", task.getStartDateTimeDB());
        values.put("DueDateTime", task.getDueDateTimeDB());
        values.put("CompleteDateTime", task.getCompleteDateTimeDB());
        values.put("isAllDay", task.IsAllDay());
        values.put("isNoDate", task.IsNoDate());
        values.put("isComplete", task.getComplete());
    }

    private void Load(){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            String sql = "select * from Tasks order by StartDateTime limit ?,?";
            cursor = db.rawQuery(sql, new String[]{"0", "360"});
            while (cursor.moveToNext()) {
                //Toast.makeText(DoMoment.getContext(),"Build Datasing ...",Toast.LENGTH_SHORT).show();
                Task task = new Task();
                task.setID(cursor.getInt(cursor.getColumnIndex("id")));
                task.setCategoryID(cursor.getInt(cursor.getColumnIndex("CategoryID")));
                task.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                task.setPlace(cursor.getString(cursor.getColumnIndex("Place")));
                task.setNote(cursor.getString(cursor.getColumnIndex("Note")));
                task.setPriorityDB(cursor.getInt(cursor.getColumnIndex("Priority")));
                task.setCreateDateTimeDB(cursor.getLong(cursor.getColumnIndex("CreateDateTime")));
                task.setStartDateTimeDB(cursor.getLong(cursor.getColumnIndex("StartDateTime")));
                task.setDueDateTimeDB(cursor.getLong(cursor.getColumnIndex("DueDateTime")));
                task.setCompleteDateTimeDB(cursor.getLong(cursor.getColumnIndex("CompleteDateTime")));
                task.setAllDay(cursor.getInt(cursor.getColumnIndex("isAllDay")) == 1);
                task.setNoDateDB(cursor.getInt(cursor.getColumnIndex("isNoDate")) == 1);
                task.setComplete(cursor.getInt(cursor.getColumnIndex("isComplete")) == 1);
                AddTask(task);
            }
            cursor.close();
        }
        db.close();

        //DoMoment.getDataManger().getTodoFragment().setProgressBarVisibility(View.GONE);
        //categoryList.BindDatas();
    }

    public void BuildDatas(){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        final int TotalTasks = 360;

        List<String> titles = new ArrayList<>();
        titles.add("购买动车票");
        titles.add("办理图书证");
        titles.add("下载一些todo软件参考");
        titles.add("测试滑动界面效果");
        titles.add("买小龙虾");
        titles.add("共享单车退押金");
        titles.add("修理鞋柜门");
        titles.add("去预定联欢会的糖果");
        titles.add("参观宠物展");
        titles.add("够买猫粮及猫砂");
        titles.add("交下半年物业费");
        titles.add("去银行更换密钥");
        titles.add("去看牙医");
        titles.add("办理新的上网套餐");
        titles.add("购买汽车年票");
        titles.add("去看看梨花展");
        titles.add("去聚餐");
        titles.add("下载新的电视剧");
        titles.add("联系旅游团");
        titles.add("了解周边商业区");
        titles.add("给比尔打电话");

        List<String> places = new ArrayList<>();
        places.add("办公室");
        places.add("家里");
        places.add("楚河汉街");
        places.add("南湖");
        places.add("广埠屯");
        places.add("武汉站");
        places.add("销品茂");
        places.add("武汉大学");
        places.add("墨水湖");
        places.add("二七路");
        places.add("同济");
        places.add("航空路");
        places.add("步行街");
        places.add("洪山广场");
        places.add("中南");
        places.add("徐东");

        int titleSum = titles.size();
        int pacleSum = places.size();
        Random random = new Random();
        for (int i = 0; i < TotalTasks; i++) {
            int dueDate = -1;
            Task task = new Task();

            task.setTitle(titles.get(random.nextInt(titleSum)));
            task.setPlace(places.get(random.nextInt(pacleSum)));

            Calendar day = Calendar.getInstance();
            int dayRange = random.nextInt(11);
            if (dayRange < 6) {
                day.add(Calendar.DATE, random.nextInt(15));
            } else if (dayRange >= 6 && dayRange < 9) {
                day.add(Calendar.DATE, random.nextInt(90));
            } else if (dayRange == 9){
                day.add(Calendar.DATE, 0-random.nextInt(10));
            } else if (dayRange == 10){
                dueDate = 1 + random.nextInt(7);
                day.add(Calendar.DATE, 0-dueDate);
            }
            if (dueDate == -1) {
                task.setStartDate(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
            } else {
                task.setCreateDateTime(day);
            }

            int categoryRange = random.nextInt(10);
            if (categoryRange >= 8) {
                task.setCategoryID(1);
            }

            AddTaskDBItem(db, task);

        }
        db.close();
    }
}
