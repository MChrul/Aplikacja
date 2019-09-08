package com.example.malgo.ewidencja.pojo;

public class ReceiptDetail {
    private int id;
    private int receipt_id;
    private String name;
    private String measure;
    private double quantity;
    private double unit_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(int receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public ReceiptDetailStr mapReceipDetailToString() {
        ReceiptDetailStr receiptDetailStr = new ReceiptDetailStr();

        receiptDetailStr.setId(this.id);
        receiptDetailStr.setReceiptNr(String.valueOf(this.receipt_id));
        receiptDetailStr.setName(this.name);
        receiptDetailStr.setMeasure(this.measure);
        receiptDetailStr.setQuantity(String.format( "%.2f",this.quantity));
        receiptDetailStr.setUnit_price(String.format( "%.2f",this.unit_price));
        receiptDetailStr.setTotalValue(String.format( "%.2f",this.quantity * this.unit_price));

        return receiptDetailStr;
    }
}
