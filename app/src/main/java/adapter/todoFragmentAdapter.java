package adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import layout.ViewFragment;

/**
 * Created by zhang on 2017/7/31.
 */

public class todoFragmentAdapter extends FragmentPagerAdapter {

    List<ViewFragment> fragmentList;
    List<String> titleList;

    public todoFragmentAdapter(FragmentManager fm, Context context){
        super(fm);
    }
    public todoFragmentAdapter(FragmentManager fm, List<ViewFragment> fragmentList, List<String> titleList){
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
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
        ViewFragment item = fragmentList.get(position);
        String t = item.getTitle();
        //return titleList.get(position);
        //return fragmentList.get(position).getTitle();
        return "aaa";
    }
}
