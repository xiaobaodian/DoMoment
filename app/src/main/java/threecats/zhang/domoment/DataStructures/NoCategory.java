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
        Context context = App.self().getContext();
        setTitle(context.getString(R.string.category_nocategory_title));
        categoryType = CategoryType.System;
        iconId = R.mipmap.brightness;
        themeBackgroundID = R.drawable.category_themebackground_1;
        BuildGroupLists();
    }

    @Override
    public boolean inCategory(TaskItem task) {
        return task.getCategoryID() == getCategoryID();
    }

    @Override
    protected void BuildGroupLists() {

        addGroupList(new TimeLineGroupList(this));
        addGroupList(new OverdueGroupList(this));
        addGroupList(new NoDateGroupList(this));
        addGroupList(new MakeOutGroupList(this));
    }

}
