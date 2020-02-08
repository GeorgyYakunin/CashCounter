package com.Example.cashcounter.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Adapters_Items.AddCashInOutItems;
import com.Example.cashcounter.Adapters_Items.AddCashOutAdapter;
import com.Example.cashcounter.Adapters_Items.AddCashOutAdapter.SetOnItemChange;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.DatabaseUntils;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CashOutFragment extends Fragment {
    ArrayList<AddCashInOutItems> arrayList = new ArrayList();
    ArrayList<String> arrayListPurpose = new ArrayList();
    double availableAmount = 0.0d;
    private CheckBox checkBox;
    SQLiteDatabase db;
    private TextInputLayout inputBillAmount;
    private TextInputLayout inputNote;
    private AddCashOutAdapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);
    private Spinner spnPurpose;
    private int totalAmount = 0;
    private TextView txtAvailableAmount;
    private TextView txtSubmit;
    private TextView txtTotalAmount;
    private TextView txtTotalQty;
    private View viewBottom;

    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            CashOutFragment.this.db = CashOutFragment.this.mDbHelper.getReadableDatabase();
            String[] strArr = new String[]{Dashboard.CASH_IN_FRAG};
            Cursor query = CashOutFragment.this.db.query(Database.TABLE_CURRENCY, null, "col_c_status = ?", strArr, null, null, "col_c_amount DESC");
            CashOutFragment.this.arrayList.clear();
            while (query.moveToNext()) {
                AddCashInOutItems addCashInOutItems = new AddCashInOutItems();
                addCashInOutItems.setQty(0);
                addCashInOutItems.setCurrency(query.getInt(query.getColumnIndex(Database.COL_C_AMOUNT)));
                addCashInOutItems.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                String string = query.getString(query.getColumnIndex(Database.SR_ID));
                String str = "col_t_currency_id = ? AND col_t_parent_type = ?";
                Cursor query2 = CashOutFragment.this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{string, String.valueOf(1)}, null, null, null);
                int i = 0;
                while (query2.moveToNext()) {
                    i += query2.getInt(query2.getColumnIndex(Database.COL_T_CURRENCY_QTY));
                }
                Cursor query3 = CashOutFragment.this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{string, String.valueOf(2)}, null, null, null);
                int i2 = 0;
                while (query3.moveToNext()) {
                    i2 += query3.getInt(query3.getColumnIndex(Database.COL_T_CURRENCY_QTY));
                }
                addCashInOutItems.setAvailableQty(i - i2);
                CashOutFragment.this.arrayList.add(addCashInOutItems);
            }
            CashOutFragment.this.arrayListPurpose.clear();
            Cursor rawQuery = CashOutFragment.this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_PURPOSE, Database.COL_P_TRANSATION_TYPE, String.valueOf(2), null), null);
            while (rawQuery.moveToNext()) {
                CashOutFragment.this.arrayListPurpose.add(rawQuery.getString(rawQuery.getColumnIndex(Database.COL_P_NAME)));
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            CashOutFragment.this.mAdapter.notifyDataSetChanged();
            ArrayAdapter arrayAdapter = new ArrayAdapter(CashOutFragment.this.getActivity(), 17367048, CashOutFragment.this.arrayListPurpose);
            arrayAdapter.setDropDownViewResource(17367049);
            CashOutFragment.this.spnPurpose.setAdapter(arrayAdapter);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_out_cash, viewGroup, false);
        this.mDbHelper = new Database(getActivity());
        this.sharedPreferences = SharePref.getSharePref(getActivity());
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_add_cash_in);
        this.checkBox = (CheckBox) inflate.findViewById(R.id.checkbox_add_cashout_auto);
        this.txtAvailableAmount = (TextView) inflate.findViewById(R.id.txt_add_cashout_available_amount);
        this.inputBillAmount = (TextInputLayout) inflate.findViewById(R.id.input_add_cashout_billamount);
        this.inputNote = (TextInputLayout) inflate.findViewById(R.id.input_add_cashout_note);
        this.viewBottom = inflate.findViewById(R.id.view_add_cashout_bottom);
        this.spnPurpose = (Spinner) inflate.findViewById(R.id.spn_add_cashout_purpose);
        this.txtSubmit = (TextView) inflate.findViewById(R.id.txt_add_cashout_submit);
        this.txtTotalQty = (TextView) inflate.findViewById(R.id.txt_add_cashout_total_qty);
        this.txtTotalAmount = (TextView) inflate.findViewById(R.id.txt_add_cashout_total_amount);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new AddCashOutAdapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        getAvailableAmount();
        new getData().execute(new Void[0]);
        makeTotal();
        this.mAdapter.SetOnQtyChange(new SetOnItemChange() {
            public void onQtyChanged() {
                CashOutFragment.this.makeTotal();
            }
        });
        this.txtSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CashOutFragment.this.onSubmitClick();
            }
        });
        this.inputBillAmount.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().trim().toString().length() > 0) {
                    double parseDouble = Double.parseDouble(charSequence.toString().trim());
                    if (parseDouble > CashOutFragment.this.availableAmount) {
                        CashOutFragment.this.mAdapter.setBillAmount(0.0d);
                        CashOutFragment.this.inputBillAmount.setErrorEnabled(true);
                        CashOutFragment.this.inputBillAmount.setError("Bill amount not greater than available");
                        CashOutFragment.this.viewBottom.setVisibility(8);
                        for (int i4 = 0; i4 < CashOutFragment.this.arrayList.size(); i4++) {
                            ((AddCashInOutItems) CashOutFragment.this.arrayList.get(i4)).setQty(0);
                        }
                        CashOutFragment.this.mAdapter.notifyDataSetChanged();
                        return;
                    }
                    CashOutFragment.this.mAdapter.setBillAmount(parseDouble);
                    CashOutFragment.this.viewBottom.setVisibility(0);
                    CashOutFragment.this.inputBillAmount.setErrorEnabled(false);
                    return;
                }
                CashOutFragment.this.viewBottom.setVisibility(8);
            }
        });
        this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                CashOutFragment.this.makeAutoArrange(z);
            }
        });
        return inflate;
    }

    public void getAvailableAmount() {
        double d = 0.0d;
        this.availableAmount = 0.0d;
        this.db = this.mDbHelper.getReadableDatabase();
        String str = "col_t_parent_type = ?";
        Cursor query = this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{String.valueOf(1)}, null, null, null);
        double d2 = 0.0d;
        while (query.moveToNext()) {
            d2 += query.getDouble(query.getColumnIndex(Database.COL_T_CURRENCY_AMOUNT)) * ((double) query.getInt(query.getColumnIndex(Database.COL_T_CURRENCY_QTY)));
        }
        Cursor query2 = this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{String.valueOf(2)}, null, null, null);
        while (query2.moveToNext()) {
            d += query2.getDouble(query2.getColumnIndex(Database.COL_T_CURRENCY_AMOUNT)) * ((double) query2.getInt(query2.getColumnIndex(Database.COL_T_CURRENCY_QTY)));
        }
        this.availableAmount = d2 - d;
        TextView textView = this.txtAvailableAmount;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder.append(this.showNumberFormate.format(this.availableAmount));
        textView.setText(stringBuilder.toString());
    }

    public void makeTotal() {
        int i = 0;
        this.totalAmount = 0;
        int i2 = 0;
        while (i < this.arrayList.size()) {
            i2 += ((AddCashInOutItems) this.arrayList.get(i)).getQty();
            this.totalAmount += ((AddCashInOutItems) this.arrayList.get(i)).getQty() * ((AddCashInOutItems) this.arrayList.get(i)).getCurrency();
            i++;
        }
        TextView textView = this.txtTotalQty;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i2);
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        textView = this.txtTotalAmount;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.totalAmount));
        textView.setText(stringBuilder2.toString());
        this.mAdapter.setCurrentAmount((double) this.totalAmount);
    }

    public void onSubmitClick() {
        double d = 0.0d;
        int i = 0;
        for (int i2 = 0; i2 < this.arrayList.size(); i2++) {
            i += ((AddCashInOutItems) this.arrayList.get(i2)).getQty();
            d += (double) (((AddCashInOutItems) this.arrayList.get(i2)).getQty() * ((AddCashInOutItems) this.arrayList.get(i2)).getCurrency());
        }
        if (i == 0) {
            new CToast(getActivity()).simpleToast("Enter qty", 0).setGravityCenter().show();
        } else if (d > ((double) Integer.parseInt(this.inputBillAmount.getEditText().getText().toString().trim()))) {
            new CToast(getActivity()).simpleToast("Bill amount and cash did not matched", 0).setGravityCenter().show();
        } else {
            this.db = this.mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Database.COL_CI_AMOUNT, Double.valueOf(d));
            contentValues.put(Database.COL_CI_QTY, Integer.valueOf(i));
            contentValues.put(Database.COL_PURPOSE, this.spnPurpose.getSelectedItem().toString());
            contentValues.put(Database.COL_NOTE, this.inputNote.getEditText().getText().toString().trim());
            contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
            Cursor rawQuery = this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CASH_OUT, Database.ROW_ID, String.valueOf(this.db.insert(Database.TABLE_CASH_OUT, null, contentValues)), Dashboard.CASH_IN_FRAG), null);
            if (rawQuery.moveToFirst()) {
                String string = rawQuery.getString(rawQuery.getColumnIndex(Database.SR_ID));
                for (i = 0; i < this.arrayList.size(); i++) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put(Database.COL_T_PARENT_ID, string);
                    contentValues2.put(Database.COL_T_PARENT_TYPE, Integer.valueOf(2));
                    contentValues2.put(Database.COL_T_CURRENCY_ID, ((AddCashInOutItems) this.arrayList.get(i)).getId());
                    contentValues2.put(Database.COL_T_CURRENCY_AMOUNT, Integer.valueOf(((AddCashInOutItems) this.arrayList.get(i)).getCurrency()));
                    contentValues2.put(Database.COL_T_CURRENCY_QTY, Integer.valueOf(((AddCashInOutItems) this.arrayList.get(i)).getQty()));
                    contentValues2.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                    if (((AddCashInOutItems) this.arrayList.get(i)).getQty() > 0) {
                        this.db.insert(Database.TABLE_TRANSACTION, null, contentValues2);
                    }
                }
                new CToast(getActivity()).simpleToast("Cash paid", 0).show();
                getFragmentManager().beginTransaction().detach(getFragmentManager().findFragmentByTag(Dashboard.CASH_OUT_FRAG));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, new CashOutFragment(), Dashboard.CASH_IN_FRAG).commit();
            }
        }
    }

    public void makeAutoArrange(boolean z) {
        int parseInt = Integer.parseInt(this.inputBillAmount.getEditText().getText().toString().trim());
        int i;
        if (z) {
            int i2 = 0;
            for (i = 0; i < this.arrayList.size(); i++) {
                int i3 = parseInt - i2;
                int currency = ((AddCashInOutItems) this.arrayList.get(i)).getCurrency();
                i3 /= currency;
                if (i3 > ((AddCashInOutItems) this.arrayList.get(i)).getAvailableQty()) {
                    i3 = ((AddCashInOutItems) this.arrayList.get(i)).getAvailableQty();
                }
                i2 += currency * i3;
                if (i3 > 0) {
                    ((AddCashInOutItems) this.arrayList.get(i)).setQty(i3);
                }
            }
            if (i2 < parseInt) {
                new CToast(getActivity()).simpleToast("Change not available", 0).setGravityCenter().show();
            }
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        for (i = 0; i < this.arrayList.size(); i++) {
            ((AddCashInOutItems) this.arrayList.get(i)).setQty(0);
        }
        this.mAdapter.notifyDataSetChanged();
    }
}
