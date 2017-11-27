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

public class MainActivity extends AppCompatActivity {

    //public List<Task> taskList = new ArrayList<>();
    private Application app;
    private BottomNavigationView navigation;
    private TodoFragment todoFragment;
    private MomentFragment momentFragment;
    private FocusFragment focusFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoMoment.setMainActivity(this);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //恢复或旋转屏幕后进入原来的状态
        if (savedInstanceState != null) {
            navigation.setSelectedItemId(savedInstanceState.getInt("BottomNavigationView"));
        } else {
            //以后根据设置的参数来判断是首先进入哪一个活动（任务、回顾、关注）
            //mountFragment(momentFragment);
        }
        mountFragment(todoFragment);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (todoFragment == null) todoFragment = new TodoFragment();
        if (momentFragment == null) momentFragment = new MomentFragment();
        if (focusFragment == null) focusFragment = new FocusFragment();

        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setNavigationState(int visibility){
        //Animation alphaAnimation = new AlphaAnimation(1, (float) 0);
        //alphaAnimation.setDuration(300);       //设置动画持续时间为3秒
        //alphaAnimation.setFillEnabled(true);
        //alphaAnimation.setFillAfter(true);      //

        //Animation naviAnimatin = new TranslateAnimation(0,0,0,300);
        //naviAnimatin.setDuration(300);
        //naviAnimatin.setFillAfter(true);

        //navigation.startAnimation(naviAnimatin);

        navigation.setVisibility(visibility);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo:
                    mountFragment(todoFragment);
                    return true;
                case R.id.navigation_moment:
                    mountFragment(momentFragment);
                    return true;
                case R.id.navigation_focus:
                    mountFragment(focusFragment);
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
            if (DoMoment.hasPopupWindow()) {
                DoMoment.closePopupWindow();
                return true;
            }
            if (DoMoment.hasDrawerMenu()) {
                DoMoment.closeDrawerMenu();
                return true;
            }
            if ((System.currentTimeMillis()-mExitTime)>1000) {
                DoMoment.Toast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("BottomNavigationView", navigation.getSelectedItemId());
    }

}
