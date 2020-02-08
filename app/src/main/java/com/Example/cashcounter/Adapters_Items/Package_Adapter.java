package com.Example.cashcounter.Adapters_Items;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Package_Adapter extends RecyclerView.Adapter<Package_Adapter.ViewHolder> {
    Context context;
    List<Package_Items> itemsList = new ArrayList();
    String rupee;
    SetOnItemClick setOnItemClick;
    DecimalFormat showFormatter = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);

    public interface SetOnItemClick {
        void onItemClick(int i);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        TextView txtDuration;
        TextView txtName;
        TextView txtPrice;

        public ViewHolder(View view) {
            super(view);
            this.mainView = view.findViewById(R.id.view_package_a_main);
            this.txtName = (TextView) view.findViewById(R.id.txt_package_a_name);
            this.txtPrice = (TextView) view.findViewById(R.id.txt_package_a_amount);
            this.txtDuration = (TextView) view.findViewById(R.id.txt_package_a_duration);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public Package_Adapter(Context context, List<Package_Items> list) {
        this.context = context;
        this.itemsList = list;
        this.rupee = context.getResources().getString(R.string.rupee);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.package_adapter, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtName.setText(((Package_Items) this.itemsList.get(i)).getpName());
        TextView textView = viewHolder.txtPrice;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.rupee);
        stringBuilder.append(((Package_Items) this.itemsList.get(i)).getpRate());
        textView.setText(stringBuilder.toString());
        textView = viewHolder.txtDuration;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Duration ");
        stringBuilder.append(((Package_Items) this.itemsList.get(i)).getpDuration());
        stringBuilder.append(" months");
        textView.setText(stringBuilder.toString());
        viewHolder.mainView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (Package_Adapter.this.setOnItemClick != null) {
                    Package_Adapter.this.setOnItemClick.onItemClick(i);
                }
            }
        });
    }

    public int getItemCount() {
        return this.itemsList.size();
    }

    public void setOnItemClick(SetOnItemClick setOnItemClick) {
        this.setOnItemClick = setOnItemClick;
    }
}
