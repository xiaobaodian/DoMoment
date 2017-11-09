package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class CustomCategory extends CategoryBase {

    CustomCategory(String title, int ID) {
        super();
        setTitle(title);
        setID(ID);
        categoryType = CategoryType.Custom;
        themeBackground = R.drawable.todo_themebackground_work1;
        BuildGroupLists();
    }

    @Override
    public boolean InCategory(Task task) {
        return task.getCategoryID() == getID();
    }

    @Override
    protected void BuildGroupLists() {

        AddGroupList(new TimeLineGroupList(this));
        AddGroupList(new OverdueGroupList(this));
        AddGroupList(new NoDateGroupList(this));
        AddGroupList(new MakeOutGroupList(this));
    }

}
