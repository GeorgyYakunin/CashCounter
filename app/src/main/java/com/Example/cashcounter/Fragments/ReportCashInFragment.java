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

import com.Example.cashcounter.Adapters_Items.CashInReport_Adapter;
import com.Example.cashcounter.Adapters_Items.CashInReport_Adapter.SetOnItemClickListner;
import com.Example.cashcounter.Adapters_Items.CashInReport_Items;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.R;
import java.util.ArrayList;

public class ReportCashInFragment extends Fragment {
    ArrayList<CashInReport_Items> arrayList = new ArrayList();
    SQLiteDatabase db;
    CashInReport_Adapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;
    String type;
    private View viewContent;
    private View viewEmpty;

    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            ReportCashInFragment.this.db = ReportCashInFragment.this.mDbHelper.getReadableDatabase();
            Cursor query = ReportCashInFragment.this.db.query(ReportCashInFragment.this.type.equals(Dashboard.CASH_IN_FRAG) ? Database.TABLE_CASH_IN : Database.TABLE_CASH_OUT, null, null, null, null, null, "created_on DESC");
            ReportCashInFragment.this.arrayList.clear();
            while (query.moveToNext()) {
                CashInReport_Items cashInReport_Items = new CashInReport_Items();
                cashInReport_Items.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                cashInReport_Items.setAmount(query.getInt(query.getColumnIndex(Database.COL_CI_AMOUNT)));
                cashInReport_Items.setQty(query.getString(query.getColumnIndex(Database.COL_CI_QTY)));
                cashInReport_Items.setCreatedOn(query.getString(query.getColumnIndex(Database.CREATED_ON)));
                cashInReport_Items.setNote(query.getString(query.getColumnIndex(Database.COL_NOTE)));
                cashInReport_Items.setPurpose(query.getString(query.getColumnIndex(Database.COL_PURPOSE)));
                ReportCashInFragment.this.arrayList.add(cashInReport_Items);
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (ReportCashInFragment.this.arrayList.size() == 0) {
                ReportCashInFragment.this.viewEmpty.setVisibility(0);
                ReportCashInFragment.this.viewContent.setVisibility(8);
            } else {
                ReportCashInFragment.this.viewEmpty.setVisibility(8);
                ReportCashInFragment.this.viewContent.setVisibility(0);
            }
            ReportCashInFragment.this.mAdapter.notifyDataSetChanged();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_report_cash_in, viewGroup, false);
        this.type = getArguments().getString("type");
        this.mDbHelper = new Database(getActivity());
        this.viewEmpty = inflate.findViewById(R.id.view_report_cashin_empty);
        this.viewContent = inflate.findViewById(R.id.view_report_cashin_content);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_report_cashin_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new CashInReport_Adapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        new getData().execute(new Void[0]);
        this.mAdapter.SetOnItemClick(new SetOnItemClickListner() {
            public void onItemClick(int i) {
                ReportCashInFragment.this.deleteItem(i);
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
                String[] strArr;
                if (ReportCashInFragment.this.type.equals(Dashboard.CASH_IN_FRAG)) {
                    strArr = new String[]{((CashInReport_Items) ReportCashInFragment.this.arrayList.get(i)).getId()};
                    ReportCashInFragment.this.db.delete(Database.TABLE_CASH_IN, "sr_id = ?", strArr);
                    ReportCashInFragment.this.db.delete(Database.TABLE_TRANSACTION, "col_t_parent_id = ?", strArr);
                } else {
                    strArr = new String[]{((CashInReport_Items) ReportCashInFragment.this.arrayList.get(i)).getId()};
                    ReportCashInFragment.this.db.delete(Database.TABLE_CASH_OUT, "sr_id = ?", strArr);
                    ReportCashInFragment.this.db.delete(Database.TABLE_TRANSACTION, "col_t_parent_id = ?", strArr);
                }
                ReportCashInFragment.this.arrayList.remove(i);
                ReportCashInFragment.this.mAdapter.notifyDataSetChanged();
                new CToast(ReportCashInFragment.this.getActivity()).simpleToast("Transaction deleted", 0).show();
            }
        }).setNegativeButton((CharSequence) "Cancel", null).show();
    }
}
