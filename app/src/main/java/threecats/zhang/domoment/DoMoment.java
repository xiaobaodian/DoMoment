package threecats.zhang.domoment;

import android.app.Application;
import android.content.Context;

import DataStructures.CategoryBase;
import DataStructures.DataManger;

/**
 * Created by zhang on 2017/8/14.
 */

public class DoMoment extends Application {

    private static Context context;
    private static DateTimeHelper dateTime;
    private static DataManger dataManger;
    private static MainActivity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dateTime = new DateTimeHelper();
        dataManger = new DataManger();
    }

    public static DateTimeHelper getDateTime(){
        return dateTime;
    }
    public static Context getContext() {
        return context;
    }
    public static DataManger getDataManger(){
        return dataManger;
    }
    public static CategoryBase getCurrentCategory(){
        return dataManger.getCurrentCategory();
    }

    public static void setMainActivity(MainActivity activity){
        mainActivity = activity;
    }
    public static MainActivity getMainActivity(){
        return mainActivity;
    }
    public static String getRString(int StringID){
        return DoMoment.getContext().getString(StringID);
    }
}
