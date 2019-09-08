package com.example.malgo.ewidencja.db.tables;

public interface Receipts {
    String TABLE_NAME = "receipts";

    interface Columns {
        String RECEIPT_ID = "id";
        String RECEIPT_NUMBER = "number";
        String RECEIPT_BUYER_ID = "buyerId";
        String RECEIPT_USER_LOGIN = "userLogin";
        String RECEIPT_CREATE_DATE = "createDate";
        String RECEIPT_SALE_DATE = "saleDate";
        String RECEIPT_CITY = "city";
        String RECEIPT_METHOD_OF_PAYMENT = "methodOfPayment";
    }
}
