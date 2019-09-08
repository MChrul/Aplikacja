package com.example.malgo.ewidencja.db;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.malgo.ewidencja.db.tables.Buyers;
import com.example.malgo.ewidencja.pojo.Buyer;

public class BuyerDAO {
    private SQLiteDatabase db;

    public BuyerDAO(Context context, String pass) {
        db = DBHelper.getInstance(context).getWritableDatabase(pass);
    }

    public void insert(final Buyer buyer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Buyers.Columns.BUYER_NAME, buyer.getName());
        contentValues.put(Buyers.Columns.BUYER_STREET, buyer.getStreet());
        contentValues.put(Buyers.Columns.BUYER_HOUSE_NR, buyer.getHouse_nr());
        contentValues.put(Buyers.Columns.BUYER_APARTMENT_NR, buyer.getApartment_nr());
        contentValues.put(Buyers.Columns.BUYER_POSTCODE, buyer.getPostcode());
        contentValues.put(Buyers.Columns.BUYER_CITY, buyer.getCity());
        contentValues.put(Buyers.Columns.BUYER_NIP, buyer.getNip());

        db.insert(Buyers.TABLE_NAME, null, contentValues);
    }

    public Buyer getBuyerById(final int id) {
        Cursor cursor = db.rawQuery("select * from " + Buyers.TABLE_NAME + " where " + Buyers.Columns.BUYER_ID + " = " + id + ";", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Buyer buyer = mapCursorToBuyer(cursor);
            cursor.close();
            return buyer;
        }
        cursor.close();
        return null;
    }

    public int getLastInsertedId() {
        Cursor cursor = db.rawQuery("select last_insert_rowid();", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int ind = cursor.getInt(0);
            cursor.close();
            return ind;
        }
        cursor.close();
        return 0;
    }

    private Buyer mapCursorToBuyer(final Cursor cursor) {
        Buyer user = new Buyer();
        user.setId(cursor.getInt(cursor.getColumnIndex(Buyers.Columns.BUYER_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_NAME)));
        user.setStreet(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_STREET)));
        user.setHouse_nr(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_HOUSE_NR)));
        user.setApartment_nr(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_APARTMENT_NR)));
        user.setPostcode(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_POSTCODE)));
        user.setCity(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_CITY)));
        user.setNip(cursor.getString(cursor.getColumnIndex(Buyers.Columns.BUYER_NIP)));
        return user;
    }
}
