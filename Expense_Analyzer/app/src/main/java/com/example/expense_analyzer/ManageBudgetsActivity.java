package com.example.expense_analyzer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class ManageBudgetsActivity extends AppCompatActivity {

    private EditText categoryEditText, amountEditText;
    private Spinner timePeriodSpinner;
    private Button setBudgetButton;
    private DatabaseReference userBudgetsRef;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_manage_budgets);

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userBudgetsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid)
                .child("budgets");

        categoryEditText = findViewById(R.id.categoryEditText);
        amountEditText = findViewById(R.id.amountEditText);
        timePeriodSpinner = findViewById(R.id.timePeriodSpinner);
        setBudgetButton = findViewById(R.id.setBudgetButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.time_periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePeriodSpinner.setAdapter(adapter);

        setBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBudget();
            }
        });
    }

    private void setBudget() {
        String category = categoryEditText.getText().toString().trim();
        String amountString = amountEditText.getText().toString().trim();
        String timePeriod = timePeriodSpinner.getSelectedItem().toString();

        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(amountString)) {
            double amount = Double.parseDouble(amountString);
            if (amount >= 0) {
                saveBudget(category, amount, timePeriod);
            } else {
                amountEditText.setError("Amount must be a positive number");
            }
        } else {
            categoryEditText.setError("Category and amount are required");
        }
    }

    private void saveBudget(String category, double amount, String timePeriod) {
        Map<String, Object> budgetMap = new HashMap<>();
        budgetMap.put("category", category);
        budgetMap.put("amount", amount);

        DatabaseReference timePeriodRef = userBudgetsRef.child(timePeriod);
        timePeriodRef.child(category).setValue(budgetMap);
        categoryEditText.setText("");
        amountEditText.setText("");
    }
}
