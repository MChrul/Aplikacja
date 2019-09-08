package com.example.malgo.ewidencja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.ReceiptDAO;
import com.example.malgo.ewidencja.db.ReceiptDetailsDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.DateHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ReceiptAdapter;
import com.example.malgo.ewidencja.pojo.Receipt;
import com.example.malgo.ewidencja.pojo.ReceiptDetail;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsListActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private Spinner yearSpinner;
    private ListView receiptsListView;
    private ReceiptDAO receiptDAO;
    private ReceiptDetailsDAO receiptDetailsDAO;
    private String userLogin;
    private ArrayList<Receipt> receipts;
    private List<String> years;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_list);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        yearSpinner = (Spinner)findViewById(R.id.receipt_list_year_spinner);
        receiptsListView = (ListView)findViewById(R.id.receipt_list_list_view);
        receiptDAO = new ReceiptDAO(this, password);
        receiptDetailsDAO = new ReceiptDetailsDAO(this, password);

        setYearSpinner();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, IncomesListActivity.class));
    }

    private void setYearSpinner() {
        years = receiptDAO.getAllYears(userLogin);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String year;
        if (parent.getItemAtPosition(position) != null) {
            year = parent.getItemAtPosition(position).toString();
            loadReceiptsList(year);
            }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void loadReceiptsList(String year) {
        receipts = receiptDAO.getAllByYear(year, userLogin);
        if (receipts.size() == 0) {
            Toast.makeText(this, "W wybranym roku nie wystawiono żadnych rachunków.", Toast.LENGTH_LONG).show();
        }

        ReceiptAdapter receiptAdapter = new ReceiptAdapter(this, receiptDetailsDAO, receipts);
        receiptsListView.setAdapter(receiptAdapter);
    }

    public void printPdf(Receipt receipt) {
        this.finish();
        Intent intent = new Intent(this, PrintReceiptActivity.class);
        intent.putExtra("Id", receipt.getId());
        startActivity(intent);
    }
}
