package com.Example.cashcounter.Adapters_Items;

public class CashInReport_Items {
    int amount;
    String createdOn;
    String id;
    String note;
    String purpose;
    String qty;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int i) {
        this.amount = i;
    }

    public String getQty() {
        return this.qty;
    }

    public void setQty(String str) {
        this.qty = str;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String str) {
        this.purpose = str;
    }
}
