package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class PriorityCategory extends CategoryBase {
    private Context context = DoMoment.getContext();

    public PriorityCategory() {
        super();
        setID(2);
        setTitle(context.getString(R.string.category_prioritycategory_title));
        categoryType = CategoryType.System;
        themeBackground = R.drawable.todo_themebackground_work1;
        BuildViewGroups();
    }

    @Override
    public boolean InCategory(Task task) {
        return task.getCategoryID() == getID();
    }

    @Override
    protected void BuildViewGroups() {

        AddGroupList(new UrgentGroupList(this));
        AddGroupList(new ImprotantGroupList(this));
        AddGroupList(new NoDateGroupList(this));
    }

}
