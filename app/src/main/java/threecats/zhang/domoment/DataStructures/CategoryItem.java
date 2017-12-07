package threecats.zhang.domoment.DataStructures;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.R;

/**
 * 由 zhang 于 2017/12/6 创建
 */

@Entity
public class CategoryItem {

    @Id
    long id;

    private String title;
    private String note;
    private int orderID;
    private int categoryID;
    private int iconId;
    private int themeBackgroundID;
    private int themeColorID;

    public CategoryItem(){
    }
    public CategoryItem(String title, int categoryID){
        this.title = title;
        this.categoryID = categoryID;
        iconId = R.mipmap.list;
        themeBackgroundID = R.drawable.category_themebackground_1;
    }

    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setNote(String note){
        this.note = note;
    }
    public String getNote(){
        return note;
    }

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }
    public int getOrderID(){
        return orderID;
    }

    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }
    public int getCategoryID(){
        return categoryID;
    }

    public void setIconId(int iconId){
        this.iconId = iconId;
    }
    public int getIconId(){
        return iconId;
    }

    public void setThemeBackgroundID(int themeBackgroundID){
        this.themeBackgroundID = themeBackgroundID;
    }
    public int getThemeBackgroundID(){
        return themeBackgroundID;
    }

    public void setThemeColorID(int themeColorID){
        this.themeColorID = themeColorID;
    }
    public int getThemeColorID(){
        return themeColorID;
    }

}
