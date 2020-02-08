package com.Example.cashcounter.Adapters_Items;

public class SalesReport_Items {
    int billAmount;
    String billNo;
    String createdOn;
    String id;
    String note;
    int paidAmount;
    String purpose;
    String qty;
    int returnAmount;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getQty() {
        return this.qty;
    }

    public void setQty(String str) {
        this.qty = str;
    }

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String str) {
        this.billNo = str;
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

    public int getBillAmount() {
        return this.billAmount;
    }

    public void setBillAmount(int i) {
        this.billAmount = i;
    }

    public int getPaidAmount() {
        return this.paidAmount;
    }

    public void setPaidAmount(int i) {
        this.paidAmount = i;
    }

    public int getReturnAmount() {
        return this.returnAmount;
    }

    public void setReturnAmount(int i) {
        this.returnAmount = i;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }
}
