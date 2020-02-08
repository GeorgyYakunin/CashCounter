package com.Example.cashcounter.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Class.AppUtils;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.DatabaseUntils;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;

public class ActivityProfileSetting extends AppCompatActivity {
    public static final String X_POINTS = "X_POINTS";
    public static final String Y_POINTS = "Y_POINTS";
    int REQUEST_CHANGE_CURRENCY = 1;
    int REQUEST_CHANGE_CURRENCY_LIST = 2;
    SQLiteDatabase db;
    Editor editor;
    public ImageView edtinfo;
    Database mDbHelper;
    private int revealX;
    private int revealY;
    View rootLayout;
    SharedPreferences sharedPreferences;
    private TextView txtAppId;
    private TextView txtCompanyName;
    private TextView txtCurrencyDetails;
    private TextView txtDuration;
    private TextView txtExptDate;
    private TextView txtMobileNo;
    private TextView txtName;
    private TextView txtPackageName;
    private TextView txtSelectedCurrency;
    private CardView viewAccountInfo;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_profile_setting);
        Intent intent = getIntent();
        this.edtinfo = (ImageView) findViewById(R.id.edtinfo);
        this.edtinfo.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityProfileSetting.this.UpdateInfo();
            }
        });
        this.viewAccountInfo = (CardView) findViewById(R.id.view_setting_account_info);
        this.rootLayout = findViewById(R.id.root_setting_layout);
        if (bundle == null && VERSION.SDK_INT >= 21 && intent.hasExtra("X_POINTS") && intent.hasExtra("Y_POINTS")) {
            this.rootLayout.setVisibility(4);
            this.revealX = intent.getIntExtra("X_POINTS", 0);
            this.revealY = intent.getIntExtra("Y_POINTS", 0);
            ViewTreeObserver viewTreeObserver = this.rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        ActivityProfileSetting.this.revealActivity(ActivityProfileSetting.this.revealX, ActivityProfileSetting.this.revealY);
                        ActivityProfileSetting.this.rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            this.rootLayout.setVisibility(0);
        }
        this.mDbHelper = new Database(this);
        this.sharedPreferences = SharePref.getSharePref(this);
        this.editor = this.sharedPreferences.edit();
        initV();
        this.txtName.setText(this.sharedPreferences.getString(SharePref.uName, ""));
        this.txtCompanyName.setText(this.sharedPreferences.getString(SharePref.uCompanyName, ""));
        this.txtMobileNo.setText(this.sharedPreferences.getString(SharePref.uMobile, ""));
        setCurrencyDetails();
        this.txtSelectedCurrency.setText(getSelectedCurrency());
        this.txtAppId.setText(this.sharedPreferences.getString(SharePref.uAppId, ""));
        this.txtPackageName.setText(this.sharedPreferences.getString(SharePref.uPackageName, ""));
        TextView textView = this.txtDuration;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uPackageDuration, ""));
        stringBuilder.append(" Months");
        textView.setText(stringBuilder.toString());
        this.txtExptDate.setText(Helper.changeDateFormate(this.sharedPreferences.getString(SharePref.uPackageExprDt, ""), Database.defaultDateFormate, "dd MMM yyyy"));
