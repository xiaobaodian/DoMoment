package threecats.zhang.domoment.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import threecats.zhang.domoment.layout.TitleFragment;

/**
 * 由 zhang 于 2017/7/31 创建
 */

public class TaskFragmentAdapter extends FragmentPagerAdapter {

    private List<TitleFragment> fragmentList;

    public TaskFragmentAdapter(FragmentManager fragmentManager, Context context){
        super(fragmentManager);
    }
    public TaskFragmentAdapter(FragmentManager fragmentManager, List<TitleFragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getTitle();
    }
}
