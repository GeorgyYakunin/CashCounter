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

import com.Example.cashcounter.Adapters_Items.SalesReport_Adapter;
import com.Example.cashcounter.Adapters_Items.SalesReport_Adapter.SetOnItemClickListner;
import com.Example.cashcounter.Adapters_Items.SalesReport_Items;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.R;
import java.util.ArrayList;

public class ReportSalesFragment extends Fragment {
    ArrayList<SalesReport_Items> arrayList = new ArrayList();
    SQLiteDatabase db;
    SalesReport_Adapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;
    private View viewContent;
    private View viewEmpty;

    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            ReportSalesFragment.this.db = ReportSalesFragment.this.mDbHelper.getReadableDatabase();
            Cursor query = ReportSalesFragment.this.db.query(Database.TABLE_SALES, null, null, null, null, null, "created_on DESC");
            ReportSalesFragment.this.arrayList.clear();
            while (query.moveToNext()) {
                SalesReport_Items salesReport_Items = new SalesReport_Items();
                salesReport_Items.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                salesReport_Items.setQty(query.getString(query.getColumnIndex(Database.COL_S_TOTAL_CASHIN_QTY)));
                salesReport_Items.setNote(query.getString(query.getColumnIndex(Database.COL_NOTE)));
                salesReport_Items.setCreatedOn(query.getString(query.getColumnIndex(Database.CREATED_ON)));
                salesReport_Items.setPurpose(query.getString(query.getColumnIndex(Database.COL_PURPOSE)));
                salesReport_Items.setBillAmount(query.getInt(query.getColumnIndex(Database.COL_S_BILL_AMOUNT)));
                salesReport_Items.setPaidAmount(query.getInt(query.getColumnIndex(Database.COL_S_PAID_AMOUNT)));
                salesReport_Items.setReturnAmount(query.getInt(query.getColumnIndex(Database.COL_S_RETURN_AMOUNT)));
                salesReport_Items.setBillNo(query.getString(query.getColumnIndex(Database.COL_S_BILL_NO)));
                ReportSalesFragment.this.arrayList.add(salesReport_Items);
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (ReportSalesFragment.this.arrayList.size() == 0) {
                ReportSalesFragment.this.viewEmpty.setVisibility(0);
                ReportSalesFragment.this.viewContent.setVisibility(8);
            } else {
                ReportSalesFragment.this.viewEmpty.setVisibility(8);
                ReportSalesFragment.this.viewContent.setVisibility(0);
            }
            ReportSalesFragment.this.mAdapter.notifyDataSetChanged();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sales_report, viewGroup, false);
        this.mDbHelper = new Database(getActivity());
        this.viewEmpty = inflate.findViewById(R.id.view_report_cashin_empty);
        this.viewContent = inflate.findViewById(R.id.view_report_cashin_content);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_report_cashin_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new SalesReport_Adapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        new getData().execute(new Void[0]);
        this.mAdapter.SetOnItemClick(new SetOnItemClickListner() {
            public void onItemClick(int i) {
                ReportSalesFragment.this.deleteItem(i);
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
                String[] strArr = new String[]{((SalesReport_Items) ReportSalesFragment.this.arrayList.get(i)).getId()};
                ReportSalesFragment.this.db.delete(Database.TABLE_SALES, "sr_id = ?", strArr);
                ReportSalesFragment.this.db.delete(Database.TABLE_TRANSACTION, "col_t_parent_id = ?", strArr);
                ReportSalesFragment.this.arrayList.remove(i);
                ReportSalesFragment.this.mAdapter.notifyDataSetChanged();
                new CToast(ReportSalesFragment.this.getActivity()).simpleToast("Transaction deleted", 0).show();
            }
        }).setNegativeButton((CharSequence) "Cancel", null).show();
    }
}
