package DataStructures;

import android.content.Context;

import ENUM.CategoryType;
import threecats.zhang.domoment.DoMoment;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class CustomCategory extends CategoryBase {
    private Context context = DoMoment.getContext();

    public CustomCategory(String title) {
        super();
        setID(2);
        setTitle(title);
        categoryType = CategoryType.Custom;
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
