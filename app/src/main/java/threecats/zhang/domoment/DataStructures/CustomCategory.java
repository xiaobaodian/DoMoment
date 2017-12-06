package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.R;

/**
 * Created by zhang on 2017/8/21.
 */

public class CustomCategory extends CategoryBase {

    CustomCategory(){
        super();
        BuildGroupLists();
    }

    CustomCategory(String title, int categoryID) {
        super();
        setTitle(title);
        setCategoryID(categoryID);
        categoryType = CategoryType.Custom;
        iconId = R.mipmap.list;
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
