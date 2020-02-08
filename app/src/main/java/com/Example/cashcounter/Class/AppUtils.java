package com.Example.cashcounter.Class;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.Example.cashcounter.R;

public class AppUtils {


    public static void checkExprDate(Context context, View view) {
        if (SharePref.getSharePref(context).getBoolean(SharePref.uIsPackageExpr, true)) {
            view.setVisibility(8);
        } else {
            view.setVisibility(0);
        }
    }

    public static boolean getExprStatus(Context context) {
        return SharePref.getSharePref(context).getBoolean(SharePref.uIsPackageExpr, true);
    }

    public static void loadBottomAd(Context context, final LinearLayout linearLayout) {

        if (InternetConnection.checkConnection(context)) {

//            Banner(linearLayout, context);
        }

    }


//    public static void Banner(final LinearLayout Ad_Layout, final Context context) {
//
//        AdView mAdView = new AdView(context);
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(context.getString(R.string.ads_bnr));
//        com.google.android.gms.ads.AdRequest adre = new com.google.android.gms.ads.AdRequest.Builder().build();
//        mAdView.loadAd(adre);
//        Ad_Layout.addView(mAdView);
//
//        mAdView.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//                // TODO Auto-generated method stub
//                Ad_Layout.setVisibility(View.VISIBLE);
//                super.onAdLoaded();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // TODO Auto-generated method stub
//
//
//            }
//        });
//    }
}
