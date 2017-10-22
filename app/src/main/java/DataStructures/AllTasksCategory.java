package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class AllTasksCategory extends CategoryBase {
    private Context context = DoMoment.getContext();

    public AllTasksCategory() {
        super();
        setID(0);
        setTitle(context.getString(R.string.category_alltask_title));
        categoryType = CategoryType.System;
        themeBackground = R.drawable.todo_themebackground_work0;
        BuildViewGroups();
    }

    @Override
    public boolean InCategory(Task task) {
        return true;
    }

    @Override
    protected void BuildViewGroups() {

        AddGroupList(new TimeLineGroupList(this));
        AddGroupList(new OverdueGroupList(this));
        AddGroupList(new NoDateGroupList(this));
    }

}
