package com.Example.cashcounter.Adapters_Items;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class View_pager_adapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();
    private final List<String> mFragmentTitleList = new ArrayList();

    public View_pager_adapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        return (Fragment) this.mFragmentList.get(i);
    }

    public int getCount() {
        return this.mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String str) {
        this.mFragmentList.add(fragment);
        this.mFragmentTitleList.add(str);
    }

    public CharSequence getPageTitle(int i) {
        return (CharSequence) this.mFragmentTitleList.get(i);
    }
}
