package threecats.zhang.domoment.DataStructures;

import java.util.ArrayList;
import java.util.List;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.R;

/**
 * 由 zhang 于 2017/10/27 创建
 */

public class CategoryList {
    private List<CategoryBase> Categorys = new ArrayList<>();
    private List<BackgroundBase> categoryThemebackground = new ArrayList<>();
    //private List<CustomCategory> customCategories;

    public CategoryList(){
        Categorys.add(new AllTasksCategory());
        Categorys.add(new NoCategory());
        Categorys.add(new TaskPriorityCategory());

        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_0, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_1, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_2, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_3, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_4, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_5, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_6, "title"));
        categoryThemebackground.add(new BackgroundBase(R.drawable.category_themebackground_7, "title"));
    }

    public CategoryBase getFirstCategory(){
        return Categorys.get(0);
    }
    public GroupListBase getFirstGroupList(){
        return Categorys.get(0).getGroupLists().get(0);
    }
    public boolean IsNull(){
        return Categorys.size() == 3 && getTaskCount() == 0;
    }
    public int getTaskCount(){
        int count = 0;
        for (CategoryBase category : Categorys) {
            count += category.getTaskCount();
        }
        return count;
    }
    public int getUsabilityID(){
        boolean find = true;
        int ID = 0;
        int count = Categorys.size();
        for (int d = 10; d < 1000; d++) {
            find = true;
            for (int i = 3 ; i < count ; i++){
                CategoryBase category = Categorys.get(i);
                if (category.getCategoryID() == d) {
                    find = false;
                    break;
                }
            }
            if (find) {
                ID = d;
                break;
            }
        }
        return ID;
    }

    public String getCategoryTitle(int site){
        String title = "";
        for (CategoryBase category : Categorys) {
            if (category.getCategoryID() == site) {
                title = category.getTitle();
                break;
            }
        }
        return title;
    }

    public void AddCustomCategory(CustomCategory customCategory){
        Categorys.add(customCategory);
        //customCategories.add(customCategory);
    }

    public void RemoveCustomCategory(CustomCategory customCategory){
        List<TaskItem> removeTasks = customCategory.getAllTasks();
        for (TaskItem task : removeTasks) {
            RemoveTask(task);
            //恢复原始的分类ID，这很重要，不然AddTask的时候就丢失了
            task.setCategoryID(1);
        }
        Categorys.remove(customCategory);
        //App.getDataManger().setCurrentCategory(getFirstCategory());
        for (TaskItem task : removeTasks) {
            AddTask(task);
            App.getDataManger().UpdateTask(task);
        }
//        for (Task task : removeTasks) {
//            App.getDataManger().UpdateTask(task);
//        }
    }

    public List<CategoryBase> getCategorys(){
        return Categorys;
    }

    public List<CategoryBase> getCustomCategories(){
        return Categorys.subList(3, Categorys.size());
    }

    public void AddTask(TaskItem task){
        for (CategoryBase category : Categorys) {
            if (category.InCategory(task)) category.AddTask(task);
        }
    }

    public void RemoveTask(TaskItem task){
        // 删除task需要从GroupList里面进行，因为需要维护GroupList的显示列表itemlist
        List<GroupBase> groups = new ArrayList<>();
        groups.addAll(task.getParentGroups());
        for (GroupBase group : groups) {
            GroupListBase groupList = group.getParent();
            groupList.RemoveTask(group, task);
        }
    }

    public void ChangeTask(TaskItem task){
        List<CategoryBase> updateCategorys = new ArrayList<>();
        List<GroupBase> changeGroups = new ArrayList<>();
        List<GroupBase> updateGroups = new ArrayList<>();
        for (GroupBase group : task.getParentGroups()) {
            GroupListBase groupList = group.getParent();
            CategoryBase category = groupList.getParent();
            if (category.InCategory(task) && groupList.InGroupList(task) && group.InGroup(task)){
                if (group.needChangedPosition(task)) {
                    changeGroups.add(group);
                } else {
                    updateGroups.add(group);
                    updateCategorys.add(category);
                }
            } else {
                changeGroups.add(group);
            }
        }
        if (changeGroups.size() > 0) {
            //从需要改变的groups里面移除task
            for (GroupBase group : changeGroups) {
                GroupListBase groupList = group.getParent();
                groupList.RemoveTask(group, task);
            }
            //将task重新加入到Categroy里面，但排除只是需要更新的Category(在更新Category里面不需要移动task的顺序)
            for (CategoryBase category : Categorys) {
                if (updateCategorys.contains(category)) continue;
                category.AddTask(task);
            }
        }
        if (updateGroups.size() > 0) {
            for (GroupBase group : updateGroups) {
                GroupListBase groupList = group.getParent();
                //groupList.SortGroup(group);
                groupList.UpdateTaskDisplay(group, task);
            }
        }
    }

    public void UpdateTaskaa(Task task){
        for (GroupBase group : task.getParentGroup()) {
            GroupListBase groupList = group.getParent();
            groupList.SortGroup(group);
        }
    }

    public List<BackgroundBase> getCategoryThemebackgrounds(){
        return categoryThemebackground;
    }

    public void CurrentDateChange(){
        List<TaskItem> dateChangeTasks = new ArrayList<>();
        for (CategoryBase category : Categorys){
            for (GroupListBase gorupList : category.getGroupLists()) {
                gorupList.BuildTimePoint();
                for (GroupBase group : gorupList.getGroups()){
                    group.BuildTimePoint();
                }
                gorupList.CheckArea();
                gorupList.UpdateTitleMessage();
            }
        }
        for (CategoryBase category : Categorys){
            for (GroupListBase gorupList : category.getGroupLists()) {
                for (GroupBase group : gorupList.getGroups()){
                    for (TaskItem task : group.getTasks()) {
                        if ( ! group.InGroup(task)) dateChangeTasks.add(task);
                    }
                }
            }
        }
        if (dateChangeTasks.size() > 0) {
            for (TaskItem task : dateChangeTasks) {
                ChangeTask(task);
            }
        }
        dateChangeTasks.clear();
    }

}
