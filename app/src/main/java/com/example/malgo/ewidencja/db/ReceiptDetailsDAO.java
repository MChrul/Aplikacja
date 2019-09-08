package com.example.malgo.ewidencja.db;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.malgo.ewidencja.db.tables.ReceiptDetails;
import com.example.malgo.ewidencja.pojo.Receipt;
import com.example.malgo.ewidencja.pojo.ReceiptDetail;

import java.util.ArrayList;

public class ReceiptDetailsDAO {
    private SQLiteDatabase db;

    public ReceiptDetailsDAO(Context context, String pass) {
        db = DBHelper.getInstance(context).getWritableDatabase(pass);
    }

    public void insert(final ReceiptDetail receiptDetail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReceiptDetails.Columns.RECEIPT_DETAILS_RECEIPT_ID, receiptDetail.getReceipt_id());
        contentValues.put(ReceiptDetails.Columns.RECEIPT_DETAILS_NAME, receiptDetail.getName());
        contentValues.put(ReceiptDetails.Columns.RECEIPT_DETAILS_MEASURE, receiptDetail.getMeasure());
        contentValues.put(ReceiptDetails.Columns.RECEIPT_DETAILS_QUANTITY, receiptDetail.getQuantity());
        contentValues.put(ReceiptDetails.Columns.RECEIPT_DETAILS_UNIT_PRICE, receiptDetail.getUnit_price());

        db.insert(ReceiptDetails.TABLE_NAME, null, contentValues);
    }

    public ArrayList getDetailsByReceipt(Receipt receipt) {
        Cursor cursor = db.rawQuery("select * from " + ReceiptDetails.TABLE_NAME + " where " +
                ReceiptDetails.Columns.RECEIPT_DETAILS_RECEIPT_ID + " = " + receipt.getId() + " order by " + ReceiptDetails.Columns.RECEIPT_DETAILS_ID + " asc;", null);

        ArrayList results = new ArrayList<ReceiptDetail>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ReceiptDetail receiptDetail = mapCursorToReceiptDetail(cursor);
                results.add(receiptDetail);
            }
        }
        cursor.close();
        return results;
    }

    private ReceiptDetail mapCursorToReceiptDetail(Cursor cursor) {
        ReceiptDetail receiptDetail = new ReceiptDetail();
        receiptDetail.setId(cursor.getInt(cursor.getColumnIndex(ReceiptDetails.Columns.RECEIPT_DETAILS_ID)));
        receiptDetail.setReceipt_id(cursor.getInt(cursor.getColumnIndex(ReceiptDetails.Columns.RECEIPT_DETAILS_RECEIPT_ID)));
        receiptDetail.setName(cursor.getString(cursor.getColumnIndex(ReceiptDetails.Columns.RECEIPT_DETAILS_NAME)));
        receiptDetail.setMeasure(cursor.getString(cursor.getColumnIndex(ReceiptDetails.Columns.RECEIPT_DETAILS_MEASURE)));
        receiptDetail.setQuantity(cursor.getDouble(cursor.getColumnIndex(ReceiptDetails.Columns.RECEIPT_DETAILS_QUANTITY)));
        receiptDetail.setUnit_price(cursor.getDouble(cursor.getColumnIndex(ReceiptDetails.Columns.RECEIPT_DETAILS_UNIT_PRICE)));

        return receiptDetail;
    }
}
