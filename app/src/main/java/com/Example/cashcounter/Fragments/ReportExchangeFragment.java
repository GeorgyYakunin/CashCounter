package com.Example.cashcounter.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Adapters_Items.ExchangeReport_Adapter;
import com.Example.cashcounter.Adapters_Items.ExchangeReport_Adapter.SetOnItemClickListner;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.ExchangeReport_Items;
import com.Example.cashcounter.R;
import java.util.ArrayList;

public class ReportExchangeFragment extends Fragment {
    ArrayList<ExchangeReport_Items> arrayList = new ArrayList();
    SQLiteDatabase db;
    ExchangeReport_Adapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;
    private View viewContent;
    private View viewEmpty;

    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            ReportExchangeFragment.this.db = ReportExchangeFragment.this.mDbHelper.getReadableDatabase();
            Cursor query = ReportExchangeFragment.this.db.query(Database.TABLE_EXCHANGE, null, null, null, null, null, "created_on DESC");
            ReportExchangeFragment.this.arrayList.clear();
            while (query.moveToNext()) {
                ExchangeReport_Items exchangeReport_Items = new ExchangeReport_Items();
                exchangeReport_Items.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                exchangeReport_Items.setCreatedOn(query.getString(query.getColumnIndex(Database.CREATED_ON)));
                exchangeReport_Items.setNote(query.getString(query.getColumnIndex(Database.COL_NOTE)));
                exchangeReport_Items.setPurpose(query.getString(query.getColumnIndex(Database.COL_PURPOSE)));
                exchangeReport_Items.setAmount(query.getInt(query.getColumnIndex(Database.COL_E_AMOUNT)));
                exchangeReport_Items.setInQty(query.getInt(query.getColumnIndex(Database.COL_E_TOTAL_IN_QTY)));
                exchangeReport_Items.setOutQty(query.getInt(query.getColumnIndex(Database.COL_E_TOTAL_OUT_QTY)));
                ReportExchangeFragment.this.arrayList.add(exchangeReport_Items);
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (ReportExchangeFragment.this.arrayList.size() == 0) {
                ReportExchangeFragment.this.viewEmpty.setVisibility(0);
                ReportExchangeFragment.this.viewContent.setVisibility(8);
            } else {
                ReportExchangeFragment.this.viewEmpty.setVisibility(8);
                ReportExchangeFragment.this.viewContent.setVisibility(0);
            }
            ReportExchangeFragment.this.mAdapter.notifyDataSetChanged();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_report_exchange, viewGroup, false);
        this.mDbHelper = new Database(getActivity());
        this.viewEmpty = inflate.findViewById(R.id.view_report_cashin_empty);
        this.viewContent = inflate.findViewById(R.id.view_report_cashin_content);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_report_cashin_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new ExchangeReport_Adapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        new getData().execute(new Void[0]);
        this.mAdapter.SetOnItemClick(new SetOnItemClickListner() {
            public void onItemClick(int i) {
                ReportExchangeFragment.this.deleteItem(i);
            }
        });
        return inflate;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDbHelper.close();
    }

    public void deleteItem(final int i) {
        new AlertDialog.Builder(getActivity()).setTitle((CharSequence) "Confirm Delete").setMessage((CharSequence) "Are you sure you want to delete this transaction?").setPositiveButton((CharSequence) "YES", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] strArr = new String[]{((ExchangeReport_Items) ReportExchangeFragment.this.arrayList.get(i)).getId()};
                ReportExchangeFragment.this.db.delete(Database.TABLE_EXCHANGE, "sr_id = ?", strArr);
                ReportExchangeFragment.this.db.delete(Database.TABLE_TRANSACTION, "col_t_parent_id = ?", strArr);
                ReportExchangeFragment.this.arrayList.remove(i);
                ReportExchangeFragment.this.mAdapter.notifyDataSetChanged();
                new CToast(ReportExchangeFragment.this.getActivity()).simpleToast("Transaction deleted", 0).show();
            }
        }).setNegativeButton((CharSequence) "Cancel", null).show();
    }
}
