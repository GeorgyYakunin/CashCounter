package com.Example.cashcounter.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.Example.cashcounter.Adapters_Items.AddCashInAdapter;
import com.Example.cashcounter.Adapters_Items.AddCashInAdapter.SetOnItemChange;
import com.Example.cashcounter.Adapters_Items.AddCashInOutItems;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.DatabaseUntils;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CashInFragment extends Fragment {
    ArrayList<AddCashInOutItems> arrayList = new ArrayList();
    ArrayList<String> arrayListPurpose = new ArrayList();
    SQLiteDatabase db;
    private TextInputLayout inputNote;
    private AddCashInAdapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);
    private Spinner spnPurpose;
    private TextView txtSubmit;
    private TextView txtTotalAmount;
    private TextView txtTotalQty;
    KProgressHUD hud;


    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }


        public Void doInBackground(Void... voidArr) {
            CashInFragment.this.db = CashInFragment.this.mDbHelper.getReadableDatabase();
            String[] strArr = new String[]{Dashboard.CASH_IN_FRAG};
            Cursor query = CashInFragment.this.db.query(Database.TABLE_CURRENCY, null, "col_c_status = ?", strArr, null, null, "col_c_amount DESC");
            CashInFragment.this.arrayList.clear();
            while (query.moveToNext()) {
                AddCashInOutItems addCashInOutItems = new AddCashInOutItems();
                addCashInOutItems.setQty(0);
                addCashInOutItems.setCurrency(query.getInt(query.getColumnIndex(Database.COL_C_AMOUNT)));
                addCashInOutItems.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                CashInFragment.this.arrayList.add(addCashInOutItems);
            }
            CashInFragment.this.arrayListPurpose.clear();
            Cursor rawQuery = CashInFragment.this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_PURPOSE, Database.COL_P_TRANSATION_TYPE, String.valueOf(1), null), null);
            while (rawQuery.moveToNext()) {
                CashInFragment.this.arrayListPurpose.add(rawQuery.getString(rawQuery.getColumnIndex(Database.COL_P_NAME)));
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            CashInFragment.this.mAdapter.notifyDataSetChanged();
            ArrayAdapter arrayAdapter = new ArrayAdapter(CashInFragment.this.getActivity(), 17367048, CashInFragment.this.arrayListPurpose);
            arrayAdapter.setDropDownViewResource(17367049);
            CashInFragment.this.spnPurpose.setAdapter(arrayAdapter);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_in_cash, viewGroup, false);
        this.mDbHelper = new Database(getActivity());
//
//        mInterstitialAd = new InterstitialAd(getActivity());
//        mInterstitialAd.setAdUnitId(getString(R.string.ads_inter));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Showing Ads....");


        this.sharedPreferences = SharePref.getSharePref(getActivity());
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_add_cash_in);
        this.inputNote = (TextInputLayout) inflate.findViewById(R.id.input_add_cashin_note);
        this.spnPurpose = (Spinner) inflate.findViewById(R.id.spn_add_cashin_purpose);
        this.txtSubmit = (TextView) inflate.findViewById(R.id.txt_add_cashina_submit);
        this.txtTotalQty = (TextView) inflate.findViewById(R.id.txt_add_cashina_total_qty);
        this.txtTotalAmount = (TextView) inflate.findViewById(R.id.txt_add_cashina_total_amount);
        this.recyclerView.setNestedScrollingEnabled(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new AddCashInAdapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        new getData().execute(new Void[0]);
        makeTotal();
        this.mAdapter.SetOnQtyChange(new SetOnItemChange() {
            public void onQtyChanged() {
                CashInFragment.this.makeTotal();
            }
        });
        this.txtSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CashInFragment.this.onSubmitClick();
            }
        });
        return inflate;
    }

    public void makeTotal() {
        double d = 0.0d;
        int i = 0;
        for (int i2 = 0; i2 < this.arrayList.size(); i2++) {
            i += ((AddCashInOutItems) this.arrayList.get(i2)).getQty();
            d += (double) (((AddCashInOutItems) this.arrayList.get(i2)).getQty() * ((AddCashInOutItems) this.arrayList.get(i2)).getCurrency());
        }
        TextView textView = this.txtTotalQty;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        textView = this.txtTotalAmount;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder2.append(this.showNumberFormate.format(d));
        textView.setText(stringBuilder2.toString());
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
            return;
        }
        this.db = this.mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.COL_CI_AMOUNT, Double.valueOf(d));
        contentValues.put(Database.COL_CI_QTY, Integer.valueOf(i));
        contentValues.put(Database.COL_PURPOSE, this.spnPurpose.getSelectedItem().toString());
        contentValues.put(Database.COL_NOTE, this.inputNote.getEditText().getText().toString().trim());
        contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
        Cursor rawQuery = this.db.rawQuery(DatabaseUntils.selectQuery(Database.TABLE_CASH_IN, Database.ROW_ID, String.valueOf(this.db.insert(Database.TABLE_CASH_IN, null, contentValues)), Dashboard.CASH_IN_FRAG), null);
        if (rawQuery.moveToFirst()) {
            String string = rawQuery.getString(rawQuery.getColumnIndex(Database.SR_ID));
            for (i = 0; i < this.arrayList.size(); i++) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(Database.COL_T_PARENT_ID, string);
                contentValues2.put(Database.COL_T_PARENT_TYPE, Integer.valueOf(1));
                contentValues2.put(Database.COL_T_CURRENCY_ID, ((AddCashInOutItems) this.arrayList.get(i)).getId());
                contentValues2.put(Database.COL_T_CURRENCY_AMOUNT, Integer.valueOf(((AddCashInOutItems) this.arrayList.get(i)).getCurrency()));
                contentValues2.put(Database.COL_T_CURRENCY_QTY, Integer.valueOf(((AddCashInOutItems) this.arrayList.get(i)).getQty()));
                contentValues2.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                if (((AddCashInOutItems) this.arrayList.get(i)).getQty() > 0) {
                    this.db.insert(Database.TABLE_TRANSACTION, null, contentValues2);
                }
            }
            new CToast(getActivity()).simpleToast("Cash Added", 0).show();

            hud.show();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                        hud.dismiss();
                        getFragmentManager().beginTransaction().detach(getFragmentManager().findFragmentByTag(Dashboard.CASH_IN_FRAG));
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, new CashInFragment(), Dashboard.CASH_IN_FRAG).commit();



//                    mInterstitialAd.setAdListener(new AdListener() {
//                        @Override
//                        public void onAdLoaded() {
//
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(int errorCode) {
//                            hud.dismiss();
//                            getFragmentManager().beginTransaction().detach(getFragmentManager().findFragmentByTag(Dashboard.CASH_IN_FRAG));
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, new CashInFragment(), Dashboard.CASH_IN_FRAG).commit();
//                        }
//
//                        @Override
//                        public void onAdOpened() {
//                            // Code to be executed when the ad is displayed.
//                        }
//
//                        @Override
//                        public void onAdClicked() {
//                            // Code to be executed when the user clicks on an ad.
//                        }
//
//                        @Override
//                        public void onAdLeftApplication() {
//                            // Code to be executed when the user has left the app.
//                        }
//
//                        @Override
//                        public void onAdClosed() {
//                            getFragmentManager().beginTransaction().detach(getFragmentManager().findFragmentByTag(Dashboard.CASH_IN_FRAG));
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_dashboard, new CashInFragment(), Dashboard.CASH_IN_FRAG).commit();
//                        }
//                    });
                }
            }, 2000);


        }
    }
}
