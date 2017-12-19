package threecats.zhang.domoment.DataStructures;

import android.content.Context;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class TaskPriorityCategory extends CategoryBase {

    TaskPriorityCategory() {
        super();
        setCategoryID(2);
        Context context = App.getContext();
        setTitle(context.getString(R.string.category_prioritycategory_title));
        categoryType = CategoryType.System;
        iconId = R.mipmap.openbook;
        themeBackgroundID = R.drawable.category_themebackground_1;
        BuildGroupLists();
    }

    @Override
    public boolean inCategory(TaskItem task) {
        return task.getPriorityID() != TaskPriority.None.ordinal();
    }

    @Override
    protected void BuildGroupLists() {
        addGroupList(new UrgentGroupList(this));
        addGroupList(new VeryImportanceGroupList(this));
        addGroupList(new ImportanceGroupList(this));
        addGroupList(new FocusGroupList(this));
    }

}
