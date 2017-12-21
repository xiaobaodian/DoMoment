package threecats.zhang.domoment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.List;

import io.objectbox.BoxStore;
import threecats.zhang.domoment.DataStructures.BackgroundBase;
import threecats.zhang.domoment.DataStructures.CategoryBase;
import threecats.zhang.domoment.DataStructures.DataManger;
import threecats.zhang.domoment.DataStructures.GroupListBase;
import threecats.zhang.domoment.DataStructures.MyObjectBox;
import threecats.zhang.domoment.Helper.DateTimeHelper;

/**
 * Created by zhang on 2017/8/14.
 */

public class App extends Application {

    public static final String TAG = "IDoMoment";

    private static App self;
    private BoxStore boxStore;
    private Context context;
    private DataManger dataManger;
    private MainActivity mainActivity;
    private Activity currentActivity;
    private PopupWindow popupWindow;
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        context = getApplicationContext();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
        //dateTime = new DateTimeHelper();
        //dataManger = new DataManger();
        //dataManger.loadToDoDatas();
    }

    public void Init(){
        dataManger = new DataManger();
        //dataManger.loadToDoDatas();
    }

    public static App self(){
        return self;
    }

    public BoxStore getBoxStore(){
        return boxStore;
    }

    public Context getContext() {
        return context;
    }
    public DataManger getDataManger(){
        return dataManger;
    }
    public CategoryBase getCurrentCategory(){
        return dataManger.getCurrentCategory();
    }
    public GroupListBase getCurrentGroupList(){
        return dataManger.getCurrentGroupList();
    }

    public void setMainActivity(MainActivity activity){
        mainActivity = activity;
    }
    public MainActivity getMainActivity(){
        return mainActivity;
    }

    public void setCurrentActivity(Activity activity){
        currentActivity = activity;
    }
    public Activity getCurrentActivity(){
        return currentActivity;
    }

    public void showTaskDisplayActivity(){
        Intent taskIntent = new Intent(mainActivity,TaskDisplayActivity.class);
        mainActivity.startActivity(taskIntent);
    }

    public void setPopupWindow(PopupWindow pWin){
        popupWindow = pWin;
    }
    public boolean hasPopupWindow(){
        return popupWindow != null;
    }
    public void closePopupWindow(){
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    public void setDrawerMenu(DrawerLayout drawer){
        drawerLayout = drawer;
    }
    public void closeDrawerMenu(){
        if (drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout = null;
        }
    }
    public boolean hasDrawerMenu(){
        return drawerLayout != null;
    }

    public List<BackgroundBase> getCategoryThemebackgrounds(){
        return dataManger.getCategoryList().getCategoryThemebackgrounds();
    }


    public static void Exit() {
        self = null;
    }
}
