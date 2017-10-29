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
        allCategorys.add(new PriorityCategory());
    }

    public CategoryBase getFirstCategory(){
        return allCategorys.get(0);
    }

    public String getCategoryTitle(int site){
        String title = "";
        for (CategoryBase category : allCategorys) {
            if (category.getID() == site) {
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

    public void RemoveTask(Task task){
        for (GroupBase group : task.getParentGroups()) {
            GroupListBase groupList = group.getParent();
            groupList.RemoveTask(group, task);
        }
        task.getParentGroups().clear();
    }

    public void ChangeTask(Task task){
        RemoveTask(task);
        InsertTask(task);
    }

    public void UpdateTask(Task task){
        for (GroupBase group : task.getParentGroups()) {
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
                RemoveTask(task);
                AddTask(task);
            }
        }
        dateChangeTasks.clear();
    }

    public void BindDatas(){
        for (CategoryBase category : allCategorys) {
            for (GroupListBase groupList : category.getGroupLists()) {
                groupList.BindDatas();
            }
        }
    }
}
