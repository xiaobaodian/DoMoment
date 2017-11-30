package threecats.zhang.domoment.DataStructures;

import android.content.Context;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class AllTasksCategory extends CategoryBase {

    AllTasksCategory() {
        super();
        setCategoryID(0);
        Context context = App.getContext();
        setTitle(context.getString(R.string.category_alltask_title));
        categoryType = CategoryType.System;
        iconId = R.mipmap.folder;
        themeBackgroundID = R.drawable.category_themebackground_0;
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
        AddGroupList(new MakeOutGroupList(this));
    }

}
