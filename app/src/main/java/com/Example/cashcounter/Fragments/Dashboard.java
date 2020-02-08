package com.Example.cashcounter.Fragments;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.Example.cashcounter.R;

public class Dashboard extends Fragment {
    public static final String CASH_IN_FRAG = "1";
    public static final String CASH_OUT_FRAG = "2";
    public static final String EXCHANGE_FRAG = "4";
    public static final String SALES_FRAG = "3";

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dashboard, viewGroup, false);
        replaceFragment(new CashInFragment(), CASH_IN_FRAG);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) inflate.findViewById(R.id.bottom_navigation_dash);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (bottomNavigationView.getSelectedItemId() == menuItem.getItemId()) {
                    return false;
                }
                if (menuItem.getItemId() == R.id.bnav_in) {
                    Dashboard.this.replaceFragment(new CashInFragment(), Dashboard.CASH_IN_FRAG);
                    return true;
                } else if (menuItem.getItemId() == R.id.bnav_out) {
                    Dashboard.this.replaceFragment(new CashOutFragment(), Dashboard.CASH_OUT_FRAG);
                    return true;
                } else if (menuItem.getItemId() == R.id.bnav_sales) {
                    Dashboard.this.replaceFragment(new SalesFragment(), Dashboard.SALES_FRAG);
                    return true;
                } else if (menuItem.getItemId() != R.id.bnav_exchange) {
                    return false;
                } else {
                    Dashboard.this.replaceFragment(new ExchangeFragment(), Dashboard.EXCHANGE_FRAG);
                    return true;
                }
            }
        });
        return inflate;
    }

    /* Access modifiers changed, original: 0000 */
    public void replaceFragment(Fragment fragment, String str) {
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.frame_layout_dashboard, fragment, str).commit();
    }
}
