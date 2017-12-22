package threecats.zhang.domoment;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import threecats.zhang.domoment.EventClass.InitEvent;
import threecats.zhang.domoment.Helper.UIHelper;

public class MainActivity extends AppCompatActivity {

    //public List<Task> taskList = new ArrayList<>();
    private Application app;
    private BottomNavigationView navigation;
    private View naviMask;
    private TodoFragment todoFragment;
    private MomentFragment momentFragment;
    private FocusFragment focusFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new InitEvent(1));

        if (App.self() == null) {
            App.setSelf((App)getApplication());
        }
        App.self().setMainActivity(this);

        setContentView(R.layout.activity_main);

        naviMask = (View) findViewById(R.id.ViewMask);
        naviMask.setOnClickListener(view -> closeNavigation());
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        closeNavigation();

        if (todoFragment == null) todoFragment = new TodoFragment();
        if (momentFragment == null) momentFragment = new MomentFragment();
        if (focusFragment == null) focusFragment = new FocusFragment();

        //恢复或旋转屏幕后进入原来的状态
        if (savedInstanceState != null) {
            navigation.setSelectedItemId(savedInstanceState.getInt("NavigationViewItemID"));
        } else {
            //以后根据设置的参数来判断是首先进入哪一个活动（任务、回顾、关注）
            mountFragment(todoFragment);
        }


    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.self().setCurrentActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void doInitApp(InitEvent initEvent){
        if (initEvent.getType() == 1) {
            App.self().init();
        }
    }

    public void showNavigation(){
        navigation.setVisibility(View.VISIBLE);
        naviMask.setVisibility(View.VISIBLE);
    }

    public void closeNavigation(){
        navigation.setVisibility(View.GONE);
        naviMask.setVisibility(View.GONE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo:
                    mountFragment(todoFragment);
                    closeNavigation();
                    return true;
                case R.id.navigation_moment:
                    mountFragment(momentFragment);
                    closeNavigation();
                    return true;
                case R.id.navigation_focus:
                    mountFragment(focusFragment);
                    closeNavigation();
                    return true;
            }
            return false;
        }

    };

    private void mountFragment(Fragment frament){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frament, frament);
        fragmentTransaction.commit();
    }

    public void setMainToolBar(Toolbar toolbar){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private double mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (App.self().hasPopupWindow()) {
                App.self().closePopupWindow();
                return true;
            }
            if (App.self().hasDrawerMenu()) {
                App.self().closeDrawerMenu();
                return true;
            }
            if (navigation.getVisibility() == View.VISIBLE) {
                this.closeNavigation();
                return true;
            }
            if ((System.currentTimeMillis()-mExitTime)>1000) {
                UIHelper.Toast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("NavigationViewItemID", navigation.getSelectedItemId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
