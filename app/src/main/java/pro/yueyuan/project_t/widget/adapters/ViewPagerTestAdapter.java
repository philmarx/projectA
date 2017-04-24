package pro.yueyuan.project_t.widget.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by xuq on 2017/4/21.
 */

public class ViewPagerTestAdapter extends FragmentPagerAdapter {

    public List<Fragment> list;
    private String[] titles;

    public ViewPagerTestAdapter(FragmentManager fm, Context context,
                                List<Fragment> fragmentList, String[] tabTitleArray) {
        super(fm);
        this.list = fragmentList;
        this.titles = tabTitleArray;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
