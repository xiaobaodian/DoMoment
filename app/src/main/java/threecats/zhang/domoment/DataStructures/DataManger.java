package threecats.zhang.domoment.DataStructures;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.EditorMode;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.TodoFragment;

/**
 * 由 zhang 于 2017/8/14 创建
 */

public class DataManger {

    private Box<TaskItem> taskBox;
    private Query<TaskItem> taskQuery;

    private Box<CheckItem> checkListBox;
    private Query<CheckItem> checkListQuery;

    private Box<CategoryItem> categoryBox;
    private Query<CategoryItem> categoryQuery;

    private ContentValues values = new ContentValues();
    private CategoryList categoryList;
    private CategoryBase currentCategory;
    private GroupListBase currentGroupList;
    private GroupBase currentGroup;
    private TaskItem currentTask = null;
    private TaskItem editorTask = null;
    private TaskExt currentTaskExt = new TaskExt();
    private TodoFragment todoFragment;
    private boolean isDataloaded;

    public CategoryEditor categoryEditor;

    public DataManger(){

        BoxStore boxStore = App.getBoxStore();

        taskBox = boxStore.boxFor(TaskItem.class);
        checkListBox = boxStore.boxFor(CheckItem.class);

        taskQuery = taskBox.query().order(TaskItem_.startDateTime).build();
        checkListQuery = checkListBox.query().build();

        categoryBox = boxStore.boxFor(CategoryItem.class);
        categoryQuery = categoryBox.query().order(CategoryItem_.orderID).build();

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

    public void setEditorTask(TaskItem task){
        editorTask = task;
        currentTaskExt.setTaskItem(task);
    }
    public TaskItem getEditorTask(){
        return  editorTask == null ? currentTask : editorTask;
    }
    public boolean hasEditorTask(){
        return editorTask != null;
    }
    public void commitEditorTask(EditorMode mode){
        if (mode == EditorMode.Edit) {
            if (hasEditorTask()){
                if (editorTask.getTitle().length() > 0) {
                    AddTask(editorTask);
                    taskBox.put(editorTask);
                    //AddTaskToDataBase(editorTask);
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

    public void setCurrentTask(TaskItem task){
        this.currentTask = task;
        this.currentTaskExt.setTaskItem(task);
    }
    public TaskItem getCurrentTask(){
        return currentTask;
    }
    public TaskExt getCurrentTaskExt(){
        return currentTaskExt;
    }

    public void NewTask(Calendar day){
        int categoryID = 1;
        CategoryBase category = getCurrentCategory();
        if (!(category instanceof AllTasksCategory || category instanceof NoCategory)) {
            categoryID = App.getCurrentCategory().getCategoryID();
        }
        //TaskItem task = new TaskItem(categoryID, day); ??
        TaskItem task = new TaskExt(categoryID, day).getTaskItem();
        setEditorTask(task);
        App.showTaskDisplayActivity();
    }
    public void AddTask(TaskItem task){
        categoryList.AddTask(task);
    }

    public void RemoveTask(TaskItem task){
        categoryList.RemoveTask(task);
        taskBox.remove(task);
    }
    private void ChangeTask(TaskItem task){
        categoryList.ChangeTask(task);
        taskBox.put(task);
    }
    public void UpdateTask(TaskItem task){
        taskBox.put(task);
    }

    public void CurrentDateChange(){
        categoryList.CurrentDateChange();
    }

    private void LoadTasks(){

        List<TaskItem> taskItems = taskQuery.find();

        for (TaskItem taskItem : taskItems) {
            AddTask(taskItem);
        }
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
        CategoryItem categoryItem = new CategoryItem();
        categoryCustomToItem(customCategory, categoryItem);
        categoryItem.setId(0);
        categoryBox.put(categoryItem);
    }
    private void UpdateCategoryToDataBase(CustomCategory customCategory){
        CategoryItem categoryItem = new CategoryItem();
        categoryCustomToItem(customCategory, categoryItem);
        categoryBox.put(categoryItem);
    }
    private void RemoveCategoryFromDataBase(CustomCategory customCategory){
        CategoryItem categoryItem = new CategoryItem();
        categoryCustomToItem(customCategory, categoryItem);
        categoryBox.remove(categoryItem);
    }

    private void LoadCategorys(){

        List<CategoryItem> categoryItems = categoryQuery.find();

        for (CategoryItem categoryItem : categoryItems) {

            CustomCategory customCategory = new CustomCategory();
            categoryItemToCustom(categoryItem, customCategory);
            AddCustomCategory(customCategory);

        }
    }

    private void categoryItemToCustom(CategoryItem item, CustomCategory custom){
        custom.setID(item.getId());
        custom.setOrderID(item.getOrderID());
        custom.setCategoryID(item.getCategoryID());
        custom.setTitle(item.getTitle());
        custom.setNote(item.getNote());
        custom.setIconId(item.getIconId());
        custom.setThemeBackgroundID(item.getThemeBackgroundID());
    }
    private void categoryCustomToItem(CustomCategory custom, CategoryItem item){
        item.setId(custom.getID());
        item.setOrderID(custom.getOrderID());
        item.setCategoryID(custom.getCategoryID());
        item.setTitle(custom.getTitle());
        item.setNote(custom.getNote());
        item.setIconId(custom.getIconId());
        item.setThemeBackgroundID(custom.getThemeBackgroundID());
    }

    // 初始化操作

    private void InitCategory(){

        List<CategoryItem> categoryItems = new ArrayList<>();

        CustomCategory customCategory;
        customCategory = new CustomCategory("学习与进修", 10);
        AddCustomCategory(customCategory);
        categoryItems.add(new CategoryItem(customCategory.getTitle(), customCategory.getCategoryID()));

        customCategory = new CustomCategory("工作", 17);
        AddCustomCategory(customCategory);
        categoryItems.add(new CategoryItem(customCategory.getTitle(), customCategory.getCategoryID()));

        customCategory = new CustomCategory("家庭", 16);
        AddCustomCategory(customCategory);
        categoryItems.add(new CategoryItem(customCategory.getTitle(), customCategory.getCategoryID()));

        customCategory = new CustomCategory("宠物花鸟", 13);
        AddCustomCategory(customCategory);
        categoryItems.add(new CategoryItem(customCategory.getTitle(), customCategory.getCategoryID()));

        customCategory = new CustomCategory("休闲娱乐", 18);
        AddCustomCategory(customCategory);
        categoryItems.add(new CategoryItem(customCategory.getTitle(), customCategory.getCategoryID()));
        //Log.d(App.TAG, "title : " + customCategory.getTitle());

        customCategory = new CustomCategory("投资理财", 15);
        AddCustomCategory(customCategory);
        categoryItems.add(new CategoryItem(customCategory.getTitle(), customCategory.getCategoryID()));

        categoryBox.put(categoryItems);
    }


    // 辅助操作

    public void BuildDatas(){
        final int TotalTasks = 80;
        List<TaskItem> taskItems = new ArrayList<>();

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
        titles.add("购买猫粮及猫砂");
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

            TaskItem taskItem = new TaskItem();
            TaskExt taskExt = new TaskExt();
            taskExt.setTaskItem(taskItem);

            taskExt.setTitle(titles.get(random.nextInt(titleSum)));
            taskExt.setPlace(places.get(random.nextInt(pacleSum)));

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
                taskExt.setStartDate(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
            } else {
                taskExt.setCreateDateTime(day);
            }

            taskExt.setCategoryID(1);

            taskItems.add(taskExt.getTaskItem());
            AddTask(taskExt.getTaskItem());
            //Log.d(App.TAG, "Start DateTime : " + item.getStartDateTime());
        }
        taskBox.put(taskItems);
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
