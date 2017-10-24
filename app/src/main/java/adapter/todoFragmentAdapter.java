package adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import layout.TitleFragment;
import layout.ViewFragment;

/**
 * Created by zhang on 2017/7/31.
 */

public class todoFragmentAdapter extends FragmentPagerAdapter {

    List<TitleFragment> fragmentList;

    public todoFragmentAdapter(FragmentManager fm, Context context){
        super(fm);
    }
    public todoFragmentAdapter(FragmentManager fm, List<TitleFragment> fragmentList){
        super(fm);
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
        TitleFragment fragment = fragmentList.get(position);
        return fragment.getTitle();
    }
}