//        AppUtils.loadBottomAd(this, (LinearLayout) findViewById(R.id.banner_ad_view));
    }

    private void UpdateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.edit_acc_info, null);
        final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.acc_name_wrapper);
        final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R.id.contact_wrapper);
        EditText editText = (EditText) inflate.findViewById(R.id.editcontact);
        ((EditText) inflate.findViewById(R.id.editaccname)).setText(this.sharedPreferences.getString(SharePref.uName, ""));
        editText.setText(this.sharedPreferences.getString(SharePref.uCompanyName, ""));
        Button button = (Button) inflate.findViewById(R.id.btnsave);
        Button button2 = (Button) inflate.findViewById(R.id.btncancel);
        button.setText("EDIT");
        button.setTextSize(15.0f);
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (textInputLayout.getEditText().getText().toString().trim().isEmpty()) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Enter user name");
                } else if (textInputLayout.getEditText().getText().toString().trim().length() > 100) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Enter Valid user name");
                } else if (textInputLayout2.getEditText().getText().toString().trim().isEmpty()) {
                    textInputLayout2.setErrorEnabled(true);
                    textInputLayout2.setError("Enter company name");
                } else if (textInputLayout2.getEditText().getText().toString().length() > 100) {
                    textInputLayout2.setErrorEnabled(true);
                    textInputLayout2.setError("Enter valid company name");
                } else {
                    ActivityProfileSetting.this.editor.putString(SharePref.uName, textInputLayout.getEditText().getText().toString());
                    ActivityProfileSetting.this.editor.putString(SharePref.uCompanyName, textInputLayout2.getEditText().getText().toString());
                    ActivityProfileSetting.this.editor.commit();
                    ActivityProfileSetting.this.txtName.setText(ActivityProfileSetting.this.sharedPreferences.getString(SharePref.uName, ""));
                    ActivityProfileSetting.this.txtCompanyName.setText(ActivityProfileSetting.this.sharedPreferences.getString(SharePref.uCompanyName, ""));
                    create.dismiss();
                }
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
            }
        });
        create.show();
    }

    public void setCurrencyDetails() {
        String string = this.sharedPreferences.getString(SharePref.uCurrencyName, "");
        String string2 = this.sharedPreferences.getString(SharePref.uCurrencySymbol, "");
        String string3 = this.sharedPreferences.getString(SharePref.uCurrencyCode, "");
        TextView textView = this.txtCurrencyDetails;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string);
        stringBuilder.append(" - ");
        stringBuilder.append(string2);
        stringBuilder.append(" - ");
        stringBuilder.append(string3);
        textView.setText(stringBuilder.toString());
    }

    public String getSelectedCurrency() {
        String str = "";
        String string = this.sharedPreferences.getString(SharePref.uCurrencySymbol, "");
        this.db = this.mDbHelper.getReadableDatabase();
        int i = 1;
        Cursor rawQuery = this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CURRENCY, Database.COL_C_STATUS, String.valueOf(1), null), null);
        while (rawQuery.moveToNext()) {
            StringBuilder stringBuilder;
            if (i != 0) {
                i = 0;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(string);
                stringBuilder.append(rawQuery.getString(rawQuery.getColumnIndex(Database.COL_C_AMOUNT)));
                str = stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(", ");
                stringBuilder.append(string);
                stringBuilder.append(rawQuery.getString(rawQuery.getColumnIndex(Database.COL_C_AMOUNT)));
                str = stringBuilder.toString();
            }
        }
        return str;
    }

    public void initV() {
        this.txtName = (TextView) findViewById(R.id.txt_setting_name);
        this.txtCompanyName = (TextView) findViewById(R.id.txt_setting_company_name);
        this.txtMobileNo = (TextView) findViewById(R.id.txt_setting_mobile_no);
        this.txtCurrencyDetails = (TextView) findViewById(R.id.txt_setting_currency_details);
        this.txtSelectedCurrency = (TextView) findViewById(R.id.txt_setting_selected_currency);
        this.txtAppId = (TextView) findViewById(R.id.txt_setting_appid);
        this.txtPackageName = (TextView) findViewById(R.id.txt_setting_package);
        this.txtDuration = (TextView) findViewById(R.id.txt_setting_duraition);
        this.txtExptDate = (TextView) findViewById(R.id.txt_setting_expr_date);
    }


    public void revealActivity(int i, int i2) {
        if (VERSION.SDK_INT >= 21) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.rootLayout, i, i2, 0.0f, (float) (((double) Math.max(this.rootLayout.getWidth(), this.rootLayout.getHeight())) * 1.1d));
            createCircularReveal.setDuration(400);
            createCircularReveal.setInterpolator(new AccelerateInterpolator());
            this.rootLayout.setVisibility(0);
            createCircularReveal.start();
            return;
        }
        finish();
    }


    public void unRevealActivity() {
        if (VERSION.SDK_INT < 21) {
            finish();
            return;
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.rootLayout, this.revealX, this.revealY, (float) (((double) Math.max(this.rootLayout.getWidth(), this.rootLayout.getHeight())) * 1.1d), 0.0f);
        createCircularReveal.setDuration(400);
        createCircularReveal.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ActivityProfileSetting.this.rootLayout.setVisibility(View.INVISIBLE);
                ActivityProfileSetting.this.finish();
            }
        });
        createCircularReveal.start();
    }

    public void onBackPressed() {
        super.onBackPressed();
        unRevealActivity();
    }

    public void onCloseClick(View view) {
        this.revealX = (int) (view.getX() + ((float) (view.getWidth() / 2)));
        this.revealY = (int) (view.getY() + ((float) (view.getHeight() / 2)));
        unRevealActivity();
    }

    public void onChangeCurrencyClick(View view) {
        Intent intent = new Intent(this, ActivityCurrencySelect.class);
        intent.putExtra("type", true);
        startActivityForResult(intent, this.REQUEST_CHANGE_CURRENCY);
    }

    public void onChangeCurrencyListClick(View view) {
        startActivityForResult(new Intent(this, ActivityCurrencyListChange.class), this.REQUEST_CHANGE_CURRENCY_LIST);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            setResult(-1);
            if (i == this.REQUEST_CHANGE_CURRENCY) {
                setCurrencyDetails();
                this.txtSelectedCurrency.setText(getSelectedCurrency());
            } else if (i == this.REQUEST_CHANGE_CURRENCY_LIST) {
                this.txtSelectedCurrency.setText(getSelectedCurrency());
            }
        }
    }

    public void onFlushData(View view) {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Flush Data").setMessage((CharSequence) "Are you sure you want to flush all data?\n-it's will be remove all transactions?").setPositiveButton((CharSequence) "Flush", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityProfileSetting.this.db = ActivityProfileSetting.this.mDbHelper.getWritableDatabase();
                ActivityProfileSetting.this.db.delete(Database.TABLE_CASH_IN, null, null);
                ActivityProfileSetting.this.db.delete(Database.TABLE_CASH_OUT, null, null);
                ActivityProfileSetting.this.db.delete(Database.TABLE_EXCHANGE, null, null);
                ActivityProfileSetting.this.db.delete(Database.TABLE_SALES, null, null);
                ActivityProfileSetting.this.db.delete(Database.TABLE_TRANSACTION, null, null);
                new CToast(ActivityProfileSetting.this).simpleToast("All data has been flushed", 0).show();
            }
        }).setNegativeButton((CharSequence) "Cancel", null).show();
    }
}
