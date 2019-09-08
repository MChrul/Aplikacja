package com.example.malgo.ewidencja.db.tables;

public interface ReceiptDetails {
    String TABLE_NAME = "receiptDetails";

    interface Columns {
        String RECEIPT_DETAILS_ID = "id";
        String RECEIPT_DETAILS_RECEIPT_ID = "receipt_id";
        String RECEIPT_DETAILS_NAME = "name";
        String RECEIPT_DETAILS_MEASURE = "measure";
        String RECEIPT_DETAILS_QUANTITY = "quantity";
        String RECEIPT_DETAILS_UNIT_PRICE = "unit_price";
    }
}
