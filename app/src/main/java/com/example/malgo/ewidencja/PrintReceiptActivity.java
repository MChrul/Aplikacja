package com.example.malgo.ewidencja;

import android.content.Context;
import android.content.Intent;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.malgo.ewidencja.db.BuyerDAO;
import com.example.malgo.ewidencja.db.ReceiptDAO;
import com.example.malgo.ewidencja.db.ReceiptDetailsDAO;
import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.AmountHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ReceiptItemsAdapter;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ViewPrintPDFAdapter;
import com.example.malgo.ewidencja.pojo.Buyer;
import com.example.malgo.ewidencja.pojo.Receipt;
import com.example.malgo.ewidencja.pojo.ReceiptDetail;
import com.example.malgo.ewidencja.pojo.ReceiptDetailStr;
import com.example.malgo.ewidencja.pojo.User;

import java.util.ArrayList;

public class PrintReceiptActivity extends AppCompatActivity {

    final Context context = this;
    private Buyer buyer;
    private Receipt receipt;
    private User user;
    private UserDAO userDAO;
    private BuyerDAO buyerDAO;
    private ReceiptDAO receiptDAO;
    private ReceiptDetailsDAO receiptDetailsDAO;
    private ListView listView;
    private TextView totalReceiptValue;
    private ArrayList<ReceiptDetail> receiptItemsList;
    private TextView name_tv;
    private TextView street_tv;
    private TextView city_tv;
    private TextView nip_tv;
    private TextView place_date_tv;
    private TextView sale_date_tv;
    private TextView payment_method_tv;
    private TextView receipt_nr_tv;
    private TextView user_name_tv;
    private TextView user_street_tv;
    private TextView user_city_tv;
    private TextView amount_in_words_tv;
    private String userLogin;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_receipt);

        init();
        getReceiptData();
        setTextViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.print_receipt_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, ReceiptsListActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.print_receipt_menu_print:
                printDocument();
                break;
        }
        return true;
    }

    public void init() {
        totalReceiptValue = (TextView)findViewById(R.id.print_receipt_total_value);
        listView = (ListView)findViewById(R.id.print_receipt_items_lv);
        receiptItemsList = new ArrayList<ReceiptDetail>();
        name_tv = (TextView)findViewById(R.id.print_receipt_name_surname);
        street_tv = (TextView)findViewById(R.id.print_receipt_street);
        city_tv = (TextView)findViewById(R.id.print_receipt_postcode_city);
        nip_tv = (TextView)findViewById(R.id.print_receipt_nip);
        place_date_tv = (TextView)findViewById(R.id.print_receipt_place_create_date);
        sale_date_tv = (TextView)findViewById(R.id.print_receipt_sale_date);
        payment_method_tv = (TextView)findViewById(R.id.print_receipt_method_of_payment);
        receipt_nr_tv = (TextView)findViewById(R.id.print_receipt_receipt_number);
        user_name_tv = (TextView)findViewById(R.id.print_receipt_user_name_surname);
        user_street_tv = (TextView)findViewById(R.id.print_receipt_user_street);
        user_city_tv = (TextView)findViewById(R.id.print_receipt_user_postcode_city);
        amount_in_words_tv = (TextView)findViewById(R.id.print_receipt_amount_in_words);

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        buyer = new Buyer();
        receipt = new Receipt();
        userDAO = new UserDAO(this, password);
        buyerDAO = new BuyerDAO(this, password);
        receiptDAO = new ReceiptDAO(this, password);
        receiptDetailsDAO = new ReceiptDetailsDAO(this, password);
        user = userDAO.getUserByLogin(userLogin);

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int id = extras.getInt("Id");
                receipt.setId(id);
            }
        } catch (Exception ex) {}
    }

    public void getReceiptData() {
        receipt = receiptDAO.getById(receipt.getId());
        buyer = buyerDAO.getBuyerById(receipt.getBuyerId());
        receiptItemsList = receiptDetailsDAO.getDetailsByReceipt(receipt);
    }

    public void setTextViews() {
        name_tv.setText(buyer.getName());
        street_tv.setText(((buyer.getStreet() == null || buyer.getStreet().isEmpty()) ? buyer.getCity() : buyer.getStreet()) + " " + buyer.getHouse_nr()
                + ((buyer.getApartment_nr() == null || buyer.getApartment_nr().isEmpty()) ? "" : "/" + buyer.getApartment_nr()));
        city_tv.setText(buyer.getPostcode() + " " + buyer.getCity());
        nip_tv.setText("NIP: " + buyer.getNip());
        place_date_tv.setText(receipt.getCity() + ", dnia " + receipt.getCreateDate());
        sale_date_tv.setText("Data sprzedaży: " + receipt.getSaleDate());
        payment_method_tv.setText("Sposób płatności: " + receipt.getMethodOfPayment());
        receipt_nr_tv.setText(receipt.getNumber());
        user_name_tv.setText(user.getName() + " " + user.getSurname());
        user_street_tv.setText(user.getAddress());
        user_city_tv.setText(user.getPostcode() + " " + user.getCity());
        calculateValueOfReceipt();
        setItemsList();
    }

    private void calculateValueOfReceipt() {
        double sum = 0.0;

        for (int i=0; i < receiptItemsList.size(); ++i) {
            sum += receiptItemsList.get(i).getQuantity() * receiptItemsList.get(i).getUnit_price();
        }

        totalReceiptValue.setText(String.format( "%.2f", sum) + " PLN");

        AmountHelper amountHelper = new AmountHelper();
        amount_in_words_tv.setText("Słownie: " + amountHelper.amountInWords(sum));
    }

    private void setItemsList() {
        ArrayList<ReceiptDetailStr> receiptItemsStrList = new ArrayList<ReceiptDetailStr>();
        ReceiptDetailStr labels = new ReceiptDetailStr();

        labels.setPosition("Lp.");
        labels.setName("Nazwa towaru / usługi");
        labels.setMeasure("Miara");
        labels.setQuantity("Ilość");
        labels.setUnit_price("Cena jednostkowa");
        labels.setTotalValue("Wartość");

        receiptItemsStrList.add(labels);

        for (int i = 0; i < receiptItemsList.size(); ++i) {
            ReceiptDetailStr receiptDetailStr = receiptItemsList.get(i).mapReceipDetailToString();
            receiptDetailStr.setPosition(String.valueOf(i + 1));
            receiptItemsStrList.add(receiptDetailStr);
        }

        ReceiptItemsAdapter receiptItemsAdapter = new ReceiptItemsAdapter(this, receiptItemsStrList);
        listView.setAdapter(receiptItemsAdapter);

        calculateValueOfReceipt();
    }

    public void printDocument()
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new ViewPrintPDFAdapter(this, findViewById(R.id.print_receipt_lt), "Rachunek_nr_" + receipt.getNumber().replace("/", "_")),
                null);
    }
}
