package com.Example.cashcounter.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.CurrencyManager;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.util.ArrayList;

public class ActivityScreenAddCurrency extends AppCompatActivity {
    ArrayList<CurrencyList> currencyList = new ArrayList();
    SQLiteDatabase db;
    private FloatingActionButton fabAddNew;
    LayoutInflater inflater;
    private LinearLayout linearList;
    Database mDbHelper;
    OnClickListener onItemClick = new OnClickListener() {
        public void onClick(View view) {
            for (int i = 0; i < ActivityScreenAddCurrency.this.currencyList.size(); i++) {
                if (((CurrencyList) ActivityScreenAddCurrency.this.currencyList.get(i)).getTag().equals(view.getTag().toString())) {
                    ActivityScreenAddCurrency.this.linearList.removeView(((CurrencyList) ActivityScreenAddCurrency.this.currencyList.get(i)).getView());
                    ActivityScreenAddCurrency.this.currencyList.remove(i);
                }
            }
        }
    };
    private View rootView;
    String selectedCurrencySymbol;
    SharedPreferences sharedPreferences;
    private TextView txtDone;
    private View viewClose;


    public static class CurrencyList {
        String id;
        boolean isNew = false;
        boolean isRemoved = false;
        String tag;
        View view;

        public String getTag() {
            return this.tag;
        }

        public void setTag(String str) {
            this.tag = str;
        }

        public View getView() {
            return this.view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public boolean isRemoved() {
            return this.isRemoved;
        }

        public void setRemoved(boolean z) {
            this.isRemoved = z;
        }

        public boolean isNew() {
            return this.isNew;
        }

        public void setNew(boolean z) {
            this.isNew = z;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String str) {
            this.id = str;
        }
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_screen_add_currency);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        this.mDbHelper = new Database(this);


        initV();
        this.sharedPreferences = SharePref.getSharePref(this);
        this.selectedCurrencySymbol = this.sharedPreferences.getString(SharePref.uCurrencySymbol, "");
        this.inflater = (LayoutInflater) getSystemService("layout_inflater");
        checkIfCurrencyListAvailable();
        this.fabAddNew.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityScreenAddCurrency.this.addNewCurrencyView();
            }
        });
        this.txtDone.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {



                    ActivityScreenAddCurrency.this.onDoneClick();

            }
        });
    }

    public void initV() {
        this.rootView = findViewById(R.id.view_add_currency_main);
        this.viewClose = findViewById(R.id.view_add_currency_close);
        this.linearList = (LinearLayout) findViewById(R.id.linear_add_currency_list);
        this.fabAddNew = (FloatingActionButton) findViewById(R.id.fab_add_currency);
        this.txtDone = (TextView) findViewById(R.id.txt_add_currency_done);
    }

    public void addNewCurrencyView(String str) {
        View inflate = this.inflater.inflate(R.layout.single_currency_note_view, null);
        View findViewById = inflate.findViewById(R.id.view_single_currency_remove);
        EditText editText = (EditText) inflate.findViewById(R.id.edt_view_currency_amount);
        editText.setText(str);
        editText.requestFocus();
        TextView textView = (TextView) inflate.findViewById(R.id.txt_view_currency_symbol);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.selectedCurrencySymbol);
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.currencyList.size());
        stringBuilder2.append("");
        findViewById.setTag(stringBuilder2.toString());
        findViewById.setOnClickListener(this.onItemClick);
        CurrencyList currencyList = new CurrencyList();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(this.currencyList.size());
        stringBuilder3.append("");
        currencyList.setTag(stringBuilder3.toString());
        currencyList.setView(inflate);
        this.currencyList.add(currencyList);
        this.linearList.addView(inflate);
    }

    public void addNewCurrencyView() {
        View inflate = this.inflater.inflate(R.layout.single_currency_note_view, null);
        View findViewById = inflate.findViewById(R.id.view_single_currency_remove);
        ((EditText) inflate.findViewById(R.id.edt_view_currency_amount)).requestFocus();
        TextView textView = (TextView) inflate.findViewById(R.id.txt_view_currency_symbol);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.selectedCurrencySymbol);
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.currencyList.size());
        stringBuilder2.append("");
        findViewById.setTag(stringBuilder2.toString());
        findViewById.setOnClickListener(this.onItemClick);
        CurrencyList currencyList = new CurrencyList();
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.currencyList.size());
        stringBuilder2.append("");
        currencyList.setTag(stringBuilder2.toString());
        currencyList.setView(inflate);
        this.currencyList.add(currencyList);
        this.linearList.addView(inflate);
    }

    public void checkIfCurrencyListAvailable() {
        ArrayList currencyList = new CurrencyManager().getCurrencyList(this.sharedPreferences.getString(SharePref.uCurrencyCode, ""));
        for (int i = 0; i < currencyList.size(); i++) {
            addNewCurrencyView((String) currencyList.get(i));
        }
        if (currencyList.size() == 0) {
            addNewCurrencyView();
        }
    }

    public void onCloseClick(View view) {
        finish();
    }

    public void onDoneClick() {
        int i = 0;
        if (this.currencyList.size() == 0) {
            new CToast(this).simpleToast("Add currency", 0).setGravityCenter().show();
            addNewCurrencyView();
            return;
        }
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < this.currencyList.size(); i3++) {
            int edtToDouble = (int) Helper.getEdtToDouble((EditText) ((CurrencyList) this.currencyList.get(i3)).getView().findViewById(R.id.edt_view_currency_amount));
            i2 += edtToDouble;
            if (edtToDouble > 0) {
                arrayList.add(Integer.valueOf(edtToDouble));
            }
        }
        if (i2 == 0) {
            new CToast(this).simpleToast("Zero currency not valid", 0).setGravityCenter().show();
            return;
        }
        this.db = this.mDbHelper.getWritableDatabase();
        while (i < arrayList.size()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Database.COL_C_AMOUNT, (Integer) arrayList.get(i));
            this.db.insert(Database.TABLE_CURRENCY, null, contentValues);
            i++;
        }
        int x = (int) (this.rootView.getX() + ((float) (this.rootView.getWidth() / 2)));
        i = (int) (this.rootView.getY() + ((float) (this.rootView.getHeight() / 2)));



        Intent intent = new Intent(this, ActivityStartUp.class);
        intent.putExtra("X_POINTS", x);
        intent.putExtra("Y_POINTS", i);
        startActivity(intent);
    }
}
