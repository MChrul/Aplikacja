package com.example.malgo.ewidencja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.pojo.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText userLogin;
    private EditText userPassword;
    private UserDAO userDAO;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userLogin = (EditText)findViewById(R.id.et_new_user_login);
        userPassword = (EditText)findViewById(R.id.et_new_user_password);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        password = m_global.getPassword();

        userDAO = new UserDAO(this, password);
    }

    public void addNewUser(View view) {
        User user = new User();
        String login = userLogin.getText().toString();
        String password = userPassword.getText().toString();

        if (login.length() > 0 && password.length() > 0) {
            user.setLogin(login);
            user.setPassword(password);

            if (userDAO.getUserByLogin(user.getLogin()) != null)
            {
                Toast.makeText(this, "Konto o podanym loginie już istnieje.", Toast.LENGTH_SHORT).show();
            } else {
                userDAO.insertUser(user);
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
         Toast.makeText(this, "Oba pola muszą zostać uzupełnione", Toast.LENGTH_SHORT).show();
        }
    }
}
