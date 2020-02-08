package com.Example.cashcounter.Adapters_Items;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddCashInAdapter extends RecyclerView.Adapter<AddCashInAdapter.ViewHolder> {
    Context context;
    String currencySymbol;
    List<AddCashInOutItems> itemsList = new ArrayList();
    int lastChange = 0;
    SetOnItemChange setOnItemChange;
    SharedPreferences sharedPreferences;
    DecimalFormat showNumberFormate = new DecimalFormat(Helper.SHOW_NUMBER_FORMATE);

    public interface SetOnItemChange {
        void onQtyChanged();
    }

    class ViewHolder extends  RecyclerView.ViewHolder {
        TextView editQty;
        View minusQty;
        View plusQty;
        TextView txtAmount;
        TextView txtCurreny;
        View viewQty;

        public ViewHolder(View view) {
            super(view);
            this.txtCurreny = (TextView) view.findViewById(R.id.txt_add_cashina_a_currency);
            this.txtAmount = (TextView) view.findViewById(R.id.txt_add_cashina_a_amount);
            this.editQty = (TextView) view.findViewById(R.id.edt_add_cashina_a_qty);
            this.minusQty = view.findViewById(R.id.img_add_cashin_a_minus);
            this.plusQty = view.findViewById(R.id.img_add_cashin_a_plus);
            this.viewQty = view.findViewById(R.id.view_add_cashin_a_qty);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isOdd(int i) {
        return (i & 1) != 0;
    }

    public AddCashInAdapter(Context context, List<AddCashInOutItems> list) {
        this.context = context;
        this.itemsList = list;
        this.sharedPreferences = SharePref.getSharePref(context);
        this.currencySymbol = this.sharedPreferences.getString(SharePref.uCurrencySymbol, "");
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_add_cash_in, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        TextView textView = viewHolder.txtCurreny;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.currencySymbol);
        stringBuilder.append(((AddCashInOutItems) this.itemsList.get(i)).getCurrency());
        textView.setText(stringBuilder.toString());
        int qty = ((AddCashInOutItems) this.itemsList.get(i)).getQty() * ((AddCashInOutItems) this.itemsList.get(i)).getCurrency();
        TextView textView2 = viewHolder.txtAmount;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(this.currencySymbol);
        stringBuilder2.append(this.showNumberFormate.format((long) qty));
        textView2.setText(stringBuilder2.toString());
        if (((AddCashInOutItems) this.itemsList.get(i)).getQty() == 0) {
            viewHolder.editQty.setText("");
        } else {
            textView = viewHolder.editQty;
            stringBuilder = new StringBuilder();
            stringBuilder.append(((AddCashInOutItems) this.itemsList.get(i)).getQty());
            stringBuilder.append("");
            textView.setText(stringBuilder.toString());
        }
        viewHolder.minusQty.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((AddCashInOutItems) AddCashInAdapter.this.itemsList.get(i)).getQty() > 0) {
                    ((AddCashInOutItems) AddCashInAdapter.this.itemsList.get(i)).setQty(((AddCashInOutItems) AddCashInAdapter.this.itemsList.get(i)).getQty() - 1);
                    AddCashInAdapter.this.notifyItemChanged(i);
                    if (AddCashInAdapter.this.setOnItemChange != null) {
                        AddCashInAdapter.this.setOnItemChange.onQtyChanged();
                    }
                }
            }
        });
        viewHolder.plusQty.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((AddCashInOutItems) AddCashInAdapter.this.itemsList.get(i)).setQty(((AddCashInOutItems) AddCashInAdapter.this.itemsList.get(i)).getQty() + 1);
                AddCashInAdapter.this.notifyItemChanged(i);
                if (AddCashInAdapter.this.setOnItemChange != null) {
                    AddCashInAdapter.this.setOnItemChange.onQtyChanged();
                }
            }
        });
        viewHolder.viewQty.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddCashInAdapter.this.openQtyPicker(i, viewHolder.txtCurreny.getText().toString().trim());
            }
        });
    }

    public int getItemCount() {
        return this.itemsList.size();
    }

    public void SetOnQtyChange(SetOnItemChange setOnItemChange) {
        this.setOnItemChange = setOnItemChange;
    }

    public void openQtyPicker(int i, String str) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_qty_picker, null);
        Builder builder = new Builder(this.context);
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        TextView textView = (TextView) inflate.findViewById(R.id.txt_add_qty_title);
        final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.input_add_qty);
        final NumberPicker numberPicker = (NumberPicker) inflate.findViewById(R.id.numberpicker_qty);
        Button button = (Button) inflate.findViewById(R.id.btn_add_qty_cancel);
        Button button2 = (Button) inflate.findViewById(R.id.btn_add_qty_add);
        textView.setText(str);
        numberPicker.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                AddCashInAdapter.this.lastChange = 1;
            }
        });
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                AddCashInAdapter.this.lastChange = 2;
            }
        });
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);
        if (((AddCashInOutItems) this.itemsList.get(i)).getQty() > 0) {
            numberPicker.setValue(((AddCashInOutItems) this.itemsList.get(i)).getQty());
        }
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
            }
        });
        final int i2 = i;
        final AlertDialog alertDialog = create;
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AddCashInAdapter.this.lastChange == 1) {
                    AddCashInAdapter.this.chenageQty(i2, numberPicker.getValue());
                } else if (AddCashInAdapter.this.lastChange == 2) {
                    int parseInt = Integer.parseInt(textInputLayout.getEditText().getText().toString().trim());
                    if (textInputLayout.getEditText().getText().toString().trim().isEmpty() || parseInt == 0) {
                        AddCashInAdapter.this.chenageQty(i2, numberPicker.getValue());
                    } else {
                        AddCashInAdapter.this.chenageQty(i2, parseInt);
                    }
                }
                alertDialog.dismiss();
            }
        });
        create.show();
    }

    public void chenageQty(int i, int i2) {
        ((AddCashInOutItems) this.itemsList.get(i)).setQty(i2);
        notifyItemChanged(i);
        if (this.setOnItemChange != null) {
            this.setOnItemChange.onQtyChanged();
        }
    }
}
