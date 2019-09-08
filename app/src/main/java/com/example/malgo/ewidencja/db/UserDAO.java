package com.example.malgo.ewidencja.db;

import android.content.ContentValues;
import android.content.Context;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import com.example.malgo.ewidencja.GlobalClass;
import com.example.malgo.ewidencja.db.tables.Users;
import com.example.malgo.ewidencja.pojo.User;

public class UserDAO {
    private SQLiteDatabase db;

    public UserDAO(Context context, String pass) {
        SQLiteDatabase.loadLibs(context);
        try {
            db = DBHelper.getInstance(context).getWritableDatabase(pass);
        } catch (Throwable e) {
            return;
        }
    }

    public UserDAO(Context context, String pass, GlobalClass m_global) {
        SQLiteDatabase.loadLibs(context);
        try {
            db = DBHelper.getInstance(context).getWritableDatabase(pass);
        } catch (Throwable e) {
            m_global.setPasswordIsCorrect(false);
            return;
        }
        m_global.setPasswordIsCorrect(true);
    }

    public void insertUser(final User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Users.Columns.USER_LOGIN, user.getLogin());
        contentValues.put(Users.Columns.USER_PASSWORD, user.getPassword());

        db.insert(Users.TABLE_NAME, null, contentValues);
    }

    public User getUserByLogin(final String login) {
        Cursor cursor = db.rawQuery("select * from " + Users.TABLE_NAME + " where " + Users.Columns.USER_LOGIN + " = '" + login + "';", null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            User user = mapCursorToUser(cursor);
            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    public void updateUser(final User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Users.Columns.USER_PASSWORD, user.getPassword());
        contentValues.put(Users.Columns.USER_NAME, user.getName());
        contentValues.put(Users.Columns.USER_SURNAME, user.getSurname());
        contentValues.put(Users.Columns.USER_ADDRESS, user.getAddress());
        contentValues.put(Users.Columns.USER_POSTCODE, user.getPostcode());
        contentValues.put(Users.Columns.USER_CITY, user.getCity());

        db.update(Users.TABLE_NAME, contentValues,
                " " + Users.Columns.USER_LOGIN + " = ? ",
                new String[]{user.getLogin()}
        );
    }

    public void deleteUserByLogin(final String login) {
        db.delete(Users.TABLE_NAME, " " + Users.Columns.USER_LOGIN
                + " = ? ", new String[]{login});
    }

    private User mapCursorToUser(final Cursor cursor) {
        User user = new User();
        user.setLogin(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_LOGIN)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_PASSWORD)));
        user.setName(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_NAME)));
        user.setSurname(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_SURNAME)));
        user.setAddress(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_ADDRESS)));
        user.setPostcode(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_POSTCODE)));
        user.setCity(cursor.getString(cursor.getColumnIndex(Users.Columns.USER_CITY)));
        return user;
    }
}
