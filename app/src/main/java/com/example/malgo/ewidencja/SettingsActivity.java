package com.example.malgo.ewidencja;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.ExpenceDAO;
import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.db.ReceiptDAO;
import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.pojo.User;

public class SettingsActivity extends AppCompatActivity {

    private UserDAO userDAO;
    private ExpenceDAO expenceDAO;
    private IncomeDAO incomeDAO;
    private ReceiptDAO receiptDAO;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        password = m_global.getPassword();

        userDAO = new UserDAO(this, password);
        expenceDAO = new ExpenceDAO(this, password);
        incomeDAO = new IncomeDAO(this, password);
        receiptDAO = new ReceiptDAO(this, password);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void openUserData(View view) {
        this.finish();
        startActivity(new Intent(SettingsActivity.this, UserDataActivity.class));
    }

    public void changePassword(View view) {
        this.finish();
        startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
    }

    public void deleteUser(View view) {
        GlobalClass m_global = (GlobalClass)getApplicationContext();
        final String userLogin = m_global.getUserLogin();
        
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Usuń konto").setMessage("Wszystkie wprowadzone dane zostaną bezpowrotnie usunięte. Czy na pewno chcesz usunąć konto?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userDAO.deleteUserByLogin(userLogin);
                        expenceDAO.deleteByUserLogin(userLogin);
                        incomeDAO.deleteByUserLogin(userLogin);
                        receiptDAO.deleteByUserLogin(userLogin);
                        Toast.makeText(getApplicationContext(), "Konto zotało usunięte.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton("Nie", null)
                .setCancelable(false);

        dialog.show();

    }
}
