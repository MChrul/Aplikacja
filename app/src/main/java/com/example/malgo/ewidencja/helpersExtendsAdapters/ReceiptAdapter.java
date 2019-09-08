package com.example.malgo.ewidencja.helpersExtendsAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.malgo.ewidencja.R;
import com.example.malgo.ewidencja.ReceiptsListActivity;
import com.example.malgo.ewidencja.db.ReceiptDetailsDAO;
import com.example.malgo.ewidencja.pojo.Receipt;
import com.example.malgo.ewidencja.pojo.ReceiptDetail;

import java.util.ArrayList;

public class ReceiptAdapter extends BaseAdapter {
    private ReceiptsListActivity receiptsListActivity;
    private ArrayList<Receipt> receipts;
    private LayoutInflater inflater;
    private ReceiptDetailsDAO receiptDetailsDAO;


    public ReceiptAdapter(ReceiptsListActivity _context, ReceiptDetailsDAO _receiptDetailsDAO, ArrayList<Receipt> _receipts) {
        this.receiptsListActivity = _context;
        this.receipts = _receipts;
        inflater = (LayoutInflater.from(_context));
        this.receiptDetailsDAO = _receiptDetailsDAO;

    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_receipts_list_view, null);
        TextView date = (TextView)view.findViewById(R.id.receipt_list_date);
        TextView number = (TextView)view.findViewById(R.id.receipt_list_number);
        TextView amount = (TextView)view.findViewById(R.id.receipt_list_amount);

        final Receipt receipt = receipts.get(i);

        ArrayList<ReceiptDetail> details = receiptDetailsDAO.getDetailsByReceipt(receipt);
        double total = 0.0;
        for(int idx=0; idx < details.size(); ++idx) {
            total += details.get(idx).getQuantity() * details.get(idx).getUnit_price();
        }

        date.setText("Data wystawienia: " + receipt.getCreateDate());
        number.setText("Numer: " + receipt.getNumber());
        amount.setText("Wartość: " + String.format("%.2f", total));

        ImageButton print_pdf_btn = (ImageButton)view.findViewById(R.id.receipt_list_generate_pdf);

        print_pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiptsListActivity.printPdf(receipt);
            }
        });

        return view;
    }
}
