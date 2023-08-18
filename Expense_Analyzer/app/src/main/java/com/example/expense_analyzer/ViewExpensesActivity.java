package com.example.expense_analyzer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewExpensesActivity extends AppCompatActivity {

    private RecyclerView expensesRecyclerView;
    private ExpensesAdapter expensesAdapter;
    private DatabaseReference expensesRef;
    private List<Expense> expenseList;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_view_expenses);

        expensesRef = FirebaseDatabase.getInstance().getReference("expenses");
        expenseList = new ArrayList<>();

        expensesRecyclerView = findViewById(R.id.expensesRecyclerView);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        expensesAdapter = new ExpensesAdapter(expenseList);
        expensesRecyclerView.setAdapter(expensesAdapter);

        // Load expenses from Firebase and update the RecyclerView
        loadExpenses();
    }

    private void loadExpenses() {
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expenseList.clear();

                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }

                expensesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
