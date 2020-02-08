package com.Example.cashcounter.Adapters_Items;

public class AddCashInOutItems {
    int availableQty;
    int currency;
    String id;
    int qty;

    public int getQty() {
        return this.qty;
    }

    public void setQty(int i) {
        this.qty = i;
    }

    public int getCurrency() {
        return this.currency;
    }

    public void setCurrency(int i) {
        this.currency = i;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public int getAvailableQty() {
        return this.availableQty;
    }

    public void setAvailableQty(int i) {
        this.availableQty = i;
    }
}
