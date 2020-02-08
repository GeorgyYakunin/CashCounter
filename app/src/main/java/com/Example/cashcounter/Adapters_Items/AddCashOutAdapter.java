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
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.Class.SharePref;
import com.Example.cashcounter.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddCashOutAdapter extends RecyclerView.Adapter<AddCashOutAdapter.ViewHolder> {
    double billAmount = 0.0d;
    Context context;
    String currencySymbol;
    double currentAmount = 0.0d;
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
            this.viewQty = view.findViewById(R.id.view_add_cashin_a_qty);
            this.txtCurreny = (TextView) view.findViewById(R.id.txt_add_cashina_a_currency);
            this.txtAmount = (TextView) view.findViewById(R.id.txt_add_cashina_a_amount);
            this.editQty = (TextView) view.findViewById(R.id.edt_add_cashina_a_qty);
            this.minusQty = view.findViewById(R.id.img_add_cashin_a_minus);
            this.plusQty = view.findViewById(R.id.img_add_cashin_a_plus);
        }
    }

    public AddCashOutAdapter(Context context, List<AddCashInOutItems> list) {
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
        stringBuilder.append(" (");
        stringBuilder.append(((AddCashInOutItems) this.itemsList.get(i)).getAvailableQty());
        stringBuilder.append(")");
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
                if (((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getQty() > 0) {
                    ((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).setQty(((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getQty() - 1);
                    AddCashOutAdapter.this.notifyItemChanged(i);
                    if (AddCashOutAdapter.this.setOnItemChange != null) {
                        AddCashOutAdapter.this.setOnItemChange.onQtyChanged();
                    }
                }
            }
        });
        viewHolder.plusQty.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getQty() < ((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getAvailableQty()) {
                    viewHolder.editQty.setFocusable(true);
                    if (AddCashOutAdapter.this.currentAmount + ((double) ((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getCurrency()) <= AddCashOutAdapter.this.billAmount) {
                        viewHolder.editQty.setEnabled(true);
                        ((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).setQty(((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getQty() + 1);
                        AddCashOutAdapter.this.notifyItemChanged(i);
                        if (AddCashOutAdapter.this.setOnItemChange != null) {
                            AddCashOutAdapter.this.setOnItemChange.onQtyChanged();
                            return;
                        }
                        return;
                    }
                    viewHolder.editQty.setEnabled(false);
                    return;
                }
                viewHolder.editQty.clearFocus();
                viewHolder.editQty.setFocusable(false);
            }
        });
        viewHolder.viewQty.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i)).getAvailableQty() > 0) {
                    AddCashOutAdapter.this.openQtyPicker(i, viewHolder.txtCurreny.getText().toString().trim());
                } else {
                    new CToast(AddCashOutAdapter.this.context).simpleToast("No qty available", 0).show();
                }
            }
        });
    }

    public int getItemCount() {
        return this.itemsList.size();
    }

    public void setBillAmount(double d) {
        this.billAmount = d;
    }

    public void setCurrentAmount(double d) {
        this.currentAmount = d;
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
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(((AddCashInOutItems) this.itemsList.get(i)).getAvailableQty());
        if (((AddCashInOutItems) this.itemsList.get(i)).getQty() > 0) {
            numberPicker.setValue(((AddCashInOutItems) this.itemsList.get(i)).getQty());
        }
        numberPicker.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                AddCashOutAdapter.this.lastChange = 1;
            }
        });
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                AddCashOutAdapter.this.lastChange = 2;
            }
        });
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
            }
        });
        final int i2 = i;
        final AlertDialog alertDialog = create;
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AddCashOutAdapter.this.lastChange == 1) {
                    AddCashOutAdapter.this.chenageQty(i2, numberPicker.getValue());
                } else if (AddCashOutAdapter.this.lastChange == 2) {
                    int parseInt = Integer.parseInt(textInputLayout.getEditText().getText().toString().trim());
                    if (textInputLayout.getEditText().getText().toString().trim().isEmpty() || parseInt == 0) {
                        AddCashOutAdapter.this.chenageQty(i2, numberPicker.getValue());
                    } else if (parseInt > ((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i2)).getAvailableQty()) {
                        //TextInputLayout textInputLayout = textInputLayout;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Maximum ");
                        stringBuilder.append(((AddCashInOutItems) AddCashOutAdapter.this.itemsList.get(i2)).getAvailableQty());
                        stringBuilder.append(" qty available");
                        textInputLayout.setError(stringBuilder.toString());
                        textInputLayout.getEditText().requestFocus();
                        return;
                    } else {
                        AddCashOutAdapter.this.chenageQty(i2, parseInt);
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
