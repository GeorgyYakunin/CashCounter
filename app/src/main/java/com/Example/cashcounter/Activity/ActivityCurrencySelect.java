package com.Example.cashcounter.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import com.google.android.gms.actions.SearchIntents;
import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Adapters_Items.CurrencyAdapter;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.CurrencyManager;
import com.Example.cashcounter.Class.CurrencyManager.Currency;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityCurrencySelect extends AppCompatActivity {
    ArrayList<Currency> arrayList = new ArrayList();
    private View btnAddNewClose;
    private TextView btnAddNewNext;
    private TextView btnNext;
    private TextInputLayout inputIsoCode;
    private TextInputLayout inputName;
    private TextInputLayout inputSymbol;
    boolean isForChange = false;
    private CurrencyAdapter mAdapter;
    private RecyclerView recyclerView;
    private int revealX;
    private int revealY;
    SharedPreferences sharedPreferences;
    private TextView txtCurrencyNext;
    private View viewAddNewCurrency;
    private View viewAddNewScrim;
    private View viewClose;
    private View viewNotFound;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_currency_select);

        Log.e("ActivityCurrencySelect","ActivityCurrencySelect");
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        this.isForChange = getIntent().getBooleanExtra("type", false);
        this.sharedPreferences = SharePref.getSharePref(this);
        initV();
        if (!getIntent().getBooleanExtra("firstTime", true)) {
            this.viewClose.setVisibility(8);
        }
        this.arrayList.addAll(new CurrencyManager().getCurrencyList());
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new CurrencyAdapter(this, this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        if (this.isForChange) {
            this.txtCurrencyNext.setText("Done");
            this.txtCurrencyNext.setCompoundDrawables(null, null, null, null);
            this.btnAddNewNext.setText("Done");
            this.btnAddNewNext.setCompoundDrawables(null, null, null, null);
            setDefaultSelectedCurrency();
        }
        this.btnNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.viewNotFound.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencySelect.this.revealActivity(ActivityCurrencySelect.this.viewAddNewCurrency);
            }
        });
        this.btnAddNewClose.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencySelect.this.unRevealActivity();
            }
        });
        this.viewAddNewScrim.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencySelect.this.unRevealActivity();
            }
        });
        this.btnAddNewNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencySelect.this.createNewCurrency();
            }
        });
        this.txtCurrencyNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencySelect.this.onCurrencySelect();
            }
        });
        this.inputName.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ActivityCurrencySelect.this.inputName.setErrorEnabled(false);
            }
        });
        this.inputIsoCode.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ActivityCurrencySelect.this.inputIsoCode.setErrorEnabled(false);
            }
        });
        this.inputSymbol.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ActivityCurrencySelect.this.inputSymbol.setErrorEnabled(false);
            }
        });
    }

    public void initV() {
        this.viewClose = findViewById(R.id.view_currency_close);
        this.viewAddNewCurrency = findViewById(R.id.view_currency_add_new_currency);
        this.viewNotFound = findViewById(R.id.txt_currency_not_found);
        this.viewAddNewScrim = findViewById(R.id.view_currency_add_new_scrim);
        this.btnAddNewClose = findViewById(R.id.view_currency_add_new_close);
        this.btnAddNewNext = (TextView) findViewById(R.id.view_currency_add_new_next);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview_currency);
        this.btnNext = (TextView) findViewById(R.id.txt_currency_next);
        this.inputName = (TextInputLayout) findViewById(R.id.input_currency_add_new_name);
        this.inputSymbol = (TextInputLayout) findViewById(R.id.input_currency_add_new_symbol);
        this.inputIsoCode = (TextInputLayout) findViewById(R.id.input_currency_add_new_iso_code);
        this.txtCurrencyNext = (TextView) findViewById(R.id.txt_currency_next);
    }

    public void setDefaultSelectedCurrency() {
        String string = this.sharedPreferences.getString(SharePref.uCurrencyCode, "");
        for (int i = 0; i < this.arrayList.size(); i++) {
            if (string.equals(((Currency) this.arrayList.get(i)).getCode())) {
                ((Currency) this.arrayList.get(i)).setSelected(true);
            } else {
                ((Currency) this.arrayList.get(i)).setSelected(false);
            }
        }
        this.mAdapter.notifyDataSetChanged();
    }


    public void revealActivity(View view) {
        if (VERSION.SDK_INT >= 21) {
            this.revealX = 0;
            this.revealY = (int) (view.getY() + ((float) view.getHeight()));
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.viewAddNewCurrency, this.revealX, this.revealY, 0.0f, (float) (((double) Math.max(this.viewAddNewCurrency.getWidth(), this.viewAddNewCurrency.getHeight())) * 1.1d));
            createCircularReveal.setDuration(400);
            createCircularReveal.setInterpolator(new AccelerateInterpolator());
            this.viewAddNewCurrency.setVisibility(0);
            createCircularReveal.start();
            return;
        }
        this.viewAddNewCurrency.setVisibility(0);
    }


    public void unRevealActivity() {
        Helper.hideSoftInput(this, this.inputName.getEditText());
        if (VERSION.SDK_INT < 21) {
            this.viewAddNewCurrency.setVisibility(8);
            return;
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.viewAddNewCurrency, this.revealX, this.revealY, (float) (((double) Math.max(this.viewAddNewCurrency.getWidth(), this.viewAddNewCurrency.getHeight())) * 1.1d), 0.0f);
        createCircularReveal.setDuration(400);
        createCircularReveal.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ActivityCurrencySelect.this.viewAddNewCurrency.setVisibility(4);
            }
        });
        createCircularReveal.start();
    }

    public void createNewCurrency() {
        if (this.inputName.getEditText().getText().toString().trim().isEmpty()) {
            this.inputName.setErrorEnabled(true);
            this.inputName.setError("Enter currency name - Eg.Indian Rupee");
            this.inputName.getEditText().requestFocus();
        } else if (this.inputSymbol.getEditText().getText().toString().trim().isEmpty()) {
            this.inputSymbol.setErrorEnabled(true);
            this.inputSymbol.setError("Enter symbol - Eg.₹ / Rs");
            this.inputSymbol.getEditText().requestFocus();
        } else if (this.inputIsoCode.getEditText().getText().toString().trim().isEmpty()) {
            this.inputIsoCode.setErrorEnabled(true);
            this.inputIsoCode.setError("Enter ISO code - Eg.INR / USD");
            this.inputIsoCode.getEditText().requestFocus();
        } else {
            Editor edit = this.sharedPreferences.edit();
            edit.putString(SharePref.uCurrencyName, this.inputName.getEditText().getText().toString().trim());
            edit.putString(SharePref.uCurrencyCode, this.inputIsoCode.getEditText().getText().toString().trim());
            edit.putString(SharePref.uCurrencySymbol, this.inputSymbol.getEditText().getText().toString().trim());
            edit.commit();
            startActivity(new Intent(this, ActivityScreenAddCurrency.class));
        }
    }

    public void onCurrencySelect() {
        Iterator it = this.arrayList.iterator();
        while (it.hasNext()) {
            Currency currency = (Currency) it.next();
            if (currency.isSelected()) {
                Editor edit = this.sharedPreferences.edit();
                edit.putString(SharePref.uCurrencyName, currency.getName());
                edit.putString(SharePref.uCurrencyCode, currency.getCode());
                edit.putString(SharePref.uCurrencySymbol, currency.getSymbol());
                edit.commit();
                if (this.isForChange) {
                    setResult(-1);
                    finish();
                } else {
                    startActivity(new Intent(this, ActivityScreenAddCurrency.class));
                }
            }
        }
    }

    public void findSymbolOnWeb(View view) {
        if (this.inputName.getEditText().getText().toString().trim().isEmpty()) {
            this.inputName.setErrorEnabled(true);
            this.inputName.setError("Enter currency name");
            return;
        }
        Intent intent = new Intent("android.intent.action.WEB_SEARCH");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.inputName.getEditText().getText().toString().trim());
        stringBuilder.append(" currency symbol wikipedia");
        intent.putExtra(SearchIntents.EXTRA_QUERY, stringBuilder.toString());
        startActivity(intent);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                new CToast(ActivityCurrencySelect.this).simpleToast(Html.fromHtml("Open first link wikipedia and then copy <b>symbol</b> and <b>code</b> from wikipedia"), 1).setGravityCenter().show();
            }
        }, 3000);
    }

    public void onCloseClick(View view) {
        finish();
    }
}
