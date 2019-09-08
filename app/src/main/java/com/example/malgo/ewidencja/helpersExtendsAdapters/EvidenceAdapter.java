package com.example.malgo.ewidencja.helpersExtendsAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.malgo.ewidencja.EvidenceActivity;
import com.example.malgo.ewidencja.R;
import com.example.malgo.ewidencja.pojo.IncomeStr;

import java.util.ArrayList;

public class EvidenceAdapter extends BaseAdapter {
    private EvidenceActivity evidenceActivity;
    private ArrayList<IncomeStr> incomesList;
    private LayoutInflater inflater;

    public EvidenceAdapter(EvidenceActivity _evidenceActivity, ArrayList<IncomeStr> _incomesList) {
        this.evidenceActivity = _evidenceActivity;
        this.incomesList = _incomesList;
        inflater = (LayoutInflater.from(_evidenceActivity));
    }

    @Override
    public int getCount() {
        return incomesList.size();
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
        view = inflater.inflate(R.layout.activity_evidence_list_view, null);

        TextView number = (TextView)view.findViewById(R.id.evidence_lv_nr);
        TextView date = (TextView)view.findViewById(R.id.evidence_lv_date);
        TextView amount = (TextView)view.findViewById(R.id.evidence_lv_amount);
        TextView yearAmount = (TextView)view.findViewById(R.id.evidence_lv_year_amount);
        TextView annotation = (TextView)view.findViewById(R.id.evidence_lv_annotation);

        final IncomeStr income = incomesList.get(i);

        number.setText(income.getIncomeNumber());
        date.setText(income.getIncomeDate());
        amount.setText(income.getIncomeAmount());
        yearAmount.setText(income.getIncomeYearAmount());
        annotation.setText(income.getIncomeAnnotation());

        return view;
    }
}
