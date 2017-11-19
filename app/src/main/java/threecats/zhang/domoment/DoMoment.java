package threecats.zhang.domoment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.List;

import DataStructures.BackgroupBase;
import DataStructures.CategoryBase;
import DataStructures.DataManger;
import DataStructures.GroupListBase;

/**
 * Created by zhang on 2017/8/14.
 */

public class DoMoment extends Application {

    private static Context context;
    private static DateTimeHelper dateTime;
    private static DataManger dataManger;
    private static MainActivity mainActivity;
    private static PopupWindow popupWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dateTime = new DateTimeHelper();
        dataManger = new DataManger();
        dataManger.LoadDatas();
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
    public static GroupListBase getCurrentGroupList(){
        return dataManger.getCurrentGroupList();
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
    public static void Toast(String message){
        android.widget.Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showTaskDisplayActivity(){
        Intent taskIntent = new Intent(DoMoment.getMainActivity(),TaskDisplayActivity.class);
        DoMoment.getMainActivity().startActivity(taskIntent);
    }
    public static void setPopupWindow(PopupWindow pWin){
        popupWindow = pWin;
    }
    public static boolean hasPopupWindow(){
        return popupWindow != null;
    }
    public static void closePopupWindow(){
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    public static List<BackgroupBase> getCategoryThemebackgrounds(){
        return dataManger.getCategoryList().getCategoryThemebackgrounds();
    }
}
