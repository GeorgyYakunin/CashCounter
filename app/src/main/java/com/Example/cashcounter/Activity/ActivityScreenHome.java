package com.Example.cashcounter.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.Example.cashcounter.Class.AppUtils;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.Fragments.CurrentSatusFragment;
import com.Example.cashcounter.Fragments.Dashboard;
import com.Example.cashcounter.Fragments.ManagePurposeFragment;
import com.Example.cashcounter.Fragments.ReportFragment;
import com.Example.cashcounter.R;

public class ActivityScreenHome extends AppCompatActivity {
    int backPressState = 0;
    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer;
    private int revealX;
    private int revealY;
    SharedPreferences sharedPreferences;
    private TextView txtTitle;
    private View viewDrawerStatus;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_screen_home);

        Log.e("homescreen","homescreen");

        this.sharedPreferences = SharePref.getSharePref(this);
        this.txtTitle = (TextView) findViewById(R.id.txt_home_title);
        this.viewDrawerStatus = findViewById(R.id.view_home_drawer_status);
        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        replaceFragment(new Dashboard());
        setNavigationHeader();
//        AppUtils.loadBottomAd(this, (LinearLayout) findViewById(R.id.banner_ad_view));
    }

    public void setNavigationHeader() {
        TextView textView = (TextView) findViewById(R.id.txt_nav_company_name);
        TextView textView2 = (TextView) findViewById(R.id.txt_nav_currency);
        ((TextView) findViewById(R.id.txt_nav_name)).setText(this.sharedPreferences.getString(SharePref.uName, "-"));
        textView.setText(this.sharedPreferences.getString(SharePref.uCompanyName, "-"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencyCode, "-").toUpperCase());
        stringBuilder.append(" (");
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, "-"));
        stringBuilder.append(")");
        textView2.setText(stringBuilder.toString());
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.frame_layout_home, fragment).commit();
    }

    public void onMoreClick(View view) {
        if (this.drawer.isDrawerOpen((int) GravityCompat.END)) {
            this.drawer.closeDrawer((int) GravityCompat.END);
        } else {
            this.drawer.openDrawer((int) GravityCompat.END);
        }
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", 0).show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ActivityScreenHome.this.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void onClockDrawerStatus(View view) {
        unRevealActivity();
    }

    public void onDrawerStatusClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_home_current_status, new CurrentSatusFragment()).commit();
        revealActivity(view);
    }


    public void revealActivity(View view) {
        if (VERSION.SDK_INT >= 21) {
            this.revealX = (int) (view.getX() + ((float) (view.getWidth() / 2)));
            this.revealY = (int) (view.getY() + ((float) (view.getHeight() / 2)));
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.viewDrawerStatus, this.revealX, this.revealY, 0.0f, (float) (((double) Math.max(this.viewDrawerStatus.getWidth(), this.viewDrawerStatus.getHeight())) * 1.1d));
            createCircularReveal.setDuration(400);
            createCircularReveal.setInterpolator(new AccelerateInterpolator());
            this.viewDrawerStatus.setVisibility(View.VISIBLE);
            createCircularReveal.start();
            return;
        }
        this.viewDrawerStatus.setVisibility(View.VISIBLE);
    }


    public void unRevealActivity() {
        if (VERSION.SDK_INT < 21) {
            this.viewDrawerStatus.setVisibility(View.GONE);
            return;
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.viewDrawerStatus, this.revealX, this.revealY, (float) (((double) Math.max(this.viewDrawerStatus.getWidth(), this.viewDrawerStatus.getHeight())) * 1.1d), 0.0f);
        createCircularReveal.setDuration(400);
        createCircularReveal.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ActivityScreenHome.this.viewDrawerStatus.setVisibility(4);
            }
        });
        createCircularReveal.start();
    }

    public void onNavigationItemClick(View view) {
        int id = view.getId();
        if (id == R.id.nav_header) {
            id = (int) (view.getX() + ((float) (view.getWidth() / 2)));
            int y = (int) (view.getY() + ((float) (view.getHeight() / 2)));
            Intent intent = new Intent(this, ActivityProfileSetting.class);
            intent.putExtra("X_POINTS", id);
            intent.putExtra("Y_POINTS", y);
            startActivityForResult(intent, 1);
            return;
        }
        if (id == R.id.nav_home) {
            this.drawer.closeDrawer((int) GravityCompat.END);
            this.txtTitle.setText("Home");
            this.backPressState = 1;
            if (this.viewDrawerStatus.isShown()) {
                unRevealActivity();
            }
            startActivity(new Intent(this, ActivityScreenHome.class));
            finish();
        }
        String packageName;
        StringBuilder stringBuilder;
        if (id == R.id.nav_report) {
            this.drawer.closeDrawer((int) GravityCompat.END);
            this.txtTitle.setText("Reports");
            this.backPressState = 1;
            if (this.viewDrawerStatus.isShown()) {
                unRevealActivity();
            }
            replaceFragment(new ReportFragment());
        } else if (id == R.id.nav_managepurpose) {
            this.drawer.closeDrawer((int) GravityCompat.END);
            this.txtTitle.setText("Manage Purpose");
            this.backPressState = 1;
            if (this.viewDrawerStatus.isShown()) {
                unRevealActivity();
            }
            replaceFragment(new ManagePurposeFragment());
        } else if (id == R.id.nav_shareapp) {
            packageName = getPackageName();
            Intent intent2 = new Intent();
            intent2.setAction("android.intent.action.SEND");
            stringBuilder = new StringBuilder();
            stringBuilder.append("Want to setup Cash Counter in your mobile? Download this app now! Manage denominations while you roam here & there.\n\nhttps://play.google.com/store/apps/details?id=");
            stringBuilder.append(packageName);
            intent2.putExtra("android.intent.extra.TEXT", stringBuilder.toString());
            intent2.setType("text/plain");
            startActivity(Intent.createChooser(intent2, "Share via"));
        } else if (id == R.id.nav_rateapp) {
            packageName = getPackageName();
            try {
                stringBuilder = new StringBuilder();
                stringBuilder.append("market://details?id=");
                stringBuilder.append(packageName);
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString())));
            } catch (ActivityNotFoundException unused) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("https://play.google.com/store/apps/details?id=");
                stringBuilder.append(packageName);
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString())));
            }
        } else if (id == R.id.nav_feedback) {
            String developerName = getString(R.string.moreapp);     //where geeks is the company name in the play store
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q="+developerName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q="+developerName+"&hl=en")));
            }
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        setNavigationHeader();
        if (i2 == -1) {
            replaceFragment(new Dashboard());
        }
    }
}
