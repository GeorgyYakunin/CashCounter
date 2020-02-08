package com.Example.cashcounter.Activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.Example.cashcounter.Activity.ActivityScreenAddCurrency.CurrencyList;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.DatabaseUntils;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.Fragments.Dashboard;
import com.Example.cashcounter.R;
import java.util.ArrayList;

public class ActivityCurrencyListChange extends AppCompatActivity {
    ArrayList<CurrencyList> currencyList = new ArrayList();
    SQLiteDatabase db;
    private FloatingActionButton fabAddNew;
    LayoutInflater inflater;
    private LinearLayout linearList;
    Database mDbHelper;
    OnClickListener onItemClick = new OnClickListener() {
        public void onClick(View view) {
            for (int i = 0; i < ActivityCurrencyListChange.this.currencyList.size(); i++) {
                if (((CurrencyList) ActivityCurrencyListChange.this.currencyList.get(i)).getTag().equals(view.getTag().toString())) {
                    ActivityCurrencyListChange.this.linearList.removeView(((CurrencyList) ActivityCurrencyListChange.this.currencyList.get(i)).getView());
                    ((CurrencyList) ActivityCurrencyListChange.this.currencyList.get(i)).setRemoved(true);
                }
            }
        }
    };
    String selectedCurrencySymbol;
    SharedPreferences sharedPreferences;
    private TextView txtDone;
    private View viewClose;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_currency_list_change);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        this.mDbHelper = new Database(this);
        initV();
        this.sharedPreferences = SharePref.getSharePref(this);
        this.selectedCurrencySymbol = this.sharedPreferences.getString(SharePref.uCurrencySymbol, "");
        this.inflater = (LayoutInflater) getSystemService("layout_inflater");
        loadDefaultList();
        this.fabAddNew.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencyListChange.this.addNewCurrencyView();
            }
        });
        this.txtDone.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityCurrencyListChange.this.onDoneClick();
            }
        });
    }

    public void initV() {
        this.viewClose = findViewById(R.id.view_add_currency_close);
        this.linearList = (LinearLayout) findViewById(R.id.linear_add_currency_list);
        this.fabAddNew = (FloatingActionButton) findViewById(R.id.fab_add_currency);
        this.txtDone = (TextView) findViewById(R.id.txt_add_currency_done);
    }

    public void loadDefaultList() {
        this.db = this.mDbHelper.getReadableDatabase();
        Cursor rawQuery = this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CURRENCY, Database.COL_C_STATUS, String.valueOf(1), null), null);
        while (rawQuery.moveToNext()) {
            addNewCurrencyView(rawQuery.getString(rawQuery.getColumnIndex(Database.COL_C_AMOUNT)), rawQuery.getString(rawQuery.getColumnIndex(Database.SR_ID)));
        }
    }

    public void addNewCurrencyView(String str, String str2) {
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
        currencyList.setId(str2);
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
        currencyList.setNew(true);
        this.currencyList.add(currencyList);
        this.linearList.addView(inflate);
    }

    public void onCloseClick(View view) {
        finish();
    }

    public void onDoneClick() {
        if (this.currencyList.size() == 0) {
            new CToast(this).simpleToast("Add currency", 0).setGravityCenter().show();
            addNewCurrencyView();
            return;
        }
        int i;
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (i = 0; i < this.currencyList.size(); i++) {
            int edtToDouble = (int) Helper.getEdtToDouble((EditText) ((CurrencyList) this.currencyList.get(i)).getView().findViewById(R.id.edt_view_currency_amount));
            i2 += edtToDouble;
            arrayList.add(Integer.valueOf(edtToDouble));
        }
        if (i2 == 0) {
            new CToast(this).simpleToast("Zero currency not valid", 0).setGravityCenter().show();
            return;
        }
        this.db = this.mDbHelper.getWritableDatabase();
        i = 0;
        while (i < arrayList.size()) {
            ContentValues contentValues;
            String[] strArr;
            if (((CurrencyList) this.currencyList.get(i)).isRemoved() && ((CurrencyList) this.currencyList.get(i)).getId() != null) {
                contentValues = new ContentValues();
                contentValues.put(Database.COL_C_STATUS, Integer.valueOf(2));
                strArr = new String[]{((CurrencyList) this.currencyList.get(i)).getId()};
                this.db.update(Database.TABLE_CURRENCY, contentValues, "sr_id = ?", strArr);
            } else if (((CurrencyList) this.currencyList.get(i)).isNew) {
                if (((Integer) arrayList.get(i)).intValue() > 0) {
                    this.db = this.mDbHelper.getReadableDatabase();
                    Cursor rawQuery = this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CURRENCY, Database.COL_C_AMOUNT, String.valueOf(arrayList.get(i)), Dashboard.CASH_IN_FRAG), null);
                    this.db = this.mDbHelper.getWritableDatabase();
                    if (rawQuery.getCount() <= 0 || !rawQuery.moveToFirst()) {
                        contentValues = new ContentValues();
                        contentValues.put(Database.COL_C_AMOUNT, (Integer) arrayList.get(i));
                        this.db.insert(Database.TABLE_CURRENCY, null, contentValues);
                    } else {
                        String string = rawQuery.getString(rawQuery.getColumnIndex(Database.SR_ID));
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put(Database.COL_C_STATUS, Integer.valueOf(1));
                        strArr = new String[]{string};
                        this.db.update(Database.TABLE_CURRENCY, contentValues2, "sr_id = ?", strArr);
                    }
                }
            } else if (((Integer) arrayList.get(i)).intValue() > 0) {
                contentValues = new ContentValues();
                contentValues.put(Database.COL_C_AMOUNT, (Integer) arrayList.get(i));
                strArr = new String[]{((CurrencyList) this.currencyList.get(i)).getId()};
                this.db.update(Database.TABLE_CURRENCY, contentValues, "sr_id = ?", strArr);
            }
            i++;
        }
        setResult(-1);
        finish();
    }
}
