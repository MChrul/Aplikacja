package com.example.malgo.ewidencja;

import android.content.Context;
import android.content.Intent;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.ExpenceDAO;
import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ViewPrintPDFAdapter;
import com.example.malgo.ewidencja.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class PrintAnnualSummaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView user_data_tv;
    private TextView month1;
    private TextView month2;
    private TextView month3;
    private TextView month4;
    private TextView month5;
    private TextView month6;
    private TextView month7;
    private TextView month8;
    private TextView month9;
    private TextView month10;
    private TextView month11;
    private TextView month12;
    private TextView incomes_total;
    private TextView expences_tv;
    private TextView profit_tv;
    private Spinner year_spinner;
    private IncomeDAO incomeDAO;
    private UserDAO userDAO;
    private ExpenceDAO expenceDAO;
    private String userLogin;
    private Double incomesSum;
    private Double expencesSum;
    private String year;
    private String password;

    List<String> years = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_annual_summary);

        init();
        fillAllValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.print_annual_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.print_annual_menu_print:
                printDocument();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, EvidenceActivity.class));
    }

    private void init() {

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        user_data_tv = (TextView)findViewById(R.id.print_annual_user_data_tv);
        month1 = (TextView)findViewById(R.id.print_annual_month1);
        month2 = (TextView)findViewById(R.id.print_annual_month2);
        month3 = (TextView)findViewById(R.id.print_annual_month3);
        month4 = (TextView)findViewById(R.id.print_annual_month4);
        month5 = (TextView)findViewById(R.id.print_annual_month5);
        month6 = (TextView)findViewById(R.id.print_annual_month6);
        month7 = (TextView)findViewById(R.id.print_annual_month7);
        month8 = (TextView)findViewById(R.id.print_annual_month8);
        month9 = (TextView)findViewById(R.id.print_annual_month9);
        month10 = (TextView)findViewById(R.id.print_annual_month10);
        month11 = (TextView)findViewById(R.id.print_annual_month11);
        month12 = (TextView)findViewById(R.id.print_annual_month12);
        incomes_total = (TextView)findViewById(R.id.print_annual_month_total);
        expences_tv = (TextView)findViewById(R.id.print_annual_expences);
        profit_tv = (TextView)findViewById(R.id.print_annual_profit);
        year_spinner = (Spinner) findViewById(R.id.print_annual_year_spinner);
        incomeDAO = new IncomeDAO(this, password);
        userDAO = new UserDAO(this, password);
        expenceDAO = new ExpenceDAO(this, password);

        incomesSum = 0.0;
        expencesSum = 0.0;

        setYearSpinner();
    }

    private void fillAllValues() {
        setUserData();
    }

    private void setUserData() {
        User user = userDAO.getUserByLogin(userLogin);
        if (user.getName() == null || user.getSurname() == null || user.getAddress() == null || user.getPostcode() == null || user.getCity() == null ||
                user.getName().isEmpty() || user.getSurname().isEmpty() || user.getAddress().isEmpty() || user.getPostcode().isEmpty() || user.getCity().isEmpty()) {
            user_data_tv.setText("Uzupełnij dane prowadzącego działalność w ustawieniach.");
        } else {
            user_data_tv.setText(user.getName() + " " + user.getSurname() + ", " + user.getAddress() + ", " + user.getPostcode() + " " + user.getCity());
        }
    }

    private void loadAnnualSummary() {
        setIncomes();
        setExpences();
        setProfit();
    }

    private void setIncomes() {
        ArrayList<Double> incomes = new ArrayList<>();
        DateHelper dateHelper = new DateHelper();

        for (int i = 1; i <= 12; ++i) {
            Double incomesAmount = incomeDAO.getSumByMonth(year + "-" + dateHelper.getMonthAsNumber(i), userLogin);
            incomes.add(incomesAmount);
            incomesSum += incomesAmount;
        }

        month1.setText(String.format("%.2f", incomes.get(0)));
        month2.setText(String.format("%.2f", incomes.get(1)));
        month3.setText(String.format("%.2f", incomes.get(2)));
        month4.setText(String.format("%.2f", incomes.get(3)));
        month5.setText(String.format("%.2f", incomes.get(4)));
        month6.setText(String.format("%.2f", incomes.get(5)));
        month7.setText(String.format("%.2f", incomes.get(6)));
        month8.setText(String.format("%.2f", incomes.get(7)));
        month9.setText(String.format("%.2f", incomes.get(8)));
        month10.setText(String.format("%.2f", incomes.get(9)));
        month11.setText(String.format("%.2f", incomes.get(10)));
        month12.setText(String.format("%.2f", incomes.get(11)));
        incomes_total.setText(String.format("%.2f", incomesSum));
    }

    private void setExpences() {
        expencesSum = expenceDAO.getSumByYear(year, userLogin);
        expences_tv.setText(String.format("%.2f", expencesSum));
    }

    private void setProfit() {
        profit_tv.setText(String.format("%.2f", incomesSum - expencesSum));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) != null) {
            year = parent.getItemAtPosition(position).toString();
            loadAnnualSummary();
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void setYearSpinner() {
        years = incomeDAO.getAllYears(userLogin);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(yearAdapter);
        year_spinner.setOnItemSelectedListener(this);
    }

    public void printDocument()
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        if (user_data_tv.getText().toString().equals("Uzupełnij dane prowadzącego działalność w ustawieniach.")) {
            Toast.makeText(this, "Aby wygenerować PDF uzupełnij dane prowadzącego działalność w ustawieniach.", Toast.LENGTH_LONG).show();
        } else {
            printManager.print(jobName, new ViewPrintPDFAdapter(this, findViewById(R.id.print_annual_summary_lt), "Ewidencja_podsumowanie_roczne_" + year),
                    null);
        }
    }
}
