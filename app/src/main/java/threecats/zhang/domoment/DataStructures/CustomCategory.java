package threecats.zhang.domoment.DataStructures;

import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.R;

/**
 * 由 zhang 于 2017/8/21 创建
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
