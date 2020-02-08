package com.Example.cashcounter.Adapters_Items;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.R;
import java.util.ArrayList;
import java.util.List;

public class PurposeAdapter extends RecyclerView.Adapter<PurposeAdapter.ViewHolder> {
    Context context;
    List<Purpose_Items> itemsList = new ArrayList();

    class ViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        TextView txtName;
        TextView txtType;

        public ViewHolder(View view) {
            super(view);
            this.mainView = view.findViewById(R.id.view_purpose_a_main);
            this.txtName = (TextView) view.findViewById(R.id.txt_purpose_a_name);
            this.txtType = (TextView) view.findViewById(R.id.txt_purpose_a_type);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public PurposeAdapter(Context context, List<Purpose_Items> list) {
        this.context = context;
        this.itemsList = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_purpose, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.txtName.setText(((Purpose_Items) this.itemsList.get(i)).getName());
        int transationType = ((Purpose_Items) this.itemsList.get(i)).getTransationType();
        if (transationType == 1) {
            viewHolder.txtType.setText("Cash In");
        } else if (transationType == 2) {
            viewHolder.txtType.setText("Cash Out");
        } else if (transationType == 3) {
            viewHolder.txtType.setText(Database.PURPOSE_SALES);
        } else if (transationType == 4) {
            viewHolder.txtType.setText("Exchange");
        }
        viewHolder.mainView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((Purpose_Items) PurposeAdapter.this.itemsList.get(i)).getType() == 1) {
                    new AlertDialog.Builder(PurposeAdapter.this.context).setMessage((CharSequence) "Default application purpose you can't delete it.").setPositiveButton((CharSequence) "OK", null).show();
                } else {
                    new AlertDialog.Builder(PurposeAdapter.this.context).setMessage((CharSequence) "Are you sure you want to delete this purpose?").setPositiveButton((CharSequence) "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PurposeAdapter.this.deletePurpose(i);
                        }
                    }).setNegativeButton((CharSequence) "Cancel", null).show();
                }
            }
        });
    }

    public int getItemCount() {
        return this.itemsList.size();
    }

    public void deletePurpose(int i) {
        String[] strArr = new String[]{((Purpose_Items) this.itemsList.get(i)).getId()};
        new Database(this.context).getWritableDatabase().delete(Database.TABLE_PURPOSE, "sr_id = ?", strArr);
        this.itemsList.remove(i);
        new CToast(this.context).simpleToast("Purpose deleted", 0).show();
        notifyDataSetChanged();
    }
}
