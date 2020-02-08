package com.Example.cashcounter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.widget.ProgressBar;

import com.Example.cashcounter.Activity.ActivityStartUp;
import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Activity.ActivityScreenAddCurrency;
import com.Example.cashcounter.Activity.ActivityCurrencySelect;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.DatabaseUntils;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;

public class ActivitySplashScreen extends AppCompatActivity {
    int REQUEST_PERMISSIONS = 1;
    private View btnLogin;
    SQLiteDatabase db;
    private TextInputLayout inputCompanyname;
    private TextInputLayout inputMobile;
    private TextInputLayout inputUsername;
    Database mDbHelper;
    String[] permissions = new String[]{"android.permission.READ_PHONE_STATE"};
    private ProgressBar prd;
    ProgressDialog prdDialog;
    int revealX;
    int revealY;
    SharedPreferences sharedPreferences;
    private View viewBtnLogin;
    private View viewLogin;
    private View viewLogo;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_screen_splash);

        this.mDbHelper = new Database(this);
        this.sharedPreferences = SharePref.getSharePref(this);
//        MobileAds.initialize(this, getString(R.string.ads_id));
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_black));
        }
        this.prd = (ProgressBar) findViewById(R.id.prd_splash);
        this.viewLogo = findViewById(R.id.view_splash);
        this.viewLogin = findViewById(R.id.view_splash_login);
        this.viewBtnLogin = findViewById(R.id.view_btn_login);
        this.inputUsername = (TextInputLayout) findViewById(R.id.input_login_username);
        this.inputMobile = (TextInputLayout) findViewById(R.id.input_login_mobile);
        this.inputCompanyname = (TextInputLayout) findViewById(R.id.input_login_company_name);
        this.btnLogin = findViewById(R.id.btn_login_confirm);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (VERSION.SDK_INT < 23) {
                    ActivitySplashScreen.this.getSubDomain();
                } else if (ActivitySplashScreen.this.checkPermissionStatus()) {
                    ActivitySplashScreen.this.requestPermissions(ActivitySplashScreen.this.permissions, ActivitySplashScreen.this.REQUEST_PERMISSIONS);
                } else {
                    ActivitySplashScreen.this.getSubDomain();
                }
            }
        }, 1000);
        this.viewBtnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivitySplashScreen.this.viewBtnLogin.setEnabled(false);
                /*ActivitySplashScreen.this.revealActivity(ActivitySplashScreen.this.viewBtnLogin);*/
            }
        });
        this.btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

            }
        });
        this.inputUsername.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ActivitySplashScreen.this.inputUsername.setErrorEnabled(false);
            }
        });
        this.inputMobile.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ActivitySplashScreen.this.inputMobile.setErrorEnabled(false);
            }
        });
        this.inputCompanyname.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ActivitySplashScreen.this.inputCompanyname.setErrorEnabled(false);
            }
        });
    }

 /*   public void onLoginBtnClick() {
        if (this.inputUsername.getEditText().getText().toString().trim().isEmpty()) {
            this.inputUsername.setErrorEnabled(true);
            this.inputUsername.setError("Enter user name");
        } else if (this.inputUsername.getEditText().getText().toString().trim().length() > 30) {
            this.inputUsername.setErrorEnabled(true);
            this.inputUsername.setError("Enter Valid user name");
        } else if (this.inputMobile.getEditText().getText().toString().trim().isEmpty()) {
            this.inputMobile.setErrorEnabled(true);
            this.inputMobile.setError("Enter mobile number");
        } else if (this.inputMobile.getEditText().getText().toString().length() <= 4 || this.inputMobile.getEditText().getText().toString().length() >= 12) {
            this.inputMobile.setErrorEnabled(true);
            this.inputMobile.setError("Enter valid mobile number");
        } else if (this.inputCompanyname.getEditText().getText().toString().trim().isEmpty()) {
            this.inputCompanyname.setErrorEnabled(true);
            this.inputCompanyname.setError("Enter company name");
        } else if (this.inputCompanyname.getEditText().getText().toString().length() > 100) {
            this.inputCompanyname.setErrorEnabled(true);
            this.inputCompanyname.setError("Enter valid company name");
        } else {
            showPrd();
            final String trim = this.inputUsername.getEditText().getText().toString().trim();
            final String trim2 = this.inputMobile.getEditText().getText().toString().trim();
            final String trim3 = this.inputCompanyname.getEditText().getText().toString().trim();
            String deviceImei = getDeviceImei();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(trim);
            stringBuilder.append("~");
            stringBuilder.append(trim2);
            stringBuilder.append("~");
            stringBuilder.append(deviceImei);
            stringBuilder.append("~");
            stringBuilder.append("jobcardandroid");
            deviceImei = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.sharedPreferences.getString(SharePref.cBaseDomain, "-"));
            stringBuilder.append(API.REGISTER_USER);
            String stringBuilder2 = stringBuilder.toString();
            RequestQueue newRequestQueue = Volley.newRequestQueue(this);
            HashMap hashMap = new HashMap();
            hashMap.put("pv", ENC.encrypttext(deviceImei));
            JsonObjectRequest anonymousClass9 = new JsonObjectRequest(1, stringBuilder2, new JSONObject(hashMap), new Listener<JSONObject>() {
                public void onResponse(JSONObject jSONObject) {
                    ActivitySplashScreen.this.hidePrd();
                    Helper.LOG("Response", jSONObject.toString());
                    try {
                        if (Integer.parseInt(ENC.decrypttext(jSONObject.getString("AppId"))) > 0) {
                            String decrypttext = ENC.decrypttext(jSONObject.getString("pid"));
                            String decrypttext2 = ENC.decrypttext(jSONObject.getString("AppId"));
                            Editor edit = ActivitySplashScreen.this.sharedPreferences.edit();
                            edit.putString(SharePref.uPid, decrypttext);
                            edit.putString(SharePref.uAppId, decrypttext2);
                            edit.putString(SharePref.uName, trim);
                            edit.putString(SharePref.uMobile, trim2);
                            edit.putString(SharePref.uCompanyName, trim3);
                            edit.putBoolean(SharePref.isRuningFirsttime, false);
                            edit.commit();
                            ActivitySplashScreen.this.db = ActivitySplashScreen.this.mDbHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Database.COL_P_NAME, Database.PURPOSE_CASHIN);
                            contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(1));
                            contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
                            contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                            ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
                            contentValues = new ContentValues();
                            contentValues.put(Database.COL_P_NAME, Database.PURPOSE_CASHOUT);
                            contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(2));
                            contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
                            contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                            ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
                            contentValues = new ContentValues();
                            contentValues.put(Database.COL_P_NAME, Database.PURPOSE_SALES);
                            contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(3));
                            contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
                            contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                            ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
                            contentValues = new ContentValues();
                            contentValues.put(Database.COL_P_NAME, Database.PURPOSE_EXCHANGE);
                            contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(4));
                            contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
                            contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                            ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
                            ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this, ActivityCurrencySelect.class));
                            ActivitySplashScreen.this.finish();
                            return;
                        }
                        ActivitySplashScreen.this.onResponseFail();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ActivitySplashScreen.this.onResponseFail();
                    }
                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    ActivitySplashScreen.this.hidePrd();
                    ActivitySplashScreen.this.onResponseFail();
                    Log.e("Response", "Error");
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap hashMap = new HashMap();
                    hashMap.put("Content-Type", "application/json; charset=utf-8");
                    return hashMap;
                }
            };
            anonymousClass9.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1.0f));
            newRequestQueue.add(anonymousClass9);
        }
    }*/


  /*  public void revealActivity(View view) {
        if (VERSION.SDK_INT >= 21) {
            this.revealX = (int) (view.getX() + ((float) (view.getWidth() / 2)));
            this.revealY = (int) (view.getY() + ((float) (view.getHeight() / 2)));
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.viewLogin, this.revealX, this.revealY, 0.0f, (float) (((double) Math.max(this.viewLogin.getWidth(), this.viewLogin.getHeight())) * 1.1d));
            createCircularReveal.setDuration(400);
            createCircularReveal.setInterpolator(new AccelerateInterpolator());
            this.viewLogin.setVisibility(0);
            createCircularReveal.start();
            return;
        }
        this.viewLogin.setVisibility(0);
    }*/


    public void unRevealActivity() {
        if (VERSION.SDK_INT < 21) {
            this.viewLogin.setVisibility(8);
            return;
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.viewLogin, this.revealX, this.revealY, (float) (((double) Math.max(this.viewLogin.getWidth(), this.viewLogin.getHeight())) * 1.1d), 0.0f);
        createCircularReveal.setDuration(400);
        createCircularReveal.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ActivitySplashScreen.this.viewLogin.setVisibility(4);
                ActivitySplashScreen.this.inputUsername.setErrorEnabled(false);
                ActivitySplashScreen.this.inputMobile.setErrorEnabled(false);
                ActivitySplashScreen.this.inputCompanyname.setErrorEnabled(false);
                ActivitySplashScreen.this.inputUsername.getEditText().setText(null);
                ActivitySplashScreen.this.inputMobile.getEditText().setText(null);
                ActivitySplashScreen.this.inputCompanyname.getEditText().setText(null);
                ActivitySplashScreen.this.viewBtnLogin.setEnabled(true);
            }
        });
        createCircularReveal.start();
    }

    public void onLoginClose(View view) {
        Helper.hideSoftInput(this, this.inputMobile.getEditText());
        unRevealActivity();
    }

    public void getSubDomain() {


        ActivitySplashScreen.this.db = ActivitySplashScreen.this.mDbHelper.getReadableDatabase();


        ActivitySplashScreen.this.db = ActivitySplashScreen.this.mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.COL_P_NAME, Database.PURPOSE_CASHIN);
        contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(1));
        contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
        contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
        ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(Database.COL_P_NAME, Database.PURPOSE_CASHOUT);
        contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(2));
        contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
        contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
        ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(Database.COL_P_NAME, Database.PURPOSE_SALES);
        contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(3));
        contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
        contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
        ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
        contentValues = new ContentValues();
        contentValues.put(Database.COL_P_NAME, Database.PURPOSE_EXCHANGE);
        contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(4));
        contentValues.put(Database.COL_P_TYPE, Integer.valueOf(1));
        contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
        ActivitySplashScreen.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
        int count = ActivitySplashScreen.this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CURRENCY, Database.COL_C_STATUS, String.valueOf(1), null), null).getCount();
        if (ActivitySplashScreen.this.sharedPreferences.getString(SharePref.uCurrencyName, "").isEmpty()) {
            ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this, ActivityCurrencySelect.class));
        } else if (count == 0) {
            ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this, ActivityScreenAddCurrency.class));
        } else {
            count = (int) (ActivitySplashScreen.this.btnLogin.getX() + ((float) (ActivitySplashScreen.this.btnLogin.getWidth() / 2)));
            int y = (int) (ActivitySplashScreen.this.btnLogin.getY() + ((float) (ActivitySplashScreen.this.btnLogin.getHeight() / 2)));
            Intent intent = new Intent(ActivitySplashScreen.this, ActivityStartUp.class);
            intent.putExtra("X_POINTS", count);
            intent.putExtra("Y_POINTS", y);
            ActivitySplashScreen.this.startActivity(intent);
        }
        /*this.prd.setVisibility(View.VISIBLE);
        Volley.newRequestQueue(this).add(new StringRequest(0, API.MAIN_URL, new Listener<String>() {
            public void onResponse(String str) {
                Helper.LOG("Response", str);

                Log.e("url",""+str);

                ActivitySplashScreen.this.prd.setVisibility(8);
                Editor edit = ActivitySplashScreen.this.sharedPreferences.edit();
                edit.putString(SharePref.cBaseDomain, str);
                edit.commit();
                if (ActivitySplashScreen.this.sharedPreferences.getBoolean(SharePref.isRuningFirsttime, true)) {
                    ActivitySplashScreen.this.viewBtnLogin.setVisibility(0);
                    return;
                }
                ActivitySplashScreen.this.db = ActivitySplashScreen.this.mDbHelper.getReadableDatabase();
                int count = ActivitySplashScreen.this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CURRENCY, Database.COL_C_STATUS, String.valueOf(1), null), null).getCount();
                if (ActivitySplashScreen.this.sharedPreferences.getString(SharePref.uCurrencyName, "").isEmpty()) {
                    ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this, ActivityCurrencySelect.class));
                } else if (count == 0) {
                    ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this, ActivityScreenAddCurrency.class));
                } else {
                    count = (int) (ActivitySplashScreen.this.btnLogin.getX() + ((float) (ActivitySplashScreen.this.btnLogin.getWidth() / 2)));
                    int y = (int) (ActivitySplashScreen.this.btnLogin.getY() + ((float) (ActivitySplashScreen.this.btnLogin.getHeight() / 2)));
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityStartUp.class);
                    intent.putExtra("X_POINTS", count);
                    intent.putExtra("Y_POINTS", y);
                    ActivitySplashScreen.this.startActivity(intent);
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                ActivitySplashScreen.this.prd.setVisibility(8);
                ActivitySplashScreen.this.onResponseFail();
            }
        }));*/
    }

    /*public void onResponseFail() {
        new AlertDialog.Builder(this).setCancelable(false).setMessage((CharSequence) "Fail to connect!\nPlease check your internet connection.").setPositiveButton((CharSequence) "Try again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivitySplashScreen.this.getSubDomain();
            }
        }).setNeutralButton((CharSequence) "Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivitySplashScreen.this.finish();
            }
        }).show();
    }*/

    public void showPrd() {
        this.prdDialog = new ProgressDialog(this);
        this.prdDialog.setMessage("Please wait...");
        this.prdDialog.show();
    }

    public void hidePrd() {
        this.prdDialog.dismiss();
    }

    private String getDeviceImei() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            return null;
        }
        return telephonyManager.getDeviceId();
    }

    @TargetApi(23)
    public boolean checkPermissionStatus() {
        for (String checkSelfPermission : this.permissions) {
            if (checkSelfPermission(checkSelfPermission) == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        Object obj = null;
        for (int i2 : iArr) {
            if (i2 == -1) {
                obj = 1;
            }
        }
        if (obj != null) {
            new AlertDialog.Builder(this).setMessage((CharSequence) "CashCounter was't start because some permission are denied.\nTry again to allow the permission").setPositiveButton((CharSequence) "Try again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (VERSION.SDK_INT >= 23) {
                        ActivitySplashScreen.this.requestPermissions(ActivitySplashScreen.this.permissions, ActivitySplashScreen.this.REQUEST_PERMISSIONS);
                    }
                }
            }).setNeutralButton((CharSequence) "Later", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivitySplashScreen.this.finish();
                }
            }).show();
        } else {
            getSubDomain();
        }
    }
}
