package com.example.malgo.ewidencja.pojo;

public class IncomeStr {
    private String incomeId;
    private String incomeNumber;
    private String incomeDate;
    private String incomeName;
    private String incomeAmount;
    private String incomeYearAmount;
    private String incomeAnnotation;
    private String userLogin;
    private boolean isEditable;

    public String getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(String incomeId) {
        this.incomeId = incomeId;
    }

    public int getIncomeIdAsInt() { return Integer.parseInt(incomeId); }

    public String getIncomeNumber() {
        return incomeNumber;
    }

    public void setIncomeNumber(String incomeNumber) {
        this.incomeNumber = incomeNumber;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    public String getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getIncomeYearAmount() {
        return incomeYearAmount;
    }

    public void setIncomeYearAmount(String incomeYearAmount) {
        this.incomeYearAmount = incomeYearAmount;
    }

    public String getIncomeAnnotation() {
        return incomeAnnotation;
    }

    public void setIncomeAnnotation(String incomeAnnotation) {
        this.incomeAnnotation = incomeAnnotation;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
