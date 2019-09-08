package com.example.malgo.ewidencja.pojo;

public class Expence {
    private Integer expenceId;
    private String expenceDate;
    private String expenceKind;
    private Double expenceAmount;
    private String userLogin;

    public Integer getExpenceId() {
        return expenceId;
    }

    public void setExpenceId(Integer expenceId) {
        this.expenceId = expenceId;
    }

    public String getExpenceDate() {
        return expenceDate;
    }

    public void setExpenceDate(String expenceDate) {
        this.expenceDate = expenceDate;
    }

    public String getExpenceKind() {
        return expenceKind;
    }

    public void setExpenceKind(String expenceKind) {
        this.expenceKind = expenceKind;
    }

    public Double getExpenceAmount() {
        return expenceAmount;
    }

    public void setExpenceAmount(Double expenceAmount) {
        this.expenceAmount = expenceAmount;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
