package com.example.malgo.ewidencja.pojo;

public class Buyer {
    private int id;
    private String name;
    private String street;
    private String house_nr;
    private String apartment_nr;
    private String postcode;
    private String city;
    private String nip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse_nr() {
        return house_nr;
    }

    public void setHouse_nr(String house_nr) {
        this.house_nr = house_nr;
    }

    public String getApartment_nr() {
        return apartment_nr;
    }

    public void setApartment_nr(String apartment_nr) {
        this.apartment_nr = apartment_nr;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public boolean isComplete() {
        if (name == null || house_nr == null || postcode == null || city == null || name.isEmpty() || house_nr.isEmpty() || postcode.isEmpty() || city.isEmpty()) {
            return false;
        }
        return true;
    }
}
