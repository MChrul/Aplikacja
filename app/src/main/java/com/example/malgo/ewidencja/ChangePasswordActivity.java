package com.example.malgo.ewidencja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.pojo.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText old_password;
    private EditText new_password;
    private EditText new_password_repeat;
    private UserDAO userDAO;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        old_password = (EditText)findViewById(R.id.change_password_old_password_et);
        new_password = (EditText)findViewById(R.id.change_password_new_password_et);
        new_password_repeat = (EditText)findViewById(R.id.change_password_new_password_repeat_et);
        GlobalClass m_global = (GlobalClass)getApplicationContext();
        password = m_global.getPassword();

        userDAO = new UserDAO(this, password);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void saveNewPassword(View view) {
        String oldPass = old_password.getText().toString();
        String newPass = new_password.getText().toString();
        String newPassRep = new_password_repeat.getText().toString();

        if(oldPass.length() > 0 && newPass.length() > 0 && newPassRep.length() > 0) {
            if(oldPass.equals(newPass)) {
                Toast.makeText(this, "Nowe hasło musi być różne od starego", Toast.LENGTH_SHORT).show();
                new_password.getText().clear();
                new_password_repeat.getText().clear();
            } else {
                GlobalClass m_global = (GlobalClass)getApplicationContext();
                String userLogin = m_global.getUserLogin();
                User user = userDAO.getUserByLogin(userLogin);

                if (user != null && user.getPassword().equals(oldPass)) {
                    if (newPass.equals(newPassRep)) {
                        user.setPassword(newPass);
                        userDAO.updateUser(user);
                        this.finish();
                        startActivity(new Intent(ChangePasswordActivity.this, SettingsActivity.class));
                    } else {
                        Toast.makeText(this, "Nowe hasło i powtórzenie się różnią, wpisz ponownie.", Toast.LENGTH_SHORT).show();
                        new_password.getText().clear();
                        new_password_repeat.getText().clear();
                    }
                } else {
                    Toast.makeText(this, "Podane stare hasło jest nieprawidłowe.", Toast.LENGTH_SHORT).show();
                    old_password.getText().clear();
                    new_password.getText().clear();
                    new_password_repeat.getText().clear();
                }
            }
        }

    }
}
