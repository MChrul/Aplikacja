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

import com.example.malgo.ewidencja.ExpencesListActivity;
import com.example.malgo.ewidencja.R;
import com.example.malgo.ewidencja.db.ExpenceDAO;
import com.example.malgo.ewidencja.pojo.Expence;

import java.util.ArrayList;

public class ExpencesAdapter extends BaseAdapter {
    private ExpencesListActivity expencesListActivity;
    private ArrayList<Expence> expencesList;
    private LayoutInflater inflater;
    private ExpenceDAO expenceDAO;


    public ExpencesAdapter(ExpencesListActivity _context, ExpenceDAO _expenceDAO, ArrayList<Expence> _expencesList) {
        this.expencesListActivity = _context;
        this.expencesList = _expencesList;
        inflater = (LayoutInflater.from(_context));
        this.expenceDAO = _expenceDAO;

    }

    @Override
    public int getCount() {
        return expencesList.size();
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

        final Expence expence = expencesList.get(i);

        date.setText(expence.getExpenceDate());
        name.setText(expence.getExpenceKind());
        amount.setText(String.format("%.2f", expence.getExpenceAmount()));

        ImageButton delete_btn = (ImageButton)view.findViewById(R.id.incomes_list_delete_btn);
        ImageButton edit_btn = (ImageButton)view.findViewById(R.id.incomes_list_edit_btn);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteExpence(expence);
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expencesListActivity.editExpence(expence);
            }
        });

        return view;
    }

    private void deleteExpence(final Expence expence) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(expencesListActivity)
                .setTitle("Usuń").setMessage("Czy na pewno usunąć koszt?")
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String date = (expence.getExpenceDate()).substring(0, 7);
                        expenceDAO.deleteById(expence.getExpenceId());
                        Toast.makeText(expencesListActivity, "Koszt zotał usunięty.", Toast.LENGTH_SHORT).show();
                        expencesListActivity.loadExpencesList(date);
                    }
                })
                .setNegativeButton("Nie", null)
                .setCancelable(false);

        dialog.show();
    }
}
