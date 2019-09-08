package com.example.malgo.ewidencja.db;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.malgo.ewidencja.db.tables.Receipts;
import com.example.malgo.ewidencja.pojo.Receipt;

import java.util.ArrayList;

public class ReceiptDAO {
    private SQLiteDatabase db;

    public ReceiptDAO(Context context, String pass) {
        db = DBHelper.getInstance(context).getWritableDatabase(pass);
    }

    public void insert(final Receipt receipt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Receipts.Columns.RECEIPT_NUMBER, receipt.getNumber());
        contentValues.put(Receipts.Columns.RECEIPT_BUYER_ID, receipt.getBuyerId());
        contentValues.put(Receipts.Columns.RECEIPT_USER_LOGIN, receipt.getUserLogin());
        contentValues.put(Receipts.Columns.RECEIPT_CREATE_DATE, receipt.getCreateDate());
        contentValues.put(Receipts.Columns.RECEIPT_SALE_DATE, receipt.getSaleDate());
        contentValues.put(Receipts.Columns.RECEIPT_CITY, receipt.getCity());
        contentValues.put(Receipts.Columns.RECEIPT_METHOD_OF_PAYMENT, receipt.getMethodOfPayment());

        db.insert(Receipts.TABLE_NAME, null, contentValues);
    }

    public Receipt getById(final int id) {
        Cursor cursor = db.rawQuery("select * from " + Receipts.TABLE_NAME + " where " + Receipts.Columns.RECEIPT_ID +
                "=" + id + ";", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Receipt receipt = mapCursorToReceipt(cursor);
            cursor.close();
            return receipt;
        }
        cursor.close();
        return null;
    }

    public void deleteByUserLogin(final String userLogin) {
        db.delete(Receipts.TABLE_NAME, Receipts.Columns.RECEIPT_USER_LOGIN + " = ?", new String[]{userLogin});
    }

    public ArrayList getAllByYear(String year, String userLogin) {
        Cursor cursor = db.rawQuery("select * from " + Receipts.TABLE_NAME + " where " + Receipts.Columns.RECEIPT_USER_LOGIN +
                "='" + userLogin + "' and strftime('%Y'," + Receipts.Columns.RECEIPT_CREATE_DATE + ") = '" + year + "';", null);

        ArrayList results = new ArrayList<Receipt>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToReceipt(cursor));
            }
        }
        cursor.close();
        return results;
    }

    public int getLastNrOfReceipt(String userLogin) {
        Cursor cursor = db.rawQuery("select count(" + Receipts.Columns.RECEIPT_NUMBER + ") from " + Receipts.TABLE_NAME +
                " WHERE " + Receipts.Columns.RECEIPT_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y'," + Receipts.Columns.RECEIPT_CREATE_DATE + ") = strftime('%Y',date('now')) AND  strftime('%m',"
                + Receipts.Columns.RECEIPT_CREATE_DATE + ") = strftime('%m',date('now'));", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int number = cursor.getInt(0);
            cursor.close();
            return number;
        }
        cursor.close();
        return 0;
    }

    public int getLastInsertedId() {
        Cursor cursor = db.rawQuery("select last_insert_rowid();", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return 0;
    }

    public ArrayList getAllYears(String userLogin) {
        Cursor cursor = db.rawQuery("select distinct strftime('%Y'," + Receipts.Columns.RECEIPT_CREATE_DATE + ") from " + Receipts.TABLE_NAME +
                " where " + Receipts.Columns.RECEIPT_USER_LOGIN + " = '" + userLogin + "' order by " + Receipts.Columns.RECEIPT_CREATE_DATE + " desc;", null);
        ArrayList results = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
        }
        cursor.close();
        return results;
    }

    private Receipt mapCursorToReceipt(Cursor cursor) {
        Receipt receipt = new Receipt();
        receipt.setId(cursor.getInt(cursor.getColumnIndex(Receipts.Columns.RECEIPT_ID)));
        receipt.setBuyerId(cursor.getInt(cursor.getColumnIndex(Receipts.Columns.RECEIPT_BUYER_ID)));
        receipt.setUserLogin(cursor.getString(cursor.getColumnIndex(Receipts.Columns.RECEIPT_USER_LOGIN)));
        receipt.setNumber(cursor.getString(cursor.getColumnIndex(Receipts.Columns.RECEIPT_NUMBER)));
        receipt.setMethodOfPayment(cursor.getString(cursor.getColumnIndex(Receipts.Columns.RECEIPT_METHOD_OF_PAYMENT)));
        receipt.setCreateDate(cursor.getString(cursor.getColumnIndex(Receipts.Columns.RECEIPT_CREATE_DATE)));
        receipt.setSaleDate(cursor.getString(cursor.getColumnIndex(Receipts.Columns.RECEIPT_SALE_DATE)));
        receipt.setCity(cursor.getString(cursor.getColumnIndex(Receipts.Columns.RECEIPT_CITY)));

        return receipt;
    }
}
