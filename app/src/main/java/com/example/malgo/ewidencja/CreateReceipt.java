package com.example.malgo.ewidencja;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malgo.ewidencja.db.BuyerDAO;
import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.db.ReceiptDAO;
import com.example.malgo.ewidencja.db.ReceiptDetailsDAO;
import com.example.malgo.ewidencja.db.UserDAO;
import com.example.malgo.ewidencja.helpersExtendsAdapters.AmountHelper;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ReceiptItemsAdapter;
import com.example.malgo.ewidencja.helpersExtendsAdapters.ViewPrintPDFAdapter;
import com.example.malgo.ewidencja.pojo.Buyer;
import com.example.malgo.ewidencja.pojo.Income;
import com.example.malgo.ewidencja.pojo.Receipt;
import com.example.malgo.ewidencja.pojo.ReceiptDetail;
import com.example.malgo.ewidencja.pojo.ReceiptDetailStr;
import com.example.malgo.ewidencja.pojo.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateReceipt extends AppCompatActivity {

    final Context context = this;
    private Buyer buyer;
    private Receipt receipt;
    private UserDAO userDAO;
    private User user;
    private BuyerDAO buyerDAO;
    private ReceiptDAO receiptDAO;
    private ReceiptDetailsDAO receiptDetailsDAO;
    private IncomeDAO incomeDAO;
    private DatePickerDialog datePickerDialog;
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
    private MenuItem add_buyer_data;
    private String msg;
    private boolean listViewFitsOnScreen;
    private String password;
    Runnable fitsOnScreen = new Runnable() {
        @Override
        public void run() {
            int last = listView.getLastVisiblePosition();
            if(last == listView.getCount() - 1 && listView.getChildAt(last).getBottom() < listView.getHeight()) {
                listViewFitsOnScreen = true;
            }
            else {
                listViewFitsOnScreen = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_receipt);

        init();
        setReceiptNumber();
        setUserData();
        setReceiptInitialData();
        setItemsList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if (position == 1) return;
                final ReceiptDetail selectedItem = receiptItemsList.get(position - 1);

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.popup_add_sales_item, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText itemName = (EditText)promptsView.findViewById(R.id.popup_sales_item_name);
                final EditText itemMeasure = (EditText)promptsView.findViewById(R.id.popup_sales_item_measure);
                final EditText itemQuantity = (EditText)promptsView.findViewById(R.id.popup_sales_item_quantity);
                final EditText itemUnitPrice = (EditText)promptsView.findViewById(R.id.popup_sales_item_unit_price);
                itemQuantity.setInputType(InputType.TYPE_CLASS_PHONE);
                itemUnitPrice.setInputType(InputType.TYPE_CLASS_PHONE);

                itemName.setText(selectedItem.getName());
                itemMeasure.setText(selectedItem.getMeasure());
                itemQuantity.setText(String.valueOf(selectedItem.getQuantity()));
                itemUnitPrice.setText(String.valueOf(selectedItem.getUnit_price()));

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("usuń",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        receiptItemsList.remove(position - 1);
                                        dialog.cancel();
                                        setItemsList();
                                    }
                                });

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                boolean isOk = true;
                                receiptItemsList.get(position - 1).setName(itemName.getText().toString());
                                receiptItemsList.get(position - 1).setMeasure(itemMeasure.getText().toString());
                                if (receiptItemsList.get(position - 1).getName() == null || receiptItemsList.get(position - 1).getMeasure() == null || receiptItemsList.get(position - 1).getName().isEmpty() || receiptItemsList.get(position - 1).getMeasure().isEmpty()) {
                                    isOk= false;
                                }
                                try{
                                    receiptItemsList.get(position - 1).setQuantity(Double.parseDouble(itemQuantity.getText().toString()));
                                } catch (Exception e1) {
                                    isOk = false;
                                    e1.printStackTrace();
                                }
                                try{
                                    receiptItemsList.get(position - 1).setUnit_price(Double.parseDouble(itemUnitPrice.getText().toString()));
                                } catch (Exception e1) {
                                    isOk = false;
                                    e1.printStackTrace();
                                }
                                if (isOk) {
                                    setItemsList();
                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wprowadzone dane są niepoprawne.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receipt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.receipt_menu_buyer_data:
                enterBuyerData();
                break;
            case R.id.receipt_menu_receipt_data:
                enterReceiptData();
                break;
            case R.id.receipt_menu_add_item:
                addSalesItem();
                break;
            case R.id.receipt_menu_print_to_pdf:
                printDocument();
                break;
        }
        return true;
    }

    public void init() {
        totalReceiptValue = (TextView)findViewById(R.id.create_receipt_total_value);
        listView = (ListView)findViewById(R.id.create_receipt_items_lv);
        receiptItemsList = new ArrayList<ReceiptDetail>();
        name_tv = (TextView)findViewById(R.id.create_receipt_name_surname);
        street_tv = (TextView)findViewById(R.id.create_receipt_street);
        city_tv = (TextView)findViewById(R.id.create_receipt_postcode_city);
        nip_tv = (TextView)findViewById(R.id.create_receipt_nip);
        place_date_tv = (TextView)findViewById(R.id.create_receipt_place_create_date);
        sale_date_tv = (TextView)findViewById(R.id.create_receipt_sale_date);
        payment_method_tv = (TextView)findViewById(R.id.create_receipt_method_of_payment);
        receipt_nr_tv = (TextView)findViewById(R.id.create_receipt_receipt_number);
        user_name_tv = (TextView)findViewById(R.id.create_receipt_user_name_surname);
        user_street_tv = (TextView)findViewById(R.id.create_receipt_user_street);
        user_city_tv = (TextView)findViewById(R.id.create_receipt_user_postcode_city);
        add_buyer_data = (MenuItem)findViewById(R.id.receipt_menu_buyer_data);
        amount_in_words_tv = (TextView)findViewById(R.id.create_receipt_amount_in_words);
        listViewFitsOnScreen = true;

        GlobalClass m_global = (GlobalClass)getApplicationContext();
        userLogin = m_global.getUserLogin();
        password = m_global.getPassword();

        buyer = new Buyer();
        receipt = new Receipt();
        buyerDAO = new BuyerDAO(this, password);
        receiptDAO = new ReceiptDAO(this, password);
        receiptDetailsDAO = new ReceiptDetailsDAO(this, password);
        incomeDAO = new IncomeDAO(this, password);
        userDAO = new UserDAO(this, password);
        user = userDAO.getUserByLogin(userLogin);

        listView.post(fitsOnScreen);
    }

    public void setUserData() {
        user_name_tv.setText(user.getName() + " " + user.getSurname());
        user_street_tv.setText(user.getAddress());
        user_city_tv.setText(user.getPostcode() + " " + user.getCity());
    }

    public void setReceiptNumber() {
        int lastNumber = receiptDAO.getLastNrOfReceipt(userLogin);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        receipt.setNumber(lastNumber + 1 + "/" + month + "/" + year);
        receipt_nr_tv.setText("Rachunek nr " + receipt.getNumber());
    }

    public void setReceiptInitialData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        receipt.setCreateDate(dateFormat.format(date));
        receipt.setMethodOfPayment("gotówka");
        receipt.setUserLogin(userLogin);
    }

    public void enterBuyerData() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_buyer_data_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText buyerName = (EditText)promptsView.findViewById(R.id.popup_buyer_name);
        final EditText buyerStreet = (EditText)promptsView.findViewById(R.id.popup_buyer_street);
        final EditText buyerHouseNr = (EditText)promptsView.findViewById(R.id.popup_buyer_house_number);
        final EditText buyerApartmentNr = (EditText)promptsView.findViewById(R.id.popup_buyer_apartment_number);
        final EditText buyerPostcode = (EditText)promptsView.findViewById(R.id.popup_buyer_postcode);
        final EditText buyerCity = (EditText)promptsView.findViewById(R.id.popup_buyer_city);
        final EditText buyerNip = (EditText)promptsView.findViewById(R.id.popup_buyer_NIP);
        buyerPostcode.setInputType(InputType.TYPE_CLASS_PHONE);
        buyerNip.setInputType(InputType.TYPE_CLASS_PHONE);

        buyerName.setText(buyer.getName());
        buyerStreet.setText(buyer.getStreet());
        buyerHouseNr.setText(buyer.getHouse_nr());
        buyerApartmentNr.setText(buyer.getApartment_nr());
        buyerPostcode.setText(buyer.getPostcode());
        buyerCity.setText(buyer.getCity());
        buyerNip.setText(buyer.getNip());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                buyer.setName(buyerName.getText().toString());
                                buyer.setStreet(buyerStreet.getText().toString());
                                buyer.setHouse_nr(buyerHouseNr.getText().toString());
                                buyer.setApartment_nr(buyerApartmentNr.getText().toString());
                                buyer.setPostcode(buyerPostcode.getText().toString());
                                buyer.setCity(buyerCity.getText().toString());
                                buyer.setNip(buyerNip.getText().toString());
                                setBuyerData();
                            }
                        })
                .setNegativeButton("anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setBuyerData() {
        if (!buyer.getName().isEmpty()) {
            name_tv.setText(buyer.getName());
        }
        if ((buyer.getStreet().isEmpty() || buyer.getStreet().equals("")) && !buyer.getHouse_nr().isEmpty()) {
            street_tv.setText(buyer.getCity() + " " + buyer.getHouse_nr() + (!buyer.getApartment_nr().isEmpty() ? "/" + buyer.getApartment_nr() : ""));
        } else if (!buyer.getStreet().isEmpty()){
            street_tv.setText(buyer.getStreet() + " " + buyer.getHouse_nr()  + (!buyer.getApartment_nr().isEmpty() ? "/" + buyer.getApartment_nr() : ""));
        }
        if (!buyer.getPostcode().isEmpty() || !buyer.getCity().isEmpty()) {
            city_tv.setText(buyer.getPostcode() + " " + buyer.getCity());
        }
        nip_tv.setText("NIP: " + buyer.getNip());
    }

    public void enterReceiptData() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_receipt_data, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText receiptSaleDate = (EditText)promptsView.findViewById(R.id.popup_receipt_sale_date);
        final EditText receiptCity = (EditText)promptsView.findViewById(R.id.popup_receipt_city);
        final RadioButton cash_rb = (RadioButton)promptsView.findViewById(R.id.popup_receipt_cash_rb);
        final RadioButton transfer_rb = (RadioButton)promptsView.findViewById(R.id.popup_receipt_transfer_rb);

        receiptSaleDate.setText(receipt.getSaleDate());
        receiptCity.setText(receipt.getCity());

        receiptSaleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CreateReceipt.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String yearMonthSeparator = ((month < 10) ? "-0" : "-");
                        String monthDaySeparator = ((day < 10) ? "-0" : "-");

                        receiptSaleDate.setText(year + yearMonthSeparator + (month + 1) + monthDaySeparator + day);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        if (receipt.getMethodOfPayment() != null && receipt.getMethodOfPayment().equals("przelew")) {
            transfer_rb.setChecked(true);
        } else {
            cash_rb.setChecked(true);
        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                receipt.setSaleDate(receiptSaleDate.getText().toString());
                                receipt.setCity(receiptCity.getText().toString());
                                setReceiptData();
                            }
                        })
                .setNegativeButton("anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create and show alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setReceiptData() {
        if (receipt.getCity().isEmpty()) {
            place_date_tv.setText("Miejscowość, dnia " + receipt.getCreateDate());
        } else {
            place_date_tv.setText(receipt.getCity() + ", dnia " + receipt.getCreateDate());
        }
        if (!receipt.getSaleDate().isEmpty()) {
            sale_date_tv.setText("Data sprzedaży: " + receipt.getSaleDate());
        }
        if (!receipt.getMethodOfPayment().isEmpty()) {
            payment_method_tv.setText("Sposób płatności: " + receipt.getMethodOfPayment());
        }
    }

    public void onRadioButtonMethodOfPaymentClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.popup_receipt_cash_rb:
                if (checked)
                    receipt.setMethodOfPayment("gotówka");
                    break;
            case R.id.popup_receipt_transfer_rb:
                if (checked)
                    receipt.setMethodOfPayment("przelew");
                    break;
        }
    }

    public void addSalesItem() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_add_sales_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText itemName = (EditText)promptsView.findViewById(R.id.popup_sales_item_name);
        final EditText itemMeasure = (EditText)promptsView.findViewById(R.id.popup_sales_item_measure);
        final EditText itemQuantity = (EditText)promptsView.findViewById(R.id.popup_sales_item_quantity);
        final EditText itemUnitPrice = (EditText)promptsView.findViewById(R.id.popup_sales_item_unit_price);
        itemQuantity.setInputType(InputType.TYPE_CLASS_PHONE);
        itemUnitPrice.setInputType(InputType.TYPE_CLASS_PHONE);

        final ReceiptDetail receiptDetail = new ReceiptDetail();

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton("anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean isOk = true;
                        receiptDetail.setName(itemName.getText().toString());
                        receiptDetail.setMeasure(itemMeasure.getText().toString());
                        if (receiptDetail.getName() == null || receiptDetail.getMeasure() == null || receiptDetail.getName().isEmpty() || receiptDetail.getMeasure().isEmpty()) {
                            isOk= false;
                        }
                        try{
                            receiptDetail.setQuantity(Double.parseDouble(itemQuantity.getText().toString()));
                        } catch (Exception e1) {
                            isOk = false;
                            e1.printStackTrace();
                        }
                        try{
                            receiptDetail.setUnit_price(Double.parseDouble(itemUnitPrice.getText().toString()));
                        } catch (Exception e1) {
                            isOk = false;
                            e1.printStackTrace();
                        }
                        if (isOk) {
                            receiptItemsList.add(receiptDetail);
                            setItemsList();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wprowadzone dane są niepoprawne.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        listView.post(fitsOnScreen);
        alertDialog.show();
    }

    private void setItemsList() {
        if (!listViewFitsOnScreen) {
            Toast.makeText(this, "Ostatnia pozycja się nie zmieści. Wydrukuj ten PDF i utwórz nowy rachunek.", Toast.LENGTH_SHORT).show();
            receiptItemsList.remove(receiptItemsList.size() - 1);
        }

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

    private void calculateValueOfReceipt() {
        double sum = 0.0;

        for (int i=0; i < receiptItemsList.size(); ++i) {
            sum += receiptItemsList.get(i).getQuantity() * receiptItemsList.get(i).getUnit_price();
        }

        totalReceiptValue.setText(String.format( "%.2f", sum) + " PLN");

        AmountHelper amountHelper = new AmountHelper();
        amount_in_words_tv.setText("Słownie: " + amountHelper.amountInWords(sum));
    }

    public void fillDataAlert() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                this);

        alertDialog2.setTitle("Uzupełnij dane");
        alertDialog2.setMessage(msg);

        alertDialog2.setPositiveButton("OK", null);
        alertDialog2.show();
    }

    public boolean checkIfAllNecessaryDataAreFilled() {
        msg = "Należy uzupełnić:";
        boolean everythingIsCompleted = true, separator = false;

        if (!buyer.isComplete()) {
            msg += " dane nabywcy (nazwa, adres)";
            everythingIsCompleted = false;
            separator = true;
        }
        if (!receipt.isComplete()) {
            msg += (separator ? "," : "") + " dane rachunku (data sprzedaży, miejsce wystawienia)";
            everythingIsCompleted = false;
            separator = true;
        }
        if (receiptItemsList.isEmpty()) {
            msg += (separator ? "," : "") + " dokument musi posiadać co najmniej jedną pozycję sprzedaży";
            everythingIsCompleted = false;
        }
        if (!everythingIsCompleted) {
            msg += ".";
            fillDataAlert();
        }

        return everythingIsCompleted;
    }

    public void insertReceiptIntoDB() {
        buyerDAO.insert(buyer);
        receipt.setBuyerId(buyerDAO.getLastInsertedId());
        GlobalClass m_global = (GlobalClass)getApplicationContext();
        receipt.setUserLogin(m_global.getUserLogin());
        receiptDAO.insert(receipt);
        int receiptId = receiptDAO.getLastInsertedId();

        for (int i = 0; i < receiptItemsList.size(); ++i) {
            receiptItemsList.get(i).setReceipt_id(receiptId);
            receiptDetailsDAO.insert(receiptItemsList.get(i));
        }
    }

    public void insertIncomesIntoDB() {
        Income income = new Income();
        income.setUserLogin(userLogin);
        income.setIncomeDate(receipt.getSaleDate());
        income.setIncomeAnnotation(receipt.getNumber());
        for (int i =0; i < receiptItemsList.size(); ++i) {
            income.setIncomeAmount(receiptItemsList.get(i).getQuantity() * receiptItemsList.get(i).getUnit_price());
            income.setIncomeName(receiptItemsList.get(i).getName());
            incomeDAO.insertIncome(income);
        }
    }

    public void printDocument()
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) + " Document";

        if (checkIfAllNecessaryDataAreFilled()) {
            insertReceiptIntoDB();
            insertIncomesIntoDB();
            printManager.print(jobName, new ViewPrintPDFAdapter(this,
                            findViewById(R.id.create_receipt_lt),
                            "Rachunek_nr_" + receipt.getNumber().replace("/", "_")),
                    null);
            finihedPrintingAndGoBack();
        }
    }

    public void finihedPrintingAndGoBack() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                this);

        alertDialog2.setTitle("Wygenerowano dokument.");
        alertDialog2.setMessage("Możesz go ponownie wygenerować w liście rachunków dostępnej z menu w widoku przychodów.");

        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CreateReceipt.this.finish();
                        startActivity(new Intent(CreateReceipt.this, DashboardActivity.class));
                    }
                });
        alertDialog2.show();
    }
}
