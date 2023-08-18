package com.example.expense_analyzer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
/*import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate; */
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ExpenseAnalysisActivity extends AppCompatActivity {

   // private PieChart pieChart;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_expense_analysis);

       // pieChart = findViewById(R.id.pieChart);

        retrieveExpenseDataAndDisplayChart();
    }

    private void retrieveExpenseDataAndDisplayChart() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid)
                .child("expenses")
                .child("monthly") // Specify the desired time period
                .child("food");  // Specify the desired category

       // expensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            //@Override
           /* public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    double amount = expenseSnapshot.child("amount").getValue(Double.class);
                    String description = expenseSnapshot.child("description").getValue(String.class);
                    entries.add(new PieEntry((float) amount, description));
                }

                PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setValueTextColor(android.R.color.black);
                dataSet.setValueTextSize(12f);

                PieData data = new PieData(dataSet);
                pieChart.setData(data);

                pieChart.getDescription().setEnabled(false);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setHoleRadius(25f);
                pieChart.setTransparentCircleRadius(30f);

                pieChart.invalidate(); // Refresh the chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });*/
    }
}

