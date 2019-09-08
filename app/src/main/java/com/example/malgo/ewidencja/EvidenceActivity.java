package com.example.malgo.ewidencja;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.EvidenceAdapter;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ViewPrintPDFAdapter;
import com.example.malgo.ewidencja.pojo.IncomeStr;
import com.example.malgo.ewidencja.pojo.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvidenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner year_spinner;
    private Spinner month_spinner;
    private ListView listView;
    private ArrayList<IncomeStr> incomesList;
    private IncomeDAO incomeDAO;
    private UserDAO userDAO;
    private TextView count;
    private TextView totalAmount;
    private TextView userData;
    private String userLogin;
    private String date;
    private String password;

    List<String> years = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        password = m_global.getPassword();
        userLogin = m_global.getUserLogin();

        year_spinner = (Spinner)findViewById(R.id.evidence_year_spinner);
        month_spinner = (Spinner)findViewById(R.id.evidence_month_spinner);
        listView = (ListView)findViewById(R.id.evidence_incomes_list);
        count = (TextView)findViewById(R.id.evidence_count_tv);
        totalAmount = (TextView)findViewById(R.id.evidence_amount_tv);
        userData = (TextView)findViewById(R.id.evidence_user_tv);
        incomeDAO = new IncomeDAO(this, password);
        userDAO = new UserDAO(this, password);

        setUserData();
        setYearSpinner();
        setMonthSpinner();
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
        DateHelper dateHelper = new DateHelper();

        switch (parent.getId()) {
            case R.id.evidence_year_spinner:
                if (parent.getItemAtPosition(position) != null) {
                    year = parent.getItemAtPosition(position).toString();
                    month = month_spinner.getSelectedItem().toString();
                    date = year + "-" + dateHelper.getMonthAsNumber(month);
                    setIncomesList();
                }
                break;
            case R.id.evidence_month_spinner:
                if (year_spinner.getSelectedItem() != null) {
                    year = year_spinner.getSelectedItem().toString();
                    month = DateHelper.MONTHS_STRING[position].toString();
                    date = year + "-" + dateHelper.getMonthAsNumber(month);
                    setIncomesList();
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.evidence_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.evidence_menu_generate_pdf:
                printDocument();
                break;
            case R.id.evidence_menu_annual_summary:
                this.finish();
                startActivity(new Intent(this, PrintAnnualSummaryActivity.class));
                break;
        }
        return true;
    }

    private void setUserData() {
        User user = userDAO.getUserByLogin(userLogin);
        if (user.getName() == null || user.getSurname() == null || user.getAddress() == null || user.getPostcode() == null || user.getCity() == null ||
                user.getName().isEmpty() || user.getSurname().isEmpty() || user.getAddress().isEmpty() || user.getPostcode().isEmpty() || user.getCity().isEmpty()) {
            userData.setText("Uzupełnij dane prowadzącego działalność w ustawieniach.");
        } else {
            userData.setText(user.getName() + " " + user.getSurname() + ", " + user.getAddress() + ", " + user.getPostcode() + " " + user.getCity());
        }
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

    private void setIncomesList() {

        incomesList = incomeDAO.getAllByMonth(date, userLogin);

        IncomeStr labels = new IncomeStr();
        labels.setIncomeNumber("Lp.");
        labels.setIncomeDate("Data sprzedaży");
        labels.setIncomeAmount("Kwota");
        labels.setIncomeYearAmount("Kwota narastająco \nod początku roku");
        labels.setIncomeAnnotation("Uwagi");
        incomesList.add(0, labels);

        if (incomesList.size() == 1) {
            Toast.makeText(this, "Brak pozycji do wyświetlenia.", Toast.LENGTH_SHORT).show();
        }

        EvidenceAdapter evidenceAdapter = new EvidenceAdapter(this, incomesList);
        listView.setAdapter(evidenceAdapter);

        setSummary();
    }

    private void setSummary() {
        double total_amount = 0;
        for (int i = 1; i < incomesList.size(); ++i) {
            total_amount += Double.parseDouble((incomesList.get(i).getIncomeAmount().replaceAll(",", ".")));
        }

        count.setText(String.valueOf(incomesList.size() - 1));
        totalAmount.setText(String.format("%.2f", total_amount) + " PLN");
    }

    public void printDocument()
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        if (userData.getText().toString().equals("Uzupełnij dane prowadzącego działalność w ustawieniach.")) {
            Toast.makeText(this, "Aby wygenerować PDF uzupełnij dane prowadzącego działalność w ustawieniach.", Toast.LENGTH_LONG).show();
        } else {
            printManager.print(jobName, new ViewPrintPDFAdapter(this, findViewById(R.id.activity_evidence), "Ewidencja_" + date),
                    null);
        }
    }
}
