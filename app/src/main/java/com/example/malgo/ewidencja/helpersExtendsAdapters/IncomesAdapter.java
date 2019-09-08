package com.example.malgo.ewidencja.helpersExtendsAdapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malgo.ewidencja.IncomesListActivity;
import com.example.malgo.ewidencja.R;
import com.example.malgo.ewidencja.db.IncomeDAO;
import com.example.malgo.ewidencja.pojo.Income;

import java.util.ArrayList;

public class IncomesAdapter extends BaseAdapter {
    private IncomesListActivity incomesListActivity;
    private ArrayList<Income> incomesList;
    private LayoutInflater inflater;
    private IncomeDAO incomeDAO;


    public IncomesAdapter(IncomesListActivity _context, IncomeDAO _incomeDAO, ArrayList<Income> _incomesList) {
        this.incomesListActivity = _context;
        this.incomesList = _incomesList;
        inflater = (LayoutInflater.from(_context));
        this.incomeDAO = _incomeDAO;

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
        view = inflater.inflate(R.layout.activity_incomes_list_view, null);
        TextView date = (TextView)view.findViewById(R.id.income_date);
        TextView name = (TextView)view.findViewById(R.id.income_name);
        TextView amount = (TextView)view.findViewById(R.id.income_amount);
        TextView annotation = (TextView)view.findViewById(R.id.income_annotation);

        final Income income = incomesList.get(i);

        date.setText(income.getIncomeDate());
        name.setText(income.getIncomeName());
        amount.setText(String.format("%.2f", income.getIncomeAmount()));
        annotation.setText(income.getIncomeAnnotation() != null ? "Rachunek nr: " + income.getIncomeAnnotation() : "");

        ImageButton delete_btn = (ImageButton)view.findViewById(R.id.incomes_list_delete_btn);
        ImageButton edit_btn = (ImageButton)view.findViewById(R.id.incomes_list_edit_btn);

        if (income.isEditable()) {
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteIncome(income);
                }
            });
            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    incomesListActivity.editIncome(income);
                }
            });
        } else {
            delete_btn.setVisibility(View.GONE);
            edit_btn.setVisibility(View.GONE);
        }

        return view;
    }

    private void deleteIncome(final Income income) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(incomesListActivity)
                .setTitle("Usuń").setMessage("Czy na pewno usunąć przychód?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String date = (income.getIncomeDate()).substring(0, 7);
                        incomeDAO.deleteById(income.getIncomeId());
                        Toast.makeText(incomesListActivity, "Przychód zotał usunięty.", Toast.LENGTH_SHORT).show();
                        incomesListActivity.loadIncomesList(date);
                    }
                })
                .setNegativeButton("Nie", null)
                .setCancelable(false);

        dialog.show();
    }
}
