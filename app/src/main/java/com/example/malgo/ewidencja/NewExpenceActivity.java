package com.example.malgo.ewidencja;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.ExpenceDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.pojo.Expence;

import java.util.Calendar;

public class NewExpenceActivity extends AppCompatActivity {

    private EditText et_date;
    private EditText et_kind;
    private EditText et_amount;
    private ExpenceDAO expenceDAO;
    private Expence expence;
    private int id;
    private DatePickerDialog datePickerDialog;
    private String userLogin;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expence);

        et_date = (EditText)findViewById(R.id.new_expence_date_et);
        et_kind = (EditText)findViewById(R.id.new_expence_kind_et);
        et_amount = (EditText)findViewById(R.id.new_expence_amount_et);
        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(NewExpenceActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        expence = new Expence();
        expence.setUserLogin(userLogin);
        expenceDAO = new ExpenceDAO(this, password);

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = extras.getInt("Id");
                expence.setExpenceId(extras.getInt("Id"));
                et_date.setText(extras.getString("Date"));
                et_kind.setText(extras.getString("Name"));
                et_amount.setText(Double.valueOf(extras.getDouble("Amount")).toString());
            }
        } catch (Exception ex) {}
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, ExpencesListActivity.class));
    }

    public void addNewExpence(View view) {
        DateHelper dateHelper = new DateHelper();
        String inputDate = (et_date.getText().toString());
        String expenceDate = dateHelper.parseDate(inputDate);
        String expenceKind = et_kind.getText().toString();
        Double expenceAmount;
        String amount = et_amount.getText().toString();
        if (!amount.isEmpty()) {
            try {
                expenceAmount = Double.parseDouble(amount);

                if (et_date.getText().toString().length() > 0 && expenceKind.length() > 0) {
                    expence.setExpenceDate(expenceDate);
                    expence.setExpenceKind(expenceKind);
                    expence.setExpenceAmount(expenceAmount);
                }

                if (id == 0) {
                    expenceDAO.insertExpence(expence);
                } else {
                    expenceDAO.update(expence);
                }

                finish();
                startActivity(new Intent(NewExpenceActivity.this, ExpencesListActivity.class));
            } catch (Exception e1) {
                Toast.makeText(this, "Wprowadzona kwota jest niepoprawna", Toast.LENGTH_LONG).show();
                e1.printStackTrace();
            }
        }
    }
}
