package threecats.zhang.domoment.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhang on 2017/9/2.
 */

public class SQLManger extends SQLiteOpenHelper {
    private Context context;
    private ContentValues values = new ContentValues();

    public static final String CreateTask = "create table Tasks ("
            + "id integer primary key autoincrement, "
            + "CategoryID int, "
            + "Title text, "
            + "Place text, "
            + "Note text, "
            + "Priority int, "
            + "CreateDateTime integer, "
            + "StartDateTime integer, "
            + "DueDateTime integer, "
            + "CompleteDateTime integer, "
            + "isAllDay boolean, "
            + "isNoDate boolean, "
            + "isComplete boolean)";

    public static final String CreateCategory = "create table Categorys ("
            + "id integer primary key autoincrement, "
            + "OrderID int, "
            + "CategoryID int, "
            + "Title text, "
            + "Note text, "
            + "iconID int, "
            + "themeBackgroundID int, "
            + "themeColorID int)";

    public SQLManger(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateCategory);
        db.execSQL(CreateTask);
        db.execSQL("create index Date_index on Tasks (StartDateTime, DueDateTime, CreateDateTime)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
