package com.example.malgo.ewidencja.db;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.malgo.ewidencja.db.tables.Incomes;
import com.example.malgo.ewidencja.pojo.Income;
import com.example.malgo.ewidencja.pojo.IncomeStr;

import java.util.ArrayList;

public class IncomeDAO {
    private SQLiteDatabase db;

    public IncomeDAO(Context context, String pass) {
        db = DBHelper.getInstance(context).getWritableDatabase(pass);
    }

    public void insertIncome(final Income income) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Incomes.Columns.INCOME_DATE, income.getIncomeDate());
        contentValues.put(Incomes.Columns.INCOME_NAME, income.getIncomeName());
        contentValues.put(Incomes.Columns.INCOME_AMOUNT, income.getIncomeAmount());
        contentValues.put(Incomes.Columns.INCOME_ANNOTATION, income.getIncomeAnnotation());
        contentValues.put(Incomes.Columns.INCOME_USER_LOGIN, income.getUserLogin());
        contentValues.put(Incomes.Columns.INCOME_IS_EDITABLE, income.isEditable() ? 1 : 0);

        db.insert(Incomes.TABLE_NAME, null, contentValues);
    }

    public ArrayList<Income> getAllIncomesByMonth(String date, String userLogin) {
        Cursor cursor = db.rawQuery("select * from " + Incomes.TABLE_NAME + " where " +
                Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin
                + "' and strftime('%Y-%m', date) = '" + date + "' order by "
                + Incomes.Columns.INCOME_DATE + " asc, " + Incomes.Columns.INCOME_ID
                + " asc;", null);

        ArrayList results = new ArrayList<Income>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Income income = mapCursorToIncome(cursor);
                results.add(income);
            }
        }
        cursor.close();
        return results;
    }

    public ArrayList<IncomeStr> getAllByMonth(String date, String userLogin) {
        Cursor cursor = db.rawQuery("select * from " + Incomes.TABLE_NAME + " where " +
                Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y-%m', date) = '"
                + date + "' order by " + Incomes.Columns.INCOME_DATE + " asc, " + Incomes.Columns.INCOME_ID
                + " asc;", null);

        ArrayList results = new ArrayList<IncomeStr>();
        int position = 1;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                IncomeStr incomeStr = mapCursorToIncomeStr(cursor);
                incomeStr.setIncomeNumber(String.valueOf(position++));
                incomeStr.setIncomeYearAmount(String.format("%.2f", getIncomesCumulativeByIncome(incomeStr, userLogin)));
                results.add(incomeStr);
            }
        }
        cursor.close();
        return results;
    }

    public Income getById(final int id) {
        Cursor cursor = db.rawQuery("select * from " + Incomes.TABLE_NAME + " where " + Incomes.Columns.INCOME_ID + " = " + id + ";", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Income income = mapCursorToIncome(cursor);
            cursor.close();
            return income;
        }
        cursor.close();
        return null;
    }

    public void update(final Income income) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Incomes.Columns.INCOME_DATE, income.getIncomeDate());
        contentValues.put(Incomes.Columns.INCOME_NAME, income.getIncomeName());
        contentValues.put(Incomes.Columns.INCOME_AMOUNT, income.getIncomeAmount());
        contentValues.put(Incomes.Columns.INCOME_ANNOTATION, income.getIncomeAnnotation());
        contentValues.put(Incomes.Columns.INCOME_USER_LOGIN, income.getUserLogin());
        contentValues.put(Incomes.Columns.INCOME_IS_EDITABLE, income.isEditable() ? 1 : 0);

        db.update(Incomes.TABLE_NAME, contentValues, " " + Incomes.Columns.INCOME_ID + " = ?;", new String[]{income.getIncomeId().toString()});
    }

    public void deleteById(final Integer id) {
        db.delete(Incomes.TABLE_NAME, Incomes.Columns.INCOME_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteByUserLogin(final String userLogin) {
        db.delete(Incomes.TABLE_NAME, Incomes.Columns.INCOME_USER_LOGIN + " = ?", new String[]{userLogin});
    }

    public ArrayList getAll(String userLogin) {
        Cursor cursor = db.query(Incomes.TABLE_NAME,
                new String[]{Incomes.Columns.INCOME_ID, Incomes.Columns.INCOME_DATE, Incomes.Columns.INCOME_NAME, Incomes.Columns.INCOME_AMOUNT, Incomes.Columns.INCOME_ANNOTATION, Incomes.Columns.INCOME_USER_LOGIN},
                Incomes.Columns.INCOME_USER_LOGIN + "='" + userLogin + "'", null, null, null, null);

        ArrayList results = new ArrayList<Income>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(mapCursorToIncome(cursor));
            }
        }
        cursor.close();
        return results;
    }

    public ArrayList<String> getAllYears(String userLogin) {
        Cursor cursor = db.rawQuery("select distinct strftime('%Y'," + Incomes.Columns.INCOME_DATE + ") from " + Incomes.TABLE_NAME +
                " where " + Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin + "' order by date desc;", null);
        ArrayList results = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                results.add(cursor.getString(0));
            }
        }
        cursor.close();
        return results;
    }

    public String getCurrentMonth(String userLogin) {
        Cursor cursor = db.rawQuery("select distinct strftime('%m'," + Incomes.Columns.INCOME_DATE + ") from " + Incomes.TABLE_NAME + " where "+
                Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin + "' and strftime('%m', date) = strftime('%m', date('now'));", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            String month = cursor.getString(0);
            cursor.close();
            return month;
        }
        cursor.close();
        return "";
    }

    public Double getIncomesCumulativeByIncome(IncomeStr income, String userLogin) {
        Cursor cursor = db.rawQuery("select sum(" + Incomes.Columns.INCOME_AMOUNT + ") from " + Incomes.TABLE_NAME
                + " where " + Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y'," + Incomes.Columns.INCOME_DATE + ") = strftime('%Y','" + income.getIncomeDate() + "')" +
                " and (strftime('%Y-%m-%d', date) < strftime('%Y-%m-%d','" + income.getIncomeDate() + "')" +
                " or (strftime('%Y-%m-%d', date) = strftime('%Y-%m-%d','" + income.getIncomeDate() + "') and " +
                Incomes.Columns.INCOME_ID + " <= "+ income.getIncomeIdAsInt() + ")" + ");", null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        cursor.close();
        return 0.0;
    }

    public Double getSumOfActualMonth(String userLogin) {
        Cursor cursor = db.rawQuery("select sum(" + Incomes.Columns.INCOME_AMOUNT + ") from "
                + Incomes.TABLE_NAME + " WHERE " + Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y'," + Incomes.Columns.INCOME_DATE + ") = strftime('%Y',date('now')) AND  strftime('%m',"
                + Incomes.Columns.INCOME_DATE + ") = strftime('%m',date('now'));", null);
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
        Cursor cursor = db.rawQuery("select sum(" + Incomes.Columns.INCOME_AMOUNT + ") from "
                + Incomes.TABLE_NAME + " WHERE " + Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin + "' and strftime('%Y'," + Incomes.Columns.INCOME_DATE + ") = strftime('%Y',date('now'));", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        cursor.close();
        return 0.0;
    }

    public Double getSumByMonth(String date, String userLogin) {
        Cursor cursor = db.rawQuery("select sum(" + Incomes.Columns.INCOME_AMOUNT + ") from "
                + Incomes.TABLE_NAME + " WHERE " + Incomes.Columns.INCOME_USER_LOGIN + " = '" + userLogin
                + "' and strftime('%Y-%m', date) = '" + date + "';", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Double sum = cursor.getDouble(0);
            cursor.close();
            return sum;
        }
        cursor.close();
        return 0.0;
    }

    private Income mapCursorToIncome(final Cursor cursor) {
        Income income = new Income();
        income.setIncomeId(cursor.getInt(cursor.getColumnIndex(Incomes.Columns.INCOME_ID)));
        income.setIncomeDate(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_DATE)));
        income.setIncomeName(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_NAME)));
        income.setIncomeAmount(cursor.getDouble(cursor.getColumnIndex(Incomes.Columns.INCOME_AMOUNT)));
        income.setIncomeAnnotation(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_ANNOTATION)));
        income.setUserLogin(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_USER_LOGIN)));
        income.setEditable(cursor.getInt(cursor.getColumnIndex(Incomes.Columns.INCOME_IS_EDITABLE)) == 1 ? true : false);

        return income;
    }

    private IncomeStr mapCursorToIncomeStr(final Cursor cursor) {
        IncomeStr incomeStr = new IncomeStr();
        incomeStr.setIncomeId(String.valueOf(cursor.getInt(cursor.getColumnIndex(Incomes.Columns.INCOME_ID))));
        incomeStr.setIncomeDate(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_DATE)));
        incomeStr.setIncomeName(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_NAME)));
        incomeStr.setIncomeAmount(String.format( "%.2f", cursor.getDouble(cursor.getColumnIndex(Incomes.Columns.INCOME_AMOUNT))));
        incomeStr.setIncomeAnnotation(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_ANNOTATION)));
        incomeStr.setUserLogin(cursor.getString(cursor.getColumnIndex(Incomes.Columns.INCOME_USER_LOGIN)));
        incomeStr.setEditable(cursor.getInt(cursor.getColumnIndex(Incomes.Columns.INCOME_IS_EDITABLE)) == 1 ? true : false);

        return incomeStr;
    }
}
