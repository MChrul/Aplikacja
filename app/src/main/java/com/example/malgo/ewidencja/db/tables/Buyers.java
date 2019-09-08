package com.example.malgo.ewidencja.db.tables;

public interface Buyers {
    String TABLE_NAME = "buyers";

    interface Columns {
        String BUYER_ID = "id";
        String BUYER_NAME = "name";
        String BUYER_STREET = "street";
        String BUYER_HOUSE_NR = "house_nr";
        String BUYER_APARTMENT_NR = "apartment_nr";
        String BUYER_POSTCODE = "postcode";
        String BUYER_CITY = "city";
        String BUYER_NIP = "nip";
    }
}
