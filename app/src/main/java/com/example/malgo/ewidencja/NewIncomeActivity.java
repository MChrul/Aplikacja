package com.example.malgo.ewidencja;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.pojo.Income;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;

import java.util.Calendar;

public class NewIncomeActivity extends AppCompatActivity {

    private EditText et_date;
    private EditText et_name;
    private EditText et_amount;
    private Income income;
    private IncomeDAO incomeDAO;
    private int id;
    private DatePickerDialog datePickerDialog;
    private String userLogin;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);

        et_date = (EditText)findViewById(R.id.new_income_date);
        et_name = (EditText)findViewById(R.id.new_income_name);
        et_amount = (EditText)findViewById(R.id.new_income_amount);
        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        income = new Income();
        income.setUserLogin(userLogin);
        income.setEditable(true);
        incomeDAO = new IncomeDAO(this, password);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(NewIncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String yearMonthSeparator = ((month < 10) ? "-0" : "-");
                        String monthDaySeparator = ((day < 10) ? "-0" : "-");
                        et_date.setText(year + yearMonthSeparator + (month + 1) + monthDaySeparator + day);
                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = extras.getInt("Id");
                income.setIncomeId(extras.getInt("Id"));
                et_date.setText(extras.getString("Date"));
                et_name.setText(extras.getString("Name"));
                et_amount.setText(Double.valueOf(extras.getDouble("Amount")).toString());
            }
        } catch (Exception ex) {}
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, IncomesListActivity.class));
    }

    public void addNewIncome(View view) {
        DateHelper dateHelper = new DateHelper();
        String inputDate = (et_date.getText().toString());
        String incomeDate = dateHelper.parseDate(inputDate);
        String incomeName = et_name.getText().toString();

        Double incomeAmount;
        String amount = (et_amount.getText().toString()).replace(",", ".");
        if (!amount.isEmpty()) {
            try {
                incomeAmount = Double.parseDouble(amount);

                if (et_date.getText().toString().length() > 0 && incomeName.length() > 0) {
                    income.setIncomeDate(incomeDate);
                    income.setIncomeName(incomeName);
                    income.setIncomeAmount(incomeAmount);

                    if (id == 0) {
                        incomeDAO.insertIncome(income);
                    } else {
                        incomeDAO.update(income);
                    }

                    this.finish();
                    startActivity(new Intent(NewIncomeActivity.this, IncomesListActivity.class));
                }
            } catch (Exception e1) {
                Toast.makeText(this, "Wprowadzona kwota jest niepoprawna", Toast.LENGTH_LONG).show();
                e1.printStackTrace();
            }
        }
    }
}
