package com.example.malgo.ewidencja.db.tables;


public interface Incomes {
    String TABLE_NAME = "incomes";

    interface Columns {
        String INCOME_ID = "id";
        String INCOME_DATE = "date";
        String INCOME_NAME = "name";
        String INCOME_AMOUNT = "amount";
        String INCOME_ANNOTATION = "annotation";
        String INCOME_USER_LOGIN = "user_login";
        String INCOME_IS_EDITABLE = "is_editable";
    }
}
