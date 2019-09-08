package com.example.malgo.ewidencja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.IncomesAdapter;
import com.example.malgo.ewidencja.pojo.Income;

import java.util.ArrayList;
import java.util.List;

public class IncomesListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner year_spinner;
    private Spinner month_spinner;
    private ListView listView;
    private ArrayList<Income> incomesList;
    private IncomeDAO incomeDAO;
    private String userLogin;
    private String password;

    List<String> years = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes_list);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        year_spinner = (Spinner)findViewById(R.id.incomes_list_year_spinner);
        month_spinner = (Spinner)findViewById(R.id.incomes_list_month_spinner);
        listView = (ListView) findViewById(R.id.incomes_list);
        incomeDAO = new IncomeDAO(this, password);
        incomesList = new ArrayList<Income>();


        setYearSpinner();
        setMonthSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.incomes_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.incomes_menu_add_income:
                this.finish();
                startActivity(new Intent(this, NewIncomeActivity.class));
                break;
            case R.id.incomes_menu_receipts_list:
                this.finish();
                startActivity(new Intent(this, ReceiptsListActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String year;
        String month;
        String date;
        DateHelper dateHelper = new DateHelper();
        switch (parent.getId()) {
            case R.id.incomes_list_year_spinner:
                if (parent.getItemAtPosition(position) != null) {
                    year = parent.getItemAtPosition(position).toString();
                    month = month_spinner.getSelectedItem().toString();
                    date = year + "-" + dateHelper.getMonthAsNumber(month);
                    loadIncomesList(date);
                }
                break;
            case R.id.incomes_list_month_spinner:
                if (year_spinner.getSelectedItem() != null) {
                    year = year_spinner.getSelectedItem().toString();
                    month = DateHelper.MONTHS_STRING[position].toString();
                    date = year + "-" + dateHelper.getMonthAsNumber(month);
                    loadIncomesList(date);
                }
                break;
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

    private void setMonthSpinner() {
        ArrayAdapter<CharSequence> monthAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, DateHelper.MONTHS_STRING);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(monthAdapter);

        DateHelper dateHelper = new DateHelper();
        String currentMonth = dateHelper.getMonthFromNumber(incomeDAO.getCurrentMonth(userLogin));
        if (currentMonth != "") {
            int spinnerPosition = monthAdapter.getPosition(currentMonth);
            month_spinner.setSelection(spinnerPosition);
        }

        month_spinner.setOnItemSelectedListener(this);
    }

    public void loadIncomesList(String date) {
        incomesList = incomeDAO.getAllIncomesByMonth(date, userLogin);
        if (incomesList.size() == 0) {
            Toast.makeText(this, "W wybranym miesiącu nie prowadzono żadnych przychodów.", Toast.LENGTH_SHORT).show();
        }

        IncomesAdapter incomesAdapter = new IncomesAdapter(this, incomeDAO, incomesList);
        listView.setAdapter(incomesAdapter);
    }

    public void editIncome(Income income) {
        this.finish();
        Intent intent = new Intent(this, NewIncomeActivity.class);
        intent.putExtra("Id", income.getIncomeId());
        intent.putExtra("Date", income.getIncomeDate());
        intent.putExtra("Name", income.getIncomeName());
        intent.putExtra("Amount", income.getIncomeAmount());
        startActivity(intent);
    }

}
