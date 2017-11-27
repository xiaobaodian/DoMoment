package threecats.zhang.domoment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.List;

import threecats.zhang.domoment.DataStructures.BackgroundBase;
import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.DataStructures.DataManger;
import threecats.zhang.domoment.DataStructures.GroupListBase;

/**
 * Created by zhang on 2017/8/14.
 */

public class DoMoment extends Application {

    private static Context context;
    private static DateTimeHelper dateTime;
    private static DataManger dataManger;
    private static MainActivity mainActivity;
    private static PopupWindow popupWindow;
    private static DrawerLayout drawerLayout;

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
    public static void setDrawerMenu(DrawerLayout drawer){
        drawerLayout = drawer;
    }
    public static void closeDrawerMenu(){
        if (drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout = null;
        }
    }
    public static boolean hasDrawerMenu(){
        return drawerLayout != null;
    }
    public static List<BackgroundBase> getCategoryThemebackgrounds(){
        return dataManger.getCategoryList().getCategoryThemebackgrounds();
    }
}
