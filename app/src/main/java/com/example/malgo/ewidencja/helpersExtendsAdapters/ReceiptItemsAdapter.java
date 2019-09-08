package com.example.malgo.ewidencja.helpersExtendsAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.malgo.ewidencja.CreateReceipt;
import com.example.malgo.ewidencja.R;
import com.example.malgo.ewidencja.pojo.ReceiptDetailStr;

import java.util.ArrayList;

public class ReceiptItemsAdapter extends BaseAdapter {
//    private CreateReceipt createReceiptActivity;
    private ArrayList<ReceiptDetailStr> receiptsItemsList;
    private LayoutInflater inflater;

    public ReceiptItemsAdapter(Context context, ArrayList<ReceiptDetailStr> _receiptsItemsList) {
//        this.createReceiptActivity = _createReceipt;
        this.receiptsItemsList = _receiptsItemsList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return receiptsItemsList.size();
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
        view = inflater.inflate(R.layout.receipt_item_list_view, null);

        TextView position = (TextView)view.findViewById(R.id.receipt_item_position);
        TextView name = (TextView)view.findViewById(R.id.receipt_item_lv_name);
        TextView measure = (TextView)view.findViewById(R.id.receipt_item_lv_measure);
        TextView quantity = (TextView)view.findViewById(R.id.receipt_item_lv_qty);
        TextView unitPrice = (TextView)view.findViewById(R.id.receipt_item_lv_unit_price);
        TextView totalValue = (TextView)view.findViewById(R.id.receipt_item_lv_total);

        final ReceiptDetailStr receiptItem = receiptsItemsList.get(i);

        position.setText(receiptItem.getPosition());
        name.setText(receiptItem.getName());
        measure.setText(receiptItem.getMeasure());
        quantity.setText(receiptItem.getQuantity());
        unitPrice.setText(receiptItem.getUnit_price());
        totalValue.setText(receiptItem.getTotalValue());

        return view;
    }
}
