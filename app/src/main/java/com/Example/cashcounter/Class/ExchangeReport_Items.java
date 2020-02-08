package com.Example.cashcounter.Class;

public class ExchangeReport_Items {
    int amount;
    String createdOn;
    String id;
    int inQty;
    String note;
    int outQty;
    String purpose;

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

    public int getInQty() {
        return this.inQty;
    }

    public void setInQty(int i) {
        this.inQty = i;
    }

    public int getOutQty() {
        return this.outQty;
    }

    public void setOutQty(int i) {
        this.outQty = i;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public void setPurpose(String str) {
        this.purpose = str;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String str) {
        this.note = str;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }
}
