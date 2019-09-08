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

import com.example.malgo.ewidencja.db.ExpenceDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ExpencesAdapter;
import com.example.malgo.ewidencja.pojo.Expence;

import java.util.ArrayList;
import java.util.List;

public class ExpencesListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner year_spinner;
    private Spinner month_spinner;
    private ListView listView;
    private ArrayList<Expence> expencesList;
    private ExpenceDAO expenceDAO;
    private String userLogin;
    private String password;

    List<String> years = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expences_list);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        password = m_global.getPassword();
        userLogin = m_global.getUserLogin();

        year_spinner = (Spinner)findViewById(R.id.expences_list_year_spinner);
        month_spinner = (Spinner)findViewById(R.id.expences_list_month_spinner);
        listView = (ListView) findViewById(R.id.expence_list);
        expenceDAO = new ExpenceDAO(this, password);
        expencesList = new ArrayList<Expence>();

        setYearSpinner();
        setMonthSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_expence_ic:
                this.finish();
                startActivity(new Intent(this, NewExpenceActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String year;
        String month;
        String date;
        DateHelper dateHelper = new DateHelper();
        switch (parent.getId()) {
            case R.id.expences_list_year_spinner:
                if (parent.getItemAtPosition(position) != null){
                    year = parent.getItemAtPosition(position).toString();
                    month = month_spinner.getSelectedItem().toString();
                    date = year + "-" + dateHelper.getMonthAsNumber(month);
                    loadExpencesList(date);
                }
                break;
            case R.id.expences_list_month_spinner:
                if (year_spinner.getSelectedItem() != null) {
                    year = year_spinner.getSelectedItem().toString();
                    month = DateHelper.MONTHS_STRING[position].toString();
                    date = year + "-" + dateHelper.getMonthAsNumber(month);
                    loadExpencesList(date);
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private void setYearSpinner() {
        years = expenceDAO.getAllYears(userLogin);
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
        String currentMonth = dateHelper.getMonthFromNumber(expenceDAO.getCurrentMonth(userLogin));
        if (currentMonth != "") {
            int spinnerPosition = monthAdapter.getPosition(currentMonth);
            month_spinner.setSelection(spinnerPosition);
        }

        month_spinner.setOnItemSelectedListener(this);
    }

    public void loadExpencesList(String date) {
        expencesList = expenceDAO.getAllExpencesByMonth(date, userLogin);
        if (expencesList.size() == 0) {
            Toast.makeText(this, "W wybranym miesiącu nie prowadzono żadnych kosztów.", Toast.LENGTH_SHORT).show();
        }

        ExpencesAdapter expencesAdapter = new ExpencesAdapter(this, expenceDAO, expencesList);
        listView.setAdapter(expencesAdapter);
    }

    public void editExpence(Expence expence) {
        this.finish();
        Intent intent = new Intent(this, NewExpenceActivity.class);
        intent.putExtra("Id", expence.getExpenceId());
        intent.putExtra("Date", expence.getExpenceDate());
        intent.putExtra("Name", expence.getExpenceKind());
        intent.putExtra("Amount", expence.getExpenceAmount());
        startActivity(intent);
    }

}
