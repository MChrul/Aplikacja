package com.example.malgo.ewidencja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.pojo.User;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EditText userLogin;
    private EditText userPassword;
    private UserDAO userDAO;
    private String pass;
    private GlobalClass m_global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_global = (GlobalClass)getApplicationContext();
        pass = m_global.getPassword();

        if (pass == null) {
            getPassword("Podaj hasło do uruchomienia aplikacji");
        } else {
            init();
        }
    }

    public void init() {
        userDAO = new UserDAO(this, pass, m_global);
        if (!m_global.isPasswordCorrect()) {
            getPassword("Niepoprawne hasło, wprowadź hasło do uruchomienia aplikacji");
        }
        Button btn_register = (Button)findViewById(R.id.btn_register);
        userLogin = (EditText)findViewById(R.id.main_login);
        userPassword = (EditText)findViewById(R.id.main_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    public void getPassword(String msg) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.app_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);
        final EditText app_password = (EditText)promptsView.findViewById(R.id.app_password_pass);
        final TextView app_message = (TextView)promptsView.findViewById(R.id.app_password_message_tv);

        if (doesDatabaseExist(this, "Ewidencja.db")) {
            app_message.setText(msg);
        } else {
            app_message.setText("Utwórz hasło do dostępu do aplikacji, zapisz je w bezpiecznym miejscu, " +
                    "ponieważ w razie jego utraty nie ma możliwości odzyskania, bądź zresetowania tego hasła.");
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                pass = app_password.getText().toString();
                                m_global.setPassword(pass);
                                init();
                            }
                        })
                .setNegativeButton("anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                closeApp();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void logIn(View view) {
        String login = userLogin.getText().toString();
        String password = userPassword.getText().toString();

        if (login.length() > 0 && password.length() > 0) {
            User user = userDAO.getUserByLogin(login);

            if (user != null) {
                if (user.getPassword().equals(password)) {
                    m_global.setUserLogin(user.getLogin());
                    this.finish();
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(this, "Niepoprawne hasło", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Brak konta o podanym loginie",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Podaj login i hasło", Toast.LENGTH_LONG).show();
        }
    }

    public void closeApp() {
        this.finish();
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
