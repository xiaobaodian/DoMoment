package DataStructures;

import java.util.List;

/**
 * Created by zhang on 2017/10/27.
 */

public class CategoryList {
    private List<CategoryBase> categorys;

    public CategoryList(){
        categorys.add(new AllTasksCategory());
        categorys.add(new NoCategory());
        categorys.add(new PriorityCategory());
    }
}
