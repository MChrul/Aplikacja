package com.example.malgo.ewidencja;

import android.app.Application;

public class GlobalClass extends Application {

    private String userLogin;
    private String password;
    private boolean passwordIsCorrect;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPasswordCorrect() {
        return passwordIsCorrect;
    }

    public void setPasswordIsCorrect(boolean passwordIsCorrect) {
        this.passwordIsCorrect = passwordIsCorrect;
    }
}
