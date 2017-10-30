package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class PriorityCategory extends CategoryBase {

    PriorityCategory() {
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
        return task.getCategoryID() == getID();
    }

    @Override
    protected void BuildGroupLists() {

        AddGroupList(new UrgentGroupList(this));
        AddGroupList(new VeryImprotantGroupList(this));
        AddGroupList(new ImprotantGroupList(this));
    }

}
