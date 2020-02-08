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

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Adapters_Items.AddCashInOutItems;
import com.Example.cashcounter.Adapters_Items.AddSalesCashInAdapter;
import com.Example.cashcounter.Adapters_Items.AddSalesCashInAdapter.SetOnItemChange;
import com.Example.cashcounter.Adapters_Items.AddSalesCashOutAdapter;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.DatabaseUntils;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesFragment extends Fragment {
    ArrayList<AddCashInOutItems> arrayListCashIn = new ArrayList();
    ArrayList<AddCashInOutItems> arrayListCashOut = new ArrayList();
    ArrayList<String> arrayListPurpose = new ArrayList();
    double availableAmount = 0.0d;
    private int billAmount = 0;
    private View btnCashIn;
    private View btnCashOut;
    private CheckBox checkBox;
    SQLiteDatabase db;
    private TextInputLayout inputBillAmount;
    private TextInputLayout inputBillNo;
    private TextInputLayout inputNote;
    private AddSalesCashInAdapter mAdapterCashIn;
    private AddSalesCashOutAdapter mAdapterCashOut;
    Database mDbHelper;
    private RecyclerView recyclerViewCashIn;
    private RecyclerView recyclerViewCashOut;
    private int returnAmount;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);
    private Spinner spnPurpose;
    private int totalCashOut = 0;
    private int totalCashPaid = 0;
    private TextView txtAvailableAmount;
    private TextView txtBillAmount;
    private TextView txtEnteredCashInAmount;
    private TextView txtEnteredCashOutAmount;
    private TextView txtPaidAmount;
    private TextView txtReturnAmount;
    private TextView txtSubmit;
    private TextView txtTotalCashInAmount;
    private TextView txtTotalCashInQty;
    private TextView txtTotalCashOutAmount;
    private TextView txtTotalCashOutQty;
    private View viewBottom;
    private View viewCashIn;
    private View viewCashOut;

    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            SalesFragment.this.db = SalesFragment.this.mDbHelper.getReadableDatabase();
            String[] strArr = new String[]{Dashboard.CASH_IN_FRAG};
            Cursor query = SalesFragment.this.db.query(Database.TABLE_CURRENCY, null, "col_c_status = ?", strArr, null, null, "col_c_amount DESC");
            SalesFragment.this.arrayListCashIn.clear();
            SalesFragment.this.arrayListCashOut.clear();
            while (query.moveToNext()) {
                AddCashInOutItems addCashInOutItems = new AddCashInOutItems();
                AddCashInOutItems addCashInOutItems2 = new AddCashInOutItems();
                addCashInOutItems.setQty(0);
                addCashInOutItems2.setQty(0);
                addCashInOutItems.setCurrency(query.getInt(query.getColumnIndex(Database.COL_C_AMOUNT)));
                addCashInOutItems2.setCurrency(query.getInt(query.getColumnIndex(Database.COL_C_AMOUNT)));
                addCashInOutItems.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                addCashInOutItems2.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                String string = query.getString(query.getColumnIndex(Database.SR_ID));
                String str = "col_t_currency_id = ? AND col_t_parent_type = ?";
                Cursor query2 = SalesFragment.this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{string, String.valueOf(1)}, null, null, null);
                int i = 0;
                while (query2.moveToNext()) {
                    i += query2.getInt(query2.getColumnIndex(Database.COL_T_CURRENCY_QTY));
                }
                Cursor query3 = SalesFragment.this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{string, String.valueOf(2)}, null, null, null);
                int i2 = 0;
                while (query3.moveToNext()) {
                    i2 += query3.getInt(query3.getColumnIndex(Database.COL_T_CURRENCY_QTY));
                }
                i -= i2;
                addCashInOutItems.setAvailableQty(i);
                addCashInOutItems2.setAvailableQty(i);
                SalesFragment.this.arrayListCashIn.add(addCashInOutItems);
                SalesFragment.this.arrayListCashOut.add(addCashInOutItems2);
            }
            SalesFragment.this.arrayListPurpose.clear();
            Cursor rawQuery = SalesFragment.this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_PURPOSE, Database.COL_P_TRANSATION_TYPE, String.valueOf(3), null), null);
            while (rawQuery.moveToNext()) {
                SalesFragment.this.arrayListPurpose.add(rawQuery.getString(rawQuery.getColumnIndex(Database.COL_P_NAME)));
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            SalesFragment.this.mAdapterCashIn.notifyDataSetChanged();
            SalesFragment.this.mAdapterCashOut.notifyDataSetChanged();
            ArrayAdapter arrayAdapter = new ArrayAdapter(SalesFragment.this.getActivity(), 17367048, SalesFragment.this.arrayListPurpose);
            arrayAdapter.setDropDownViewResource(17367049);
            SalesFragment.this.spnPurpose.setAdapter(arrayAdapter);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sales, viewGroup, false);
        this.mDbHelper = new Database(getActivity());
        this.sharedPreferences = SharePref.getSharePref(getActivity());
        this.recyclerViewCashIn = (RecyclerView) inflate.findViewById(R.id.recyclerview_add_sales_cashin);
        this.recyclerViewCashOut = (RecyclerView) inflate.findViewById(R.id.recyclerview_add_sales_cashout);
        this.txtEnteredCashInAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_cashin_entered_amount);
        this.txtEnteredCashOutAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_cashout_entered_amount);
        this.txtBillAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_billamount);
        this.txtPaidAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_paidamount);
        this.txtReturnAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_returnamount);
        this.viewBottom = inflate.findViewById(R.id.view_add_sales_bottom);
        this.btnCashOut = inflate.findViewById(R.id.view_add_sales_cashout_btn);
        this.btnCashIn = inflate.findViewById(R.id.view_add_sales_cashin_btn);
        this.viewCashIn = inflate.findViewById(R.id.view_add_sales_cashin);
        this.viewCashOut = inflate.findViewById(R.id.view_add_sales_cashout);
        this.checkBox = (CheckBox) inflate.findViewById(R.id.checkbox_add_sales_auto);
        this.txtAvailableAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_available_amount);
        this.inputBillNo = (TextInputLayout) inflate.findViewById(R.id.input_add_sales_billno);
        this.inputBillAmount = (TextInputLayout) inflate.findViewById(R.id.input_add_sales_billamount);
        this.inputNote = (TextInputLayout) inflate.findViewById(R.id.input_add_sales_note);
        this.spnPurpose = (Spinner) inflate.findViewById(R.id.spn_add_sales_purpose);
        this.txtSubmit = (TextView) inflate.findViewById(R.id.txt_add_sales_submit);
        this.txtTotalCashInQty = (TextView) inflate.findViewById(R.id.txt_add_sales_cashin_qty);
        this.txtTotalCashInAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_cashin_amount);
        this.txtTotalCashOutQty = (TextView) inflate.findViewById(R.id.txt_add_sales_cashout_qty);
        this.txtTotalCashOutAmount = (TextView) inflate.findViewById(R.id.txt_add_sales_cashout_amount);
        this.recyclerViewCashIn.setNestedScrollingEnabled(false);
        this.recyclerViewCashIn.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapterCashIn = new AddSalesCashInAdapter(getActivity(), this.arrayListCashIn);
        this.recyclerViewCashIn.setAdapter(this.mAdapterCashIn);
        this.recyclerViewCashOut.setNestedScrollingEnabled(false);
        this.recyclerViewCashOut.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapterCashOut = new AddSalesCashOutAdapter(getActivity(), this.arrayListCashOut);
        this.recyclerViewCashOut.setAdapter(this.mAdapterCashOut);
        getAvailableAmount();
        new getData().execute(new Void[0]);
        makeCashInTotal();
        this.mAdapterCashIn.SetOnQtyChange(new SetOnItemChange() {
            public void onQtyChanged() {
                SalesFragment.this.makeCashInTotal();
            }
        });
        this.mAdapterCashOut.SetOnQtyChange(new AddSalesCashOutAdapter.SetOnItemChange() {
            public void onQtyChanged() {
                SalesFragment.this.makeCashOutTotal();
            }
        });
        this.txtSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SalesFragment.this.onSubmitClick();
            }
        });
        this.inputBillAmount.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TextView access$100;
                StringBuilder stringBuilder;
                if (charSequence.toString().trim().toString().length() > 0) {
                    SalesFragment.this.billAmount = (int) Double.parseDouble(charSequence.toString().trim());
                    access$100 = SalesFragment.this.txtBillAmount;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(SalesFragment.this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
                    stringBuilder.append(SalesFragment.this.showNumberFormate.format((long) SalesFragment.this.billAmount));
                    access$100.setText(stringBuilder.toString());
                    SalesFragment.this.viewBottom.setVisibility(0);
                    SalesFragment.this.viewCashIn.setVisibility(0);
                    SalesFragment.this.inputBillAmount.setErrorEnabled(false);
                    SalesFragment.this.refreshBothList();
                    return;
                }
                SalesFragment.this.billAmount = 0;
                access$100 = SalesFragment.this.txtBillAmount;
                stringBuilder = new StringBuilder();
                stringBuilder.append(SalesFragment.this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
                stringBuilder.append(SalesFragment.this.showNumberFormate.format((long) SalesFragment.this.billAmount));
                access$100.setText(stringBuilder.toString());
                SalesFragment.this.viewBottom.setVisibility(8);
            }
        });
        this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                SalesFragment.this.makeAutoArrange(z);
                SalesFragment.this.makeCashOutTotal();
            }
        });
        this.btnCashIn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SalesFragment.this.viewCashIn.isShown()) {
                    SalesFragment.this.viewCashIn.setVisibility(8);
                    return;
                }
                SalesFragment.this.viewCashIn.setVisibility(0);
                SalesFragment.this.viewCashOut.setVisibility(8);
            }
        });
        this.btnCashOut.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SalesFragment.this.viewCashOut.isShown()) {
                    SalesFragment.this.viewCashOut.setVisibility(8);
                    return;
                }
                SalesFragment.this.viewCashIn.setVisibility(8);
                SalesFragment.this.viewCashOut.setVisibility(0);
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

    public void makeCashInTotal() {
        this.totalCashPaid = 0;
        int i = 0;
        for (int i2 = 0; i2 < this.arrayListCashIn.size(); i2++) {
            i += ((AddCashInOutItems) this.arrayListCashIn.get(i2)).getQty();
            this.totalCashPaid += ((AddCashInOutItems) this.arrayListCashIn.get(i2)).getQty() * ((AddCashInOutItems) this.arrayListCashIn.get(i2)).getCurrency();
        }
        TextView textView = this.txtTotalCashInQty;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        textView = this.txtTotalCashInAmount;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.totalCashPaid));
        textView.setText(stringBuilder2.toString());
        textView = this.txtEnteredCashInAmount;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Amount ");
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.totalCashPaid));
        textView.setText(stringBuilder2.toString());
        textView = this.txtPaidAmount;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.totalCashPaid));
        textView.setText(stringBuilder2.toString());
        this.returnAmount = this.totalCashPaid - this.billAmount;
        textView = this.txtReturnAmount;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.returnAmount));
        textView.setText(stringBuilder2.toString());
        this.mAdapterCashOut.setMaxAmount(this.returnAmount);
        if (this.returnAmount >= 0) {
            this.txtReturnAmount.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        } else {
            this.txtReturnAmount.setTextColor(SupportMenu.CATEGORY_MASK);
        }
        if (this.totalCashPaid < this.billAmount) {
            this.txtEnteredCashInAmount.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            this.txtEnteredCashInAmount.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
        if (this.returnAmount == 0) {
            makeAutoArrange(false);
            this.btnCashOut.setClickable(false);
        } else {
            this.btnCashOut.setClickable(true);
        }
        if (this.checkBox.isChecked()) {
            makeAutoArrange(true);
        }
        makeCashOutTotal();
    }

    public void makeCashOutTotal() {
        int i = 0;
        this.totalCashOut = 0;
        int i2 = 0;
        while (i < this.arrayListCashOut.size()) {
            i2 += ((AddCashInOutItems) this.arrayListCashOut.get(i)).getQty();
            this.totalCashOut += ((AddCashInOutItems) this.arrayListCashOut.get(i)).getQty() * ((AddCashInOutItems) this.arrayListCashOut.get(i)).getCurrency();
            i++;
        }
        TextView textView = this.txtTotalCashOutQty;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i2);
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        textView = this.txtTotalCashOutAmount;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.totalCashOut));
        textView.setText(stringBuilder2.toString());
        textView = this.txtEnteredCashOutAmount;
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Amount ");
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format((long) this.totalCashOut));
        textView.setText(stringBuilder2.toString());
        if (this.totalCashOut > this.returnAmount) {
            this.txtEnteredCashOutAmount.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            this.txtEnteredCashOutAmount.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }

    public void onSubmitClick() {
        if (this.inputBillNo.getEditText().getText().toString().trim().isEmpty()) {
            this.inputBillNo.setErrorEnabled(true);
            this.inputBillNo.setError("Enter bill number");
            this.inputBillNo.getEditText().requestFocus();
            return;
        }
        double d = 0.0d;
        int i = 0;
        for (int i2 = 0; i2 < this.arrayListCashIn.size(); i2++) {
            i += ((AddCashInOutItems) this.arrayListCashIn.get(i2)).getQty();
            d += (double) (((AddCashInOutItems) this.arrayListCashIn.get(i2)).getQty() * ((AddCashInOutItems) this.arrayListCashIn.get(i2)).getCurrency());
        }
        double d2 = 0.0d;
        int i3 = 0;
        for (int i4 = 0; i4 < this.arrayListCashOut.size(); i4++) {
            i3 += ((AddCashInOutItems) this.arrayListCashOut.get(i4)).getQty();
            d2 += (double) (((AddCashInOutItems) this.arrayListCashOut.get(i4)).getQty() * ((AddCashInOutItems) this.arrayListCashOut.get(i4)).getCurrency());
        }
        if (i == 0) {
            new CToast(getActivity()).simpleToast("Enter qty", 0).setGravityCenter().show();
        } else if (d < ((double) Integer.parseInt(this.inputBillAmount.getEditText().getText().toString().trim()))) {
            new CToast(getActivity()).simpleToast("Bill amount and cash did not matched", 0).setGravityCenter().show();
        } else {
            if (this.returnAmount > 0) {
                if (i3 == 0) {
                    new CToast(getActivity()).simpleToast("Enter return amount", 0).setGravityCenter().show();
                    return;
                } else if (d2 > ((double) this.returnAmount) || d2 < ((double) this.returnAmount)) {
                    new CToast(getActivity()).simpleToast("Return amount did not matched", 0).setGravityCenter().show();
                    return;
                }
            }
            this.db = this.mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Database.COL_S_BILL_AMOUNT, Integer.valueOf(this.billAmount));
            contentValues.put(Database.COL_S_BILL_NO, this.inputBillNo.getEditText().getText().toString().trim());
            contentValues.put(Database.COL_S_PAID_AMOUNT, Integer.valueOf(this.totalCashPaid));
            contentValues.put(Database.COL_S_RETURN_AMOUNT, Integer.valueOf(this.returnAmount));
            contentValues.put(Database.COL_S_TOTAL_CASHIN_QTY, Integer.valueOf(i));
            contentValues.put(Database.COL_S_TOTAL_CASHOUT_QTY, Integer.valueOf(i3));
            contentValues.put(Database.COL_PURPOSE, this.spnPurpose.getSelectedItem().toString());
            contentValues.put(Database.COL_NOTE, this.inputNote.getEditText().getText().toString().trim());
            contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
            Cursor rawQuery = this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_SALES, Database.ROW_ID, String.valueOf(this.db.insert(Database.TABLE_SALES, null, contentValues)), Dashboard.CASH_IN_FRAG), null);
            if (rawQuery.moveToFirst()) {
                String string = rawQuery.getString(rawQuery.getColumnIndex(Database.SR_ID));
                for (i3 = 0; i3 < this.arrayListCashIn.size(); i3++) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put(Database.COL_T_PARENT_ID, string);
                    contentValues2.put(Database.COL_T_PARENT_TYPE, Integer.valueOf(1));
                    contentValues2.put(Database.COL_T_CURRENCY_ID, ((AddCashInOutItems) this.arrayListCashIn.get(i3)).getId());
                    contentValues2.put(Database.COL_T_CURRENCY_AMOUNT, Integer.valueOf(((AddCashInOutItems) this.arrayListCashIn.get(i3)).getCurrency()));
                    contentValues2.put(Database.COL_T_CURRENCY_QTY, Integer.valueOf(((AddCashInOutItems) this.arrayListCashIn.get(i3)).getQty()));
                    contentValues2.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                    if (((AddCashInOutItems) this.arrayListCashIn.get(i3)).getQty() > 0) {
                        this.db.insert(Database.TABLE_TRANSACTION, null, contentValues2);
                    }
                }
                for (int i5 = 0; i5 < this.arrayListCashOut.size(); i5++) {
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put(Database.COL_T_PARENT_ID, string);
                    contentValues3.put(Database.COL_T_PARENT_TYPE, Integer.valueOf(2));
                    contentValues3.put(Database.COL_T_CURRENCY_ID, ((AddCashInOutItems) this.arrayListCashOut.get(i5)).getId());
                    contentValues3.put(Database.COL_T_CURRENCY_AMOUNT, Integer.valueOf(((AddCashInOutItems) this.arrayListCashOut.get(i5)).getCurrency()));
                    contentValues3.put(Database.COL_T_CURRENCY_QTY, Integer.valueOf(((AddCashInOutItems) this.arrayListCashOut.get(i5)).getQty()));
                    contentValues3.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                    if (((AddCashInOutItems) this.arrayListCashOut.get(i5)).getQty() > 0) {
                        this.db.insert(Database.TABLE_TRANSACTION, null, contentValues3);
                    }
                }
                new CToast(getActivity()).simpleToast("Sales added", 0).show();
                getFragmentManager().beginTransaction().detach(getFragmentManager().findFragmentByTag(Dashboard.SALES_FRAG));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, new SalesFragment(), Dashboard.SALES_FRAG).commit();
            }
        }
    }

    public void makeAutoArrange(boolean z) {
        int i;
        if (z) {
            int i2 = 0;
            for (i = 0; i < this.arrayListCashOut.size(); i++) {
                int i3 = this.returnAmount - i2;
                int currency = ((AddCashInOutItems) this.arrayListCashOut.get(i)).getCurrency();
                i3 /= currency;
                if (i3 > ((AddCashInOutItems) this.arrayListCashOut.get(i)).getAvailableQty()) {
                    i3 = ((AddCashInOutItems) this.arrayListCashOut.get(i)).getAvailableQty();
                }
                i2 += currency * i3;
                if (i3 > 0) {
                    ((AddCashInOutItems) this.arrayListCashOut.get(i)).setQty(i3);
                }
            }
            if (i2 < this.returnAmount) {
                new CToast(getActivity()).simpleToast("Change not available", 0).setGravityCenter().show();
            }
            this.mAdapterCashOut.notifyDataSetChanged();
            return;
        }
        for (i = 0; i < this.arrayListCashOut.size(); i++) {
            ((AddCashInOutItems) this.arrayListCashOut.get(i)).setQty(0);
        }
        this.mAdapterCashOut.notifyDataSetChanged();
    }

    public void refreshBothList() {
        int i;
        for (i = 0; i < this.arrayListCashIn.size(); i++) {
            ((AddCashInOutItems) this.arrayListCashIn.get(i)).setQty(0);
        }
        for (i = 0; i < this.arrayListCashOut.size(); i++) {
            ((AddCashInOutItems) this.arrayListCashOut.get(i)).setQty(0);
        }
        this.mAdapterCashIn.notifyDataSetChanged();
        this.mAdapterCashOut.notifyDataSetChanged();
        makeCashOutTotal();
        makeCashInTotal();
    }
}
