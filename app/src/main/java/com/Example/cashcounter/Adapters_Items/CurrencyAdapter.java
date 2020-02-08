package com.Example.cashcounter.Adapters_Items;

import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Class.CurrencyManager.Currency;
import com.Example.cashcounter.R;
import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    Context context;
    List<Currency> itemsList = new ArrayList();
    int lastPositin = -1;

    class ViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        TextView txtName;
        TextView txtSymbol;
        View viewChecked;

        public ViewHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txt_currency_a_name);
            this.txtSymbol = (TextView) view.findViewById(R.id.txt_currency_a_symbol);
            this.viewChecked = view.findViewById(R.id.img_currency_a_check);
            this.mainView = view.findViewById(R.id.view_currency_a_main);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public CurrencyAdapter(Context context, List<Currency> list) {
        this.context = context;
        this.itemsList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_currency, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtSymbol.setText(((Currency) this.itemsList.get(i)).getSymbol());
        TextView textView = viewHolder.txtName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(((Currency) this.itemsList.get(i)).getName());
        stringBuilder.append(" (");
        stringBuilder.append(((Currency) this.itemsList.get(i)).getCode());
        stringBuilder.append(")");
        textView.setText(stringBuilder.toString());
        viewHolder.mainView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CurrencyAdapter.this.lastPositin != i) {
                    ((Currency) CurrencyAdapter.this.itemsList.get(CurrencyAdapter.this.lastPositin)).setSelected(false);
                    CurrencyAdapter.this.notifyItemChanged(CurrencyAdapter.this.lastPositin);
                    ((Currency) CurrencyAdapter.this.itemsList.get(i)).setSelected(true);
                    CurrencyAdapter.this.notifyItemChanged(i);
                }
            }
        });
        if (((Currency) this.itemsList.get(i)).isSelected()) {
            this.lastPositin = i;
            viewHolder.viewChecked.setVisibility(0);
            viewHolder.txtName.setTextColor(this.context.getResources().getColor(R.color.colorPrimary));
            viewHolder.txtSymbol.setTextColor(this.context.getResources().getColor(R.color.colorPrimary));
            return;
        }
        viewHolder.viewChecked.setVisibility(8);
        viewHolder.txtName.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        viewHolder.txtSymbol.setTextColor(ViewCompat.MEASURED_STATE_MASK);
    }

    public int getItemCount() {
        return this.itemsList.size();
    }
}
