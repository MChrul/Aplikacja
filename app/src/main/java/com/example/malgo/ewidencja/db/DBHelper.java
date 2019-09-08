package com.example.malgo.ewidencja.db;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import com.example.malgo.ewidencja.db.tables.Buyers;
import com.example.malgo.ewidencja.db.tables.Expences;
import com.example.malgo.ewidencja.db.tables.Incomes;
import com.example.malgo.ewidencja.db.tables.ReceiptDetails;
import com.example.malgo.ewidencja.db.tables.Receipts;
import com.example.malgo.ewidencja.db.tables.Users;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    private final static int DB_VERSION = 19;
    private final static String DB_NAME = "Ewidencja.db";

    private static final String TABLE_USERS =
            "create table " + Users.TABLE_NAME + " ( " +
            Users.Columns.USER_LOGIN + " text primary key not null, " +
            Users.Columns.USER_PASSWORD + " text not null, " +
            Users.Columns.USER_NAME + " text, " +
            Users.Columns.USER_SURNAME + " text, " +
            Users.Columns.USER_ADDRESS + " text, " +
            Users.Columns.USER_POSTCODE + " text, " +
            Users.Columns.USER_CITY + " text );";

    private static final String TABLE_INCOMES = "create table " + Incomes.TABLE_NAME + "( " +
            Incomes.Columns.INCOME_ID + " integer primary key, " +
            Incomes.Columns.INCOME_USER_LOGIN + " text not null, " +
            Incomes.Columns.INCOME_DATE + " text not null, " +
            Incomes.Columns.INCOME_NAME + " text not null, " +
            Incomes.Columns.INCOME_AMOUNT + " real not null, " +
            Incomes.Columns.INCOME_IS_EDITABLE + " integer not null, " +
            Incomes.Columns.INCOME_ANNOTATION + " text, " +
            " foreign key(" + Incomes.Columns.INCOME_USER_LOGIN + ") references " + Users.TABLE_NAME + "(" + Users.Columns.USER_LOGIN + "));";

    private static final String TABLE_EXPENCES = "create table " + Expences.TABLE_NAME + "( " +
            Expences.Columns.EXPENCE_ID + " integer primary key, " +
            Expences.Columns.EXPENCE_USER_LOGIN + " text not null, " +
            Expences.Columns.EXPENCE_DATE + " text not null, " +
            Expences.Columns.EXPENCE_NAME + " text not null, " +
            Expences.Columns.EXPENCE_AMOUNT + " real not null, " +
            " foreign key(" + Expences.Columns.EXPENCE_USER_LOGIN + ") references " + Users.TABLE_NAME + "(" + Users.Columns.USER_LOGIN + "));";

    private static final String TABLE_BUYERS = "create table " + Buyers.TABLE_NAME + "( " +
            Buyers.Columns.BUYER_ID + " integer primary key, " +
            Buyers.Columns.BUYER_NAME + " text not null, " +
            Buyers.Columns.BUYER_STREET + " text not null, " +
            Buyers.Columns.BUYER_HOUSE_NR + " text not null, " +
            Buyers.Columns.BUYER_APARTMENT_NR + " text, " +
            Buyers.Columns.BUYER_POSTCODE + " text not null, " +
            Buyers.Columns.BUYER_CITY + " text not null, " +
            Buyers.Columns.BUYER_NIP + " text );";

    private static final String TABLE_RECEIPTS = "create table " + Receipts.TABLE_NAME + "( " +
            Receipts.Columns.RECEIPT_ID + " integer primary key, " +
            Receipts.Columns.RECEIPT_NUMBER + " text not null, " +
            Receipts.Columns.RECEIPT_BUYER_ID + " integer not null, " +
            Receipts.Columns.RECEIPT_USER_LOGIN + " text not null, " +
            Receipts.Columns.RECEIPT_CREATE_DATE + " text not null, " +
            Receipts.Columns.RECEIPT_SALE_DATE + " text not null, " +
            Receipts.Columns.RECEIPT_CITY + " text not null, " +
            Receipts.Columns.RECEIPT_METHOD_OF_PAYMENT + " text not null, " +
            " foreign key(" + Receipts.Columns.RECEIPT_USER_LOGIN + ") references " + Users.TABLE_NAME + "(" + Users.Columns.USER_LOGIN + "), " +
            " foreign key(" + Receipts.Columns.RECEIPT_BUYER_ID + ") references " + Buyers.TABLE_NAME + "(" + Buyers.Columns.BUYER_ID + "));";

    private static final String TABLE_RECEIPT_DETAILS = "create table " + ReceiptDetails.TABLE_NAME + "( " +
            ReceiptDetails.Columns.RECEIPT_DETAILS_ID + " integer primary key, " +
            ReceiptDetails.Columns.RECEIPT_DETAILS_RECEIPT_ID + " text not null, " +
            ReceiptDetails.Columns.RECEIPT_DETAILS_NAME + " text not null, " +
            ReceiptDetails.Columns.RECEIPT_DETAILS_MEASURE + " text not null, " +
            ReceiptDetails.Columns.RECEIPT_DETAILS_QUANTITY + " real not null, " +
            ReceiptDetails.Columns.RECEIPT_DETAILS_UNIT_PRICE + " real not null, " +
            " foreign key(" + ReceiptDetails.Columns.RECEIPT_DETAILS_RECEIPT_ID + ") references " + Receipts.TABLE_NAME + "(" + Receipts.Columns.RECEIPT_ID + "));";

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    static public synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        dataBase.execSQL(TABLE_USERS);
        dataBase.execSQL(TABLE_INCOMES);
        dataBase.execSQL(TABLE_EXPENCES);
        dataBase.execSQL(TABLE_BUYERS);
        dataBase.execSQL(TABLE_RECEIPTS);
        dataBase.execSQL(TABLE_RECEIPT_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        dataBase.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
        dataBase.execSQL("DROP TABLE IF EXISTS " + Incomes.TABLE_NAME);
        dataBase.execSQL("DROP TABLE IF EXISTS " + Expences.TABLE_NAME);
        dataBase.execSQL("DROP TABLE IF EXISTS " + Buyers.TABLE_NAME);
        dataBase.execSQL("DROP TABLE IF EXISTS " + Receipts.TABLE_NAME);
        dataBase.execSQL("DROP TABLE IF EXISTS " + ReceiptDetails.TABLE_NAME);

        onCreate(dataBase);
    }
}
