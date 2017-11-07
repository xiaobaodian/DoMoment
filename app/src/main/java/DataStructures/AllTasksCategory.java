package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class AllTasksCategory extends CategoryBase {

    AllTasksCategory() {
        super();
        setID(0);
        Context context = DoMoment.getContext();
        setTitle(context.getString(R.string.category_alltask_title));
        categoryType = CategoryType.System;
        themeBackground = R.drawable.todo_themebackground_work0;
        BuildGroupLists();
    }

    @Override
    public boolean InCategory(Task task) {
        return true;
    }

    @Override
    protected void BuildGroupLists() {
        AddGroupList(new TimeLineGroupList(this));
        AddGroupList(new OverdueGroupList(this));
        AddGroupList(new NoDateGroupList(this));
    }

}
