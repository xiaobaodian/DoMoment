package DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/10/27.
 */

public class CategoryList {
    private List<CategoryBase> allCategorys = new ArrayList<>();
    //private List<CustomCategory> customCategories;

    public CategoryList(){
        allCategorys.add(new AllTasksCategory());
        allCategorys.add(new NoCategory());
        allCategorys.add(new TaskPriorityCategory());
    }

    public CategoryBase getFirstCategory(){
        return allCategorys.get(0);
    }
    public GroupListBase getFirstGroupList(){
        return allCategorys.get(0).getGroupLists().get(0);
    }
    public boolean IsNull(){
        if (allCategorys.size() == 3 && getTaskCount() == 0) return true;
        return false;
    }
    public int getTaskCount(){
        int count = 0;
        for (CategoryBase category : allCategorys) {
            count += category.getTaskCount();
        }
        return count;
    }

    public String getCategoryTitle(int site){
        String title = "";
        for (CategoryBase category : allCategorys) {
            if (category.getCategoryID() == site) {
                title = category.getTitle();
                break;
            }
        }
        return title;
    }

    public void AddCustomCategory(CustomCategory customCategory){
        allCategorys.add(customCategory);
        //customCategories.add(customCategory);
    }

    public void RemoveCustomCategory(CustomCategory customCategory){
        allCategorys.remove(customCategory);
    }

    public List<CategoryBase> getAllCategorys(){
        return  allCategorys;
    }

    public List<CategoryBase> getCustomCategories(){
        return allCategorys.subList(3, allCategorys.size());
    }

    public void AddTask(Task task){
        for (CategoryBase category : allCategorys) {
            if (category.InCategory(task)) category.AddTask(task);
        }
    }

    public void InsertTask(Task task){
        for (CategoryBase category : allCategorys) {
            if (category.InCategory(task)) category.InsertTask(task);
        }
    }

    public void InsertTask(Task task, List<CategoryBase> Categorys){
        for (CategoryBase category : Categorys) {
            if (category.InCategory(task)) category.AddTask(task);
        }
    }

    public void RemoveTask(Task task){
        // 删除task需要从GroupList里面进行，因为需要维护GroupList的显示列表itemlist
        List<GroupBase> groups = new ArrayList<>();
        for (GroupBase group : task.getParentGroup()) {
            groups.add(group);
        }
        for (GroupBase group : groups) {
            GroupListBase groupList = group.getParent();
            groupList.RemoveTask(group, task);
        }
    }

    public void ChangeTask(Task task){
        List<CategoryBase> updateCategorys = new ArrayList<>();
        List<GroupBase> changeGroups = new ArrayList<>();
        List<GroupBase> updateGroups = new ArrayList<>();
        for (GroupBase group : task.getParentGroup()) {
            GroupListBase groupList = group.getParent();
            CategoryBase category = groupList.getParent();
            if (category.InCategory(task) && groupList.InGroupList(task) && group.InGroup(task)){
                updateGroups.add(group);
                updateCategorys.add(category);
            } else {
                changeGroups.add(group);
            }
        }
        for (GroupBase group : changeGroups) {
            GroupListBase groupList = group.getParent();
            groupList.RemoveTask(group, task);
        }
        for (CategoryBase category : allCategorys) {
            if (updateCategorys.contains(category)) continue;
            category.InsertTask(task);
        }
        for (GroupBase group : updateGroups) {
            GroupListBase groupList = group.getParent();
            groupList.SortGroup(group);
        }
    }

    public void UpdateTaskaa(Task task){
        for (GroupBase group : task.getParentGroup()) {
            GroupListBase groupList = group.getParent();
            groupList.SortGroup(group);
        }
    }

    public void CurrentDateChange(){
        List<Task> dateChangeTasks = new ArrayList<>();
        for (CategoryBase category : allCategorys){
            for (GroupListBase gorupList : category.getGroupLists()) {
                gorupList.BuildTimePoint();
                for (GroupBase group : gorupList.getGroups()){
                    group.BuildTimePoint();
                }
                gorupList.CheckArea();
                gorupList.UpdateTitleMessage();
            }
        }
        for (CategoryBase category : allCategorys){
            for (GroupListBase gorupList : category.getGroupLists()) {
                for (GroupBase group : gorupList.getGroups()){
                    for (Task task : group.getTasks()) {
                        if ( ! group.InGroup(task)) dateChangeTasks.add(task);
                    }
                }
            }
        }
        if (dateChangeTasks.size() > 0) {
            for (Task task : dateChangeTasks) {
                ChangeTask(task);
            }
        }
        dateChangeTasks.clear();
    }

}
