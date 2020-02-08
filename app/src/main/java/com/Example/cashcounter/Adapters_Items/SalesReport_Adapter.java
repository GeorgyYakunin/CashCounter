package com.Example.cashcounter.Adapters_Items;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SalesReport_Adapter extends RecyclerView.Adapter<SalesReport_Adapter.ViewHolder> {
    Context context;
    List<SalesReport_Items> itemsList = new ArrayList();
    SetOnItemClickListner setOnItemClickListner;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);

    public interface SetOnItemClickListner {
        void onItemClick(int i);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount;
        TextView txtBillno;
        TextView txtDate;
        TextView txtNote;
        TextView txtPaidAmount;
        TextView txtPurpose;
        TextView txtReturnAmount;
        View viewMain;

        public ViewHolder(View view) {
            super(view);
            this.viewMain = view.findViewById(R.id.view_sales_report_a_main);
            this.txtPurpose = (TextView) view.findViewById(R.id.txt_sales_report_a_purpose);
            this.txtAmount = (TextView) view.findViewById(R.id.txt_sales_report_a_amount);
            this.txtDate = (TextView) view.findViewById(R.id.txt_sales_report_a_date);
            this.txtNote = (TextView) view.findViewById(R.id.txt_sales_report_a_note);
            this.txtBillno = (TextView) view.findViewById(R.id.txt_sales_report_a_bil_no);
            this.txtPaidAmount = (TextView) view.findViewById(R.id.txt_sales_report_a_paid);
            this.txtReturnAmount = (TextView) view.findViewById(R.id.txt_sales_report_a_return);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public SalesReport_Adapter(Context context, List<SalesReport_Items> list) {
        this.context = context;
        this.itemsList = list;
        this.sharedPreferences = SharePref.getSharePref(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_report_sales, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtPurpose.setText(((SalesReport_Items) this.itemsList.get(i)).getPurpose());
        viewHolder.txtDate.setText(Helper.changeDateFormate(((SalesReport_Items) this.itemsList.get(i)).getCreatedOn(), "dd MMM yyyy"));
        TextView textView = viewHolder.txtAmount;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder.append(this.showNumberFormate.format((long) ((SalesReport_Items) this.itemsList.get(i)).getBillAmount()));
        textView.setText(stringBuilder.toString());
        viewHolder.txtBillno.setText(((SalesReport_Items) this.itemsList.get(i)).getBillNo());
        textView = viewHolder.txtPaidAmount;
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder.append(this.showNumberFormate.format((long) ((SalesReport_Items) this.itemsList.get(i)).paidAmount));
        textView.setText(stringBuilder.toString());
        textView = viewHolder.txtReturnAmount;
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder.append(this.showNumberFormate.format((long) ((SalesReport_Items) this.itemsList.get(i)).getReturnAmount()));
        textView.setText(stringBuilder.toString());
        if (((SalesReport_Items) this.itemsList.get(i)).getNote() == null || ((SalesReport_Items) this.itemsList.get(i)).getNote().isEmpty()) {
            viewHolder.txtNote.setVisibility(8);
        } else {
            viewHolder.txtNote.setVisibility(0);
            textView = viewHolder.txtNote;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Note: ");
            stringBuilder.append(((SalesReport_Items) this.itemsList.get(i)).getNote());
            textView.setText(stringBuilder.toString());
        }
        viewHolder.viewMain.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SalesReport_Adapter.this.setOnItemClickListner != null) {
                    SalesReport_Adapter.this.setOnItemClickListner.onItemClick(i);
                }
            }
        });
    }

    public int getItemCount() {
        return this.itemsList.size();
    }

    public void SetOnItemClick(SetOnItemClickListner setOnItemClickListner) {
        this.setOnItemClickListner = setOnItemClickListner;
    }
}
