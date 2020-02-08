package com.Example.cashcounter.Class;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.Example.cashcounter.R;

public class CToast {
    Context context;
    LayoutInflater inflater;
    Toast toast;
    View toastRoot;

    public CToast(Context context) {
        this.context = context;
    }

    public CToast simpleToast(String str, int i) {
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.toastRoot = this.inflater.inflate(R.layout.simple_toast, null);
        ((TextView) this.toastRoot.findViewById(R.id.txt_simple_toast_message)).setText(str);
        this.toast = new Toast(this.context);
        this.toast.setView(this.toastRoot);
        this.toast.setDuration(i);
        return this;
    }

    public CToast simpleToast(Spanned spanned, int i) {
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.toastRoot = this.inflater.inflate(R.layout.simple_toast, null);
        ((TextView) this.toastRoot.findViewById(R.id.txt_simple_toast_message)).setText(spanned);
        this.toast = new Toast(this.context);
        this.toast.setView(this.toastRoot);
        this.toast.setDuration(i);
        return this;
    }

    public CToast setGravityCenter() {
        this.toast.setGravity(17, 0, 0);
        return this;
    }

    public CToast show() {
        this.toast.show();
        return this;
    }
}
