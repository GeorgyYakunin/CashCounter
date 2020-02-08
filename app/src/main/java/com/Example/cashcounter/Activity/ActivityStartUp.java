package com.Example.cashcounter.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;


import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;

public class ActivityStartUp extends AppCompatActivity {
    public static final String X_POINTS = "X_POINTS";
    public static final String Y_POINTS = "Y_POINTS";
    int REQUEST_CHANGE_CURRENCY = 1;
    int REQUEST_CHANGE_CURRENCY_LIST = 2;
    SQLiteDatabase db;
    Database mDbHelper;
    private ProgressBar prd;
    private int revealX;
    private int revealY;
    View rootLayout;
    SharedPreferences sharedPreferences;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_start_up);


        Log.e("ActivityStartUp", "ActivityStartUp");
        this.sharedPreferences = SharePref.getSharePref(this);
        this.mDbHelper = new Database(this);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        Intent intent = getIntent();
        this.rootLayout = findViewById(R.id.root_startup_layout);
        this.prd = (ProgressBar) findViewById(R.id.prd_start_all);
        this.prd.setVisibility(0);
        if (bundle == null && VERSION.SDK_INT >= 21 && intent.hasExtra("X_POINTS") && intent.hasExtra("Y_POINTS")) {
            this.rootLayout.setVisibility(4);
            this.revealX = intent.getIntExtra("X_POINTS", 0);
            this.revealY = intent.getIntExtra("Y_POINTS", 0);
            ViewTreeObserver viewTreeObserver = this.rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        ActivityStartUp.this.revealActivity(ActivityStartUp.this.revealX, ActivityStartUp.this.revealY);
                        ActivityStartUp.this.rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            this.rootLayout.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ActivityStartUp.this.next();
            }
        }, 2000);
    }


    public void revealActivity(int i, int i2) {
        if (VERSION.SDK_INT >= 21) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.rootLayout, i, i2, 0.0f, (float) (((double) Math.max(this.rootLayout.getWidth(), this.rootLayout.getHeight())) * 1.1d));
            createCircularReveal.setDuration(400);
            createCircularReveal.setInterpolator(new AccelerateInterpolator());
            this.rootLayout.setVisibility(View.VISIBLE);
            createCircularReveal.start();
            return;
        }
        finish();
    }


    private String getDeviceImei() {
        return ((TelephonyManager) getSystemService("phone")).getDeviceId();
    }

    public void next() {
        Intent intent = new Intent(this, ActivityScreenHome.class);
        intent.setFlags(268468224);
        startActivity(intent);
        finish();
    }
}
