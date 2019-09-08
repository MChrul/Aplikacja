package com.example.malgo.ewidencja.db;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.malgo.ewidencja.db.tables.Expences;
import com.example.malgo.ewidencja.pojo.Expence;

import java.util.ArrayList;

public class ExpenceDAO {
    private SQLiteDatabase db;

    public ExpenceDAO(Context context, String pass) {
        db = DBHelper.getInstance(context).getWritableDatabase(pass);
    }

    public void insertExpence(final Expence expence) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Expences.Columns.EXPENCE_DATE, expence.getExpenceDate());
        contentValues.put(Expences.Columns.EXPENCE_NAME, expence.getExpenceKind());
        contentValues.put(Expences.Columns.EXPENCE_AMOUNT, expence.getExpenceAmount());
        contentValues.put(Expences.Columns.EXPENCE_USER_LOGIN, expence.getUserLogin());

        db.insert(Expences.TABLE_NAME, null, contentValues);
    }

    public void update(final Expence expence) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Expences.Columns.EXPENCE_DATE, expence.getExpenceDate());
        contentValues.put(Expences.Columns.EXPENCE_NAME, expence.getExpenceKind());
        contentValues.put(Expences.Columns.EXPENCE_AMOUNT, expence.getExpenceAmount());
        contentValues.put(Expences.Columns.EXPENCE_USER_LOGIN, expence.getUserLogin());

        db.update(Expences.TABLE_NAME, contentValues, " " + Expences.Columns.EXPENCE_ID + " = ?;", new String[]{expence.getExpenceId().toString()});
    }

    public void deleteById(final Integer id) {
        db.delete(Expences.TABLE_NAME, Expences.Columns.EXPENCE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteByUserLogin(final String userLogin) {
        db.delete(Expences.TABLE_NAME, Expences.Columns.EXPENCE_USER_LOGIN + " = ?", new String[]{userLogin});
    }

    public ArrayList getAll(String userLogin) {
        Cursor cursor = db.query(Expences.TABLE_NAME,
                new String[]{Expences.Columns.EXPENCE_ID, Expences.Columns.EXPENCE_DATE, Expences.Columns.EXPENCE_NAME, Expences.Columns.EXPENCE_AMOUNT, Expences.Columns.EXPENCE_USER_LOGIN},
                Expences.Columns.EXPENCE_USER_LOGIN + "='" + userLogin + "'", null, null, null, null);

        ArrayList results = new ArrayList<Expence>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToExpence(cursor));
            }
        }
        cursor.close();
        return results;
    }

    public ArrayList getAllYears(String userLogin) {
        Cursor cursor = db.rawQuery("select distinct strftime('%Y'," + Expences.Columns.EXPENCE_DATE + ") from " + Expences.TABLE_NAME +
                " where " + Expences.Columns.EXPENCE_USER_LOGIN + " = '" + userLogin + "' order by date desc;", null);
        ArrayList results = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
        }
        cursor.close();
        return results;
    }

    public ArrayList getAllExpencesByMonth(String date, String userLogin) {
        Cursor cursor = db.rawQuery("select * from " + Expences.TABLE_NAME + " where " + Expences.Columns.EXPENCE_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y-%m', date) = '" + date + "' order by " + Expences.Columns.EXPENCE_DATE + " asc, " + Expences.Columns.EXPENCE_ID + " asc;", null);

        ArrayList results = new ArrayList<Expence>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Expence expence = mapCursorToExpence(cursor);
                results.add(expence);
            }
        }
        cursor.close();
        return results;
    }

    public String getCurrentMonth(String userLogin) {
        Cursor cursor = db.rawQuery("select distinct strftime('%m'," + Expences.Columns.EXPENCE_DATE + ") from " + Expences.TABLE_NAME + " where " + Expences.Columns.EXPENCE_USER_LOGIN + " = '" + userLogin + "' and strftime('%m', date) = strftime('%m', date('now'));", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            String month = cursor.getString(0);
            cursor.close();
            return month;
        }
        cursor.close();
        return "";
    }

    public Double getSumOfActualMonth(String userLogin) {
        Cursor cursor = db.rawQuery("select sum(" + Expences.Columns.EXPENCE_AMOUNT + ") from "
                + Expences.TABLE_NAME + " WHERE " + Expences.Columns.EXPENCE_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y'," + Expences.Columns.EXPENCE_DATE + ") = strftime('%Y',date('now')) AND  strftime('%m',"
                + Expences.Columns.EXPENCE_DATE + ") = strftime('%m',date('now'));", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        cursor.close();
        return 0.0;
    }

    public Double getSumOfActualYear(String userLogin) {
        Cursor cursor = db.rawQuery("select sum(" + Expences.Columns.EXPENCE_AMOUNT + ") from "
                + Expences.TABLE_NAME + " WHERE " + Expences.Columns.EXPENCE_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y'," + Expences.Columns.EXPENCE_DATE + ") = strftime('%Y',date('now'));", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            Double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        cursor.close();
        return 0.0;
    }

    public Double getSumByYear(String date, String userLogin) {
        Cursor cursor = db.rawQuery("select sum(" + Expences.Columns.EXPENCE_AMOUNT + ") from "
                + Expences.TABLE_NAME + " WHERE " + Expences.Columns.EXPENCE_USER_LOGIN + " = '" + userLogin
                + "' and strftime('%Y', date) = '" + date + "';", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        cursor.close();
        return 0.0;
    }

    private Expence mapCursorToExpence(final Cursor cursor) {
        Expence expence = new Expence();
        expence.setExpenceId(cursor.getInt(cursor.getColumnIndex(Expences.Columns.EXPENCE_ID)));
        expence.setExpenceDate(cursor.getString(cursor.getColumnIndex(Expences.Columns.EXPENCE_DATE)));
        expence.setExpenceKind(cursor.getString(cursor.getColumnIndex(Expences.Columns.EXPENCE_NAME)));
        expence.setExpenceAmount(cursor.getDouble(cursor.getColumnIndex(Expences.Columns.EXPENCE_AMOUNT)));

        return expence;
    }
}
