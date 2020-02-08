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

public class CashInReport_Adapter extends RecyclerView.Adapter<CashInReport_Adapter.ViewHolder> {
    Context context;
    List<CashInReport_Items> itemsList = new ArrayList();
    SetOnItemClickListner setOnItemClickListner;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);

    public interface SetOnItemClickListner {
        void onItemClick(int i);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount;
        TextView txtDate;
        TextView txtNote;
        TextView txtPurpose;
        View viewMain;

        public ViewHolder(View view) {
            super(view);
            this.viewMain = view.findViewById(R.id.view_cashin_report_a_main);
            this.txtPurpose = (TextView) view.findViewById(R.id.txt_cashin_report_a_purpose);
            this.txtAmount = (TextView) view.findViewById(R.id.txt_cashin_report_a_amount);
            this.txtDate = (TextView) view.findViewById(R.id.txt_cashin_report_a_date);
            this.txtNote = (TextView) view.findViewById(R.id.txt_cashin_report_a_note);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public CashInReport_Adapter(Context context, List<CashInReport_Items> list) {
        this.context = context;
        this.itemsList = list;
        this.sharedPreferences = SharePref.getSharePref(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_cash_in_report, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtDate.setText(Helper.changeDateFormate(((CashInReport_Items) this.itemsList.get(i)).getCreatedOn(), "dd MMM yyyy"));
        viewHolder.txtPurpose.setText(((CashInReport_Items) this.itemsList.get(i)).getPurpose());
        TextView textView = viewHolder.txtAmount;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.sharedPreferences.getString(SharePref.uCurrencySymbol, ""));
        stringBuilder.append(this.showNumberFormate.format((long) ((CashInReport_Items) this.itemsList.get(i)).getAmount()));
        textView.setText(stringBuilder.toString());
        if (((CashInReport_Items) this.itemsList.get(i)).getNote() == null || ((CashInReport_Items) this.itemsList.get(i)).getNote().isEmpty()) {
            viewHolder.txtNote.setVisibility(8);
        } else {
            viewHolder.txtNote.setVisibility(0);
            textView = viewHolder.txtNote;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Note: ");
            stringBuilder.append(((CashInReport_Items) this.itemsList.get(i)).getNote());
            textView.setText(stringBuilder.toString());
        }
        viewHolder.viewMain.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CashInReport_Adapter.this.setOnItemClickListner != null) {
                    CashInReport_Adapter.this.setOnItemClickListner.onItemClick(i);
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
