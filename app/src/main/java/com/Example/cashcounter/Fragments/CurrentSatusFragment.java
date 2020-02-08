package com.Example.cashcounter.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Adapters_Items.CurrentStatusAdapter;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrentSatusFragment extends Fragment {
    ArrayList<CurrentStatusItems> arrayList = new ArrayList();
    SQLiteDatabase db;
    private CurrentStatusAdapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);
    private TextView txtTotalAmount;
    private TextView txtTotalQty;

    public class CurrentStatusItems {
        double currencyAmount;
        int inQty;
        int outQty;

        public double getCurrencyAmount() {
            return this.currencyAmount;
        }

        public void setCurrencyAmount(double d) {
            this.currencyAmount = d;
        }

        public int getInQty() {
            return this.inQty;
        }

        public void setInQty(int i) {
            this.inQty = i;
        }

        public int getOutQty() {
            return this.outQty;
        }

        public void setOutQty(int i) {
            this.outQty = i;
        }
    }

    class getData extends AsyncTask<Void, Void, Void> {
        double totalAmount = 0.0d;
        int totalQty = 0;

        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            CurrentSatusFragment.this.db = CurrentSatusFragment.this.mDbHelper.getReadableDatabase();
            String[] strArr = new String[]{Dashboard.CASH_IN_FRAG};
            Cursor query = CurrentSatusFragment.this.db.query(Database.TABLE_CURRENCY, null, "col_c_status = ?", strArr, null, null, "col_c_amount DESC");
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex(Database.SR_ID));
                String str = "col_t_currency_id = ? AND col_t_parent_type = ?";
                Cursor query2 = CurrentSatusFragment.this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{string, String.valueOf(1)}, null, null, null);
                int i = 0;
                while (query2.moveToNext()) {
                    i += query2.getInt(query2.getColumnIndex(Database.COL_T_CURRENCY_QTY));
                }
                Cursor query3 = CurrentSatusFragment.this.db.query(Database.TABLE_TRANSACTION, null, str, new String[]{string, String.valueOf(2)}, null, null, null);
                int i2 = 0;
                while (query3.moveToNext()) {
                    i2 += query3.getInt(query3.getColumnIndex(Database.COL_T_CURRENCY_QTY));
                }
                CurrentStatusItems currentStatusItems = new CurrentStatusItems();
                currentStatusItems.setCurrencyAmount(query.getDouble(query.getColumnIndex(Database.COL_C_AMOUNT)));
                currentStatusItems.setInQty(i);
                currentStatusItems.setOutQty(i2);
                i -= i2;
                this.totalQty += i;
                this.totalAmount += currentStatusItems.getCurrencyAmount() * ((double) i);
                CurrentSatusFragment.this.arrayList.add(currentStatusItems);
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            CurrentSatusFragment.this.mAdapter.notifyDataSetChanged();
            TextView access$100 = CurrentSatusFragment.this.txtTotalQty;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.totalQty);
            stringBuilder.append("");
            access$100.setText(stringBuilder.toString());
            access$100 = CurrentSatusFragment.this.txtTotalAmount;
            stringBuilder = new StringBuilder();
            stringBuilder.append(CurrentSatusFragment.this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
            stringBuilder.append(CurrentSatusFragment.this.showNumberFormate.format(this.totalAmount));
            access$100.setText(stringBuilder.toString());
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_current_satus, viewGroup, false);
        this.sharedPreferences = SharePref.getSharePref(getActivity());
        this.mDbHelper = new Database(getActivity());
        this.txtTotalAmount = (TextView) inflate.findViewById(R.id.txt_counter_status_total_amount);
        this.txtTotalQty = (TextView) inflate.findViewById(R.id.txt_counter_status_total_qty);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_counter_status);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        this.mAdapter = new CurrentStatusAdapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        new getData().execute(new Void[0]);
        return inflate;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDbHelper.close();
    }
}
