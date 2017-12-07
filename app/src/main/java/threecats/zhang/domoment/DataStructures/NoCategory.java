package threecats.zhang.domoment.DataStructures;

import android.content.Context;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class NoCategory extends CategoryBase {

    NoCategory() {
        super();
        setCategoryID(1);
        Context context = App.getContext();
        setTitle(context.getString(R.string.category_nocategory_title));
        categoryType = CategoryType.System;
        iconId = R.mipmap.brightness;
        themeBackgroundID = R.drawable.category_themebackground_1;
        BuildGroupLists();
    }

    @Override
    public boolean InCategory(TaskItem task) {
        return task.getCategoryID() == getCategoryID();
    }

    @Override
    protected void BuildGroupLists() {

        AddGroupList(new TimeLineGroupList(this));
        AddGroupList(new OverdueGroupList(this));
        AddGroupList(new NoDateGroupList(this));
        AddGroupList(new MakeOutGroupList(this));
    }

}
