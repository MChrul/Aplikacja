package com.example.malgo.ewidencja.db.tables;

public interface Expences {
    String TABLE_NAME = "expences";

    interface Columns {
        String EXPENCE_ID = "id";
        String EXPENCE_DATE = "date";
        String EXPENCE_NAME = "name";
        String EXPENCE_AMOUNT = "amount";
        String EXPENCE_USER_LOGIN = "user_login";
    }
}
