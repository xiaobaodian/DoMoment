package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import ENUM.TaskPriority;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class TaskPriorityCategory extends CategoryBase {

    TaskPriorityCategory() {
        super();
        setID(2);
        Context context = DoMoment.getContext();
        setTitle(context.getString(R.string.category_prioritycategory_title));
        categoryType = CategoryType.System;
        themeBackground = R.drawable.todo_themebackground_work1;
        BuildGroupLists();
    }

    @Override
    public boolean InCategory(Task task) {
        return task.getPriority() != TaskPriority.None;
    }

    @Override
    protected void BuildGroupLists() {
        AddGroupList(new UrgentGroupList(this));
        AddGroupList(new VeryImportanceGroupList(this));
        AddGroupList(new ImportanceGroupList(this));
        AddGroupList(new MakeOutGroupList(this));
    }

}
