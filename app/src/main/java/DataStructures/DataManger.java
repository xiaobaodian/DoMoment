package DataStructures;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ENUM.EditorMode;
import ENUM.TaskPriority;
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
    private Task currentTask = null;
    private Task editorTask = null;
    private TodoFragment todoFragment;
    private boolean isDataloaded;

    public CategoryEditor categoryEditor;

    public DataManger(){
        sqlDB = new SQLManger(DoMoment.getContext(), "IdoMoment.db", null, 1);
        isDataloaded = false;
        BuildCategorys();
    }

    private void BuildCategorys(){
        categoryList = new CategoryList();
        currentCategory = categoryList.getFirstCategory();
        currentGroupList = categoryList.getFirstGroupList();
        categoryEditor = new CategoryEditor();
    }

    public CategoryList getCategoryList(){
        return categoryList;
    }

    public void LoadDatas(){
        if (isDataloaded) return;
        //new LoadDatas().execute();
        LoadCategorys();
        LoadTasks();
        if (categoryList.IsNull()) InitCategory();
        isDataloaded = true;
    }

    public class CategoryEditor {
        CategoryBase editorCategory;
        CategoryBase oldCategory;
        public CategoryEditor(){
            editorCategory = null;
            oldCategory = null;
            //editorMode = EditorMode.Edit;
        }
        public CategoryBase newCategory(int ID){
            editorCategory = new CustomCategory("新的项目", ID);
            return editorCategory;
        }
        public CategoryBase getEditorCategory(){
            CategoryBase category = null;
            if (editorCategory == null) {
                category = currentCategory;
            } else {
                category = editorCategory;
                oldCategory = currentCategory;
                currentCategory = editorCategory;
                todoFragment.setCurrentCategory();
            }
            return category;
        }
        public void Commit(EditorMode mode){
            if (mode == EditorMode.Edit) {
                if (editorCategory != null){
                    if (editorCategory.title.length() > 0) {
                        AddCustomCategory((CustomCategory) editorCategory);
                        AddCategoryToDataBase((CustomCategory) editorCategory);
                    }
                } else {
                    UpdateCategoryToDataBase((CustomCategory) currentCategory);
                }
            } else {
                if (editorCategory == null){
                    RemoveCategoryFromDataBase((CustomCategory) currentCategory);
                } else {
                    currentCategory = oldCategory;
                    todoFragment.setCurrentCategory();
                    oldCategory = null;
                }
            }
            editorCategory = null;
        }
    }

    public void setEditorTask(Task task){
        editorTask = task;
    }
    public Task getEditorTask(){
        return  editorTask == null ? currentTask : editorTask;
    }
    public boolean hasEditorTask(){
        return editorTask != null;
    }
    public void commitEditorTask(EditorMode mode){
        if (mode == EditorMode.Edit) {
            if (hasEditorTask()){
                if (editorTask.title.length() > 0) {
                    AddTask(editorTask);
                    AddTaskToDataBase(editorTask);
                }
            } else {
                ChangeTask(currentTask);
            }
        } else {
            if (!hasEditorTask()){
                RemoveTask(currentTask);
            }
        }
        editorTask = null;
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

    public void NewTask(Calendar day){
        int categoryID = 1;
        CategoryBase category = getCurrentCategory();
        if (!(category instanceof AllTasksCategory || category instanceof NoCategory)) {
            categoryID = DoMoment.getCurrentCategory().getCategoryID();
        }
        Task task = new Task(categoryID, day);
        setEditorTask(task);
        DoMoment.showTaskDisplayActivity();
    }
    public void AddTask(Task task){
        categoryList.AddTask(task);
    }
    public void InsertTask(Task task){
        categoryList.InsertTask(task);
    }
    public void RemoveTask(Task task){
        categoryList.RemoveTask(task);
        RemoveTaskToDataBase(task);
    }
    private void ChangeTask(Task task){
        categoryList.ChangeTask(task);
        UpdateTaskToDataBase(task);
    }
    public void UpdateTask(Task task){
        //categoryList.UpdateTaskaa(task);
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
    private void RemoveTaskToDataBase(Task task){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        RemoveTaskDBItem(db, task);
        db.close();
    }
    private void AddTaskDBItem(SQLiteDatabase db, Task task){
        PutTaskValues(task);
        db.insert("Tasks", null, values);
    }
    private void UpdateTaskDBItem(SQLiteDatabase db, Task task){
        PutTaskValues(task);
        db.update("Tasks",values,"id = ?", new String[]{task.getID()+""});
    }
    private void RemoveTaskDBItem(SQLiteDatabase db, Task task){
        db.delete("Tasks", "id = ?", new String[]{task.getID()+""});
    }
    private void RemoveTasksDB(List<Task> tasks){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        for (Task task : tasks) {
            RemoveTaskDBItem(db, task);
        }
        db.close();
    }

    private void PutTaskValues(Task task){
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
        values.put("isComplete", task.IsComplete());
    }

    private void LoadTasks(){
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
                task.setCompleteDB(cursor.getInt(cursor.getColumnIndex("isComplete")) == 1);
                AddTask(task);
            }
            cursor.close();
        }
        assert db != null;
        db.close();

        //DoMoment.getDataManger().getTodoFragment().setProgressBarVisibility(View.GONE);
        //categoryList.BindDatas();
    }

    // Category操作
    public void AddCustomCategory(CustomCategory customCategory){
        categoryList.AddCustomCategory(customCategory);
    }
    public void UpdateCustomCategory(CustomCategory customCategory){
        UpdateCategoryToDataBase(customCategory);
    }
    public void RemoveCustomCategory(CustomCategory customCategory){
        categoryList.RemoveCustomCategory(customCategory);
        RemoveCategoryFromDataBase(customCategory);
    }

    // Category数据库操作

    private void AddCategoryToDataBase(CustomCategory customCategory){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        AddCategoryDBItem(db, customCategory);
        db.close();
    }
    private void UpdateCategoryToDataBase(CustomCategory customCategory){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        UpdateCategoryDBItem(db, customCategory);
        db.close();
    }
    private void RemoveCategoryFromDataBase(CustomCategory customCategory){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        RemoveCategoryDBItem(db, customCategory);
        db.close();
    }

    private void AddCategoryDBItem(SQLiteDatabase db, CustomCategory customCategory){
        PutCategoryValues(customCategory);
        db.insert("Categorys", null, values);
    }
    private void UpdateCategoryDBItem(SQLiteDatabase db, CustomCategory customCategory){
        PutCategoryValues(customCategory);
        db.update("Categorys", values,"id = ?", new String[]{customCategory.getID()+""});
    }
    private void RemoveCategoryDBItem(SQLiteDatabase db, CustomCategory customCategory){
        db.delete("Categorys", "id = ?", new String[]{customCategory.getID()+""});
    }
    private void PutCategoryValues(CustomCategory customCategory){
        values.clear();
        values.put("OrderID", customCategory.getOrderID());
        values.put("CategoryID", customCategory.getCategoryID());
        values.put("Title", customCategory.getTitle());
        values.put("Note", customCategory.getNote());
        values.put("iconID", customCategory.getIconId());
        values.put("themeBackgroundID", customCategory.getThemeBackgroundID());
    }

    private void LoadCategorys(){
        SQLiteDatabase db = sqlDB.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            String sql = "select * from Categorys";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                CustomCategory customCategory = new CustomCategory();
                customCategory.setID(cursor.getInt(cursor.getColumnIndex("id")));
                customCategory.setOrderID(cursor.getInt(cursor.getColumnIndex("OrderID")));
                customCategory.setCategoryID(cursor.getInt(cursor.getColumnIndex("CategoryID")));
                customCategory.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                customCategory.setNote(cursor.getString(cursor.getColumnIndex("Note")));
                customCategory.setIconId(cursor.getInt(cursor.getColumnIndex("iconID")));
                customCategory.setThemeBackgroundID(cursor.getInt(cursor.getColumnIndex("themeBackgroundID")));
                AddCustomCategory(customCategory);
            }
            cursor.close();
        }
        assert db != null;
        db.close();
    }

    // 初始化操作

    private void InitCategory(){
        CustomCategory customCategory;
        customCategory = new CustomCategory("学习与进修", 10);
        AddCustomCategory(customCategory);
        AddCategoryToDataBase(customCategory);
        customCategory = new CustomCategory("工作", 17);
        AddCustomCategory(customCategory);
        AddCategoryToDataBase(customCategory);
        customCategory = new CustomCategory("家庭", 16);
        AddCustomCategory(customCategory);
        AddCategoryToDataBase(customCategory);
        customCategory = new CustomCategory("宠物花鸟", 13);
        AddCustomCategory(customCategory);
        AddCategoryToDataBase(customCategory);
        customCategory = new CustomCategory("休闲娱乐", 18);
        AddCustomCategory(customCategory);
        AddCategoryToDataBase(customCategory);
        customCategory = new CustomCategory("投资理财", 15);
        AddCustomCategory(customCategory);
        AddCategoryToDataBase(customCategory);
    }


    // 辅助操作

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

    class EditorPoint {
        private Task task;
        private int categoryID;
        private long titleHash;
        private long placeHask;
        private TaskPriority priority;

        EditorPoint(Task task){
            this.task = task;
            categoryID = task.getCategoryID();
            titleHash = task.getTitle().hashCode();
            placeHask = task.getPlace().hashCode();
            priority = task.getPriority();
        }

    }

}
