package com.Example.cashcounter.Adapters_Items;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.Fragments.CurrentSatusFragment.CurrentStatusItems;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CurrentStatusAdapter extends RecyclerView.Adapter<CurrentStatusAdapter.ViewHolder> {
    Context context;
    String currencySymbol;
    List<CurrentStatusItems> itemsList = new ArrayList();
    int lastPositin = -1;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormat = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount;
        TextView txtAvailableAmount;
        TextView txtIn;
        TextView txtOut;
        TextView txtQty;

        public ViewHolder(View view) {
            super(view);
            this.txtAmount = (TextView) view.findViewById(R.id.txt_current_status_a_amount);
            this.txtQty = (TextView) view.findViewById(R.id.txt_current_status_a_qty);
            this.txtAvailableAmount = (TextView) view.findViewById(R.id.txt_current_status_a_available);
            this.txtIn = (TextView) view.findViewById(R.id.txt_current_status_a_in);
            this.txtOut = (TextView) view.findViewById(R.id.txt_current_status_a_out);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public CurrentStatusAdapter(Context context, List<CurrentStatusItems> list) {
        this.context = context;
        this.itemsList = list;
        this.sharedPreferences = SharePref.getSharePref(context);
        this.currencySymbol = this.sharedPreferences.getString(SharePref.uCurrencySymbol, "");
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_current_status, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        double currencyAmount = ((CurrentStatusItems) this.itemsList.get(i)).getCurrencyAmount();
        int inQty = ((CurrentStatusItems) this.itemsList.get(i)).getInQty();
        i = ((CurrentStatusItems) this.itemsList.get(i)).getOutQty();
        TextView textView = viewHolder.txtAmount;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.currencySymbol);
        stringBuilder.append(this.showNumberFormat.format(currencyAmount));
        textView.setText(stringBuilder.toString());
        int i2 = inQty - i;
        currencyAmount *= (double) i2;
        TextView textView2 = viewHolder.txtAvailableAmount;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.currencySymbol);
        stringBuilder2.append(this.showNumberFormat.format(currencyAmount));
        textView2.setText(stringBuilder2.toString());
        TextView textView3 = viewHolder.txtQty;
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(i2);
        stringBuilder3.append("");
        textView3.setText(stringBuilder3.toString());
        textView3 = viewHolder.txtIn;
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("In: ");
        stringBuilder3.append(inQty);
        textView3.setText(stringBuilder3.toString());
        TextView textView4 = viewHolder.txtOut;
        StringBuilder stringBuilder4 = new StringBuilder();
        stringBuilder4.append("Out: ");
        stringBuilder4.append(i);
        textView4.setText(stringBuilder4.toString());
    }

    public int getItemCount() {
        return this.itemsList.size();
    }
}
