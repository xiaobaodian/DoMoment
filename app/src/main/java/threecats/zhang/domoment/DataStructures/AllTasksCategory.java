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
        Context context = App.self().getContext();
        setTitle(context.getString(R.string.category_alltask_title));
        categoryType = CategoryType.System;
        iconId = R.mipmap.folder;
        themeBackgroundID = R.drawable.category_themebackground_0;
        BuildGroupLists();
    }

    @Override
    public boolean inCategory(TaskItem task) {
        return true;
    }

    @Override
    protected void BuildGroupLists() {
        addGroupList(new TimeLineGroupList(this));
        addGroupList(new OverdueGroupList(this));
        addGroupList(new NoDateGroupList(this));
        addGroupList(new MakeOutGroupList(this));
    }

}
