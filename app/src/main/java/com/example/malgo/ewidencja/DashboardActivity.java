package com.example.malgo.ewidencja;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.ExpenceDAO;
import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.pojo.Income;
import com.example.malgo.ewidencja.pojo.User;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private TextView current_incomes_sum;
    private TextView current_expences_sum;
    private TextView current_balance;
    private TextView current_left;
    private TextView total_incomes;
    private TextView total_expences;
    private TextView total_balance;
    private IncomeDAO incomeDAO;
    private ExpenceDAO expenceDAO;
    private String userLogin;
    private UserDAO userDAO;
    private User user;
    private String password;
    private Double incomesLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
        setAllTextViews();
        checkIfLimitWasExceeded();
    }

    public void init() {
        GlobalClass m_global = (GlobalClass)getApplicationContext();
        password = m_global.getPassword();

        incomesLimit = 1125.00;

        incomeDAO = new IncomeDAO(this, password);
        expenceDAO = new ExpenceDAO(this, password);
        userDAO = new UserDAO(this, password);

        current_incomes_sum = findViewById(R.id.dashboard_current_incomes_tv);
        current_expences_sum = findViewById(R.id.dashboard_current_expences_tv);
        current_balance = findViewById(R.id.dashboard_current_balance_tv);
        current_left = findViewById(R.id.dashboard_left_tv);
        total_incomes = findViewById(R.id.dashboard_year_incomes_tv);
        total_expences = findViewById(R.id.dashboard_year_expences_tv);
        total_balance = findViewById(R.id.dashboard_year_balance_tv);
        userLogin = m_global.getUserLogin();
        user = userDAO.getUserByLogin(userLogin);
    }

    public void setAllTextViews() {
        Double currentIncomes = incomeDAO.getSumOfActualMonth(userLogin);
        Double currentLeft = incomesLimit - currentIncomes;
        Double currentExpences = expenceDAO.getSumOfActualMonth(userLogin);
        Double currentBalance = currentIncomes - currentExpences;

        Double totalIncomes = incomeDAO.getSumOfActualYear(userLogin);
        Double totalExpences = expenceDAO.getSumOfActualYear(userLogin);
        Double totalBalance = totalIncomes - totalExpences;

        current_incomes_sum.setText(String.format("%.2f", currentIncomes));
        current_expences_sum.setText(String.format("%.2f", currentExpences));
        current_balance.setText(String.format("%.2f", currentBalance));
        current_left.setText(String.format("%.2f", currentLeft));
        total_incomes.setText(String.format("%.2f", totalIncomes));
        total_expences.setText(String.format("%.2f", totalExpences));
        total_balance.setText(String.format("%.2f", totalBalance));
    }

    public void openCreateReceipt(View view) {
        if (userDataAreComplete()) {
            this.finish();
            startActivity(new Intent(DashboardActivity.this, CreateReceipt.class));
        } else {
            Toast.makeText(this,"Uzupełnij dane prowadzącego działalność w ustawieniach, aby móc wystawiać rachunki.", Toast.LENGTH_LONG).show();
        }
    }

    public void openIncomesList(View view) {
        this.finish();
        startActivity(new Intent(DashboardActivity.this, IncomesListActivity.class));
    }

    public void openExpenseList(View view) {
        this.finish();
        startActivity(new Intent(DashboardActivity.this, ExpencesListActivity.class));
    }

    public void openEvidence(View view) {
        this.finish();
        startActivity(new Intent(DashboardActivity.this, EvidenceActivity.class));
    }

    public void openSettings(View view) {
        this.finish();
        startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
    }

    public void checkIfLimitWasExceeded() {
        ArrayList<String> years = incomeDAO.getAllYears(userLogin);
        DateHelper dateHelper = new DateHelper();
        boolean wasExceeded = false;

        for (int ind = years.size() - 1; (ind >= 0) && (!wasExceeded); --ind) {
            for (int i = 0; (i < DateHelper.MONTHS_STRING.length) && (!wasExceeded); ++i) {
                String date = years.get(ind) + "-" +
                        dateHelper.getMonthAsNumber(DateHelper.MONTHS_STRING[i].toString());
                ArrayList<Income> incomes = incomeDAO.getAllIncomesByMonth(date, userLogin);

                double sum = 0;
                for (int index = 0; index < incomes.size(); ++index) {
                    if ((sum += incomes.get(index).getIncomeAmount()) > incomesLimit) {
                        displayAlert(incomes.get(index));
                        wasExceeded = true;
                        break;
                    }
                }
            }
        }
    }

    private void displayAlert(Income income) {
        String alert_message = "Dnia " + income.getIncomeDate() +
                " został przekroczony miesięczny limit przychodów, " +
                "masz 7 dni od tego dnia, na zarejestrowanie działalności gospodarczej.";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Uwaga").setMessage(alert_message)
                .setPositiveButton("Ok", null)
                .setCancelable(false);
        dialog.show();
    }

    private boolean userDataAreComplete () {
        if (user.getName() == null || user.getSurname() == null || user.getAddress() == null || user.getPostcode() == null || user.getCity() == null ||
                user.getName().isEmpty() || user.getSurname().isEmpty() || user.getAddress().isEmpty() || user.getPostcode().isEmpty() || user.getCity().isEmpty()) {
            return false;
        }
        return true;
    }
}
