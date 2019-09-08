package com.example.malgo.ewidencja.db.tables;

public interface Users {
    String TABLE_NAME = "users";

    interface Columns {
        String USER_LOGIN = "login";
        String USER_PASSWORD = "password";
        String USER_NAME = "name";
        String USER_SURNAME = "surname";
        String USER_ADDRESS = "address";
        String USER_POSTCODE = "postcode";
        String USER_CITY = "city";
    }
}
