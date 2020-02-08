package com.Example.cashcounter.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.Example.cashcounter.Adapters_Items.View_pager_adapter;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.R;

public class ReportFragment extends Fragment {
    private TabLayout tabView;
    private ViewPager viewPager;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_report, viewGroup, false);
        this.tabView = (TabLayout) inflate.findViewById(R.id.tabview_report);
        this.viewPager = (ViewPager) inflate.findViewById(R.id.viewpager_report);
        setupViewPager();
        return inflate;
    }

    public void setupViewPager() {
        View_pager_adapter view_pager_adapter = new View_pager_adapter(getChildFragmentManager());
        view_pager_adapter.addFrag(getFragment(new ReportCashInFragment(), String.valueOf(1)), "Cash In");
        view_pager_adapter.addFrag(getFragment(new ReportCashInFragment(), String.valueOf(2)), "Cash Out");
        view_pager_adapter.addFrag(new ReportSalesFragment(), Database.PURPOSE_SALES);
        view_pager_adapter.addFrag(new ReportExchangeFragment(), "Exchange");
        this.viewPager.setAdapter(view_pager_adapter);
        this.tabView.setupWithViewPager(this.viewPager);
    }

    public Fragment getFragment(Fragment fragment, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        fragment.setArguments(bundle);
        return fragment;
    }
}
