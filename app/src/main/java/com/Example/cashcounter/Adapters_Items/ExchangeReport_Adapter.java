package com.Example.cashcounter.Adapters_Items;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Class.ExchangeReport_Items;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExchangeReport_Adapter extends RecyclerView.Adapter<ExchangeReport_Adapter.ViewHolder> {
    Context context;
    List<ExchangeReport_Items> itemsList = new ArrayList();
    SetOnItemClickListner setOnItemClickListner;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);

    public interface SetOnItemClickListner {
        void onItemClick(int i);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount;
        TextView txtDate;
        TextView txtInQty;
        TextView txtNote;
        TextView txtOutQty;
        TextView txtPurpose;
        View viewMain;

        public ViewHolder(View view) {
            super(view);
            this.viewMain = view.findViewById(R.id.view_sales_report_a_main);
            this.txtPurpose = (TextView) view.findViewById(R.id.txt_sales_report_a_purpose);
            this.txtAmount = (TextView) view.findViewById(R.id.txt_sales_report_a_amount);
            this.txtDate = (TextView) view.findViewById(R.id.txt_sales_report_a_date);
            this.txtNote = (TextView) view.findViewById(R.id.txt_sales_report_a_note);
            this.txtInQty = (TextView) view.findViewById(R.id.txt_sales_report_a_paid);
            this.txtOutQty = (TextView) view.findViewById(R.id.txt_sales_report_a_return);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public ExchangeReport_Adapter(Context context, List<ExchangeReport_Items> list) {
        this.context = context;
        this.itemsList = list;
        this.sharedPreferences = SharePref.getSharePref(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_exchange_report, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtPurpose.setText(((ExchangeReport_Items) this.itemsList.get(i)).getPurpose());
        viewHolder.txtDate.setText(Helper.changeDateFormate(((ExchangeReport_Items) this.itemsList.get(i)).getCreatedOn(), "dd MMM yyyy"));
        TextView textView = viewHolder.txtAmount;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder.append(this.showNumberFormate.format((long) ((ExchangeReport_Items) this.itemsList.get(i)).getAmount()));
        textView.setText(stringBuilder.toString());
        textView = viewHolder.txtInQty;
        stringBuilder = new StringBuilder();
        stringBuilder.append(((ExchangeReport_Items) this.itemsList.get(i)).getInQty());
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        textView = viewHolder.txtOutQty;
        stringBuilder = new StringBuilder();
        stringBuilder.append(((ExchangeReport_Items) this.itemsList.get(i)).getOutQty());
        stringBuilder.append("");
        textView.setText(stringBuilder.toString());
        if (((ExchangeReport_Items) this.itemsList.get(i)).getNote() == null || ((ExchangeReport_Items) this.itemsList.get(i)).getNote().isEmpty()) {
            viewHolder.txtNote.setVisibility(8);
        } else {
            viewHolder.txtNote.setVisibility(0);
            textView = viewHolder.txtNote;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Note: ");
            stringBuilder.append(((ExchangeReport_Items) this.itemsList.get(i)).getNote());
            textView.setText(stringBuilder.toString());
        }
        viewHolder.viewMain.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ExchangeReport_Adapter.this.setOnItemClickListner != null) {
                    ExchangeReport_Adapter.this.setOnItemClickListner.onItemClick(i);
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
