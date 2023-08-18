package com.example.expense_analyzer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText amountEditText, dateEditText, descriptionEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private DatabaseReference userExpensesRef;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_add_expense);

        // Get the current user's UID
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set the database reference to the user's expenses
        userExpensesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid)
                .child("expenses");

        amountEditText = findViewById(R.id.amountEditText);
        dateEditText = findViewById(R.id.dateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExpense();
            }
        });
    }

    private void saveExpense() {
        String amount = amountEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String selectedCategory = categorySpinner.getSelectedItem().toString();

        // Create a new Expense object
        Expense expense = new Expense(amount, date, description, selectedCategory);

        // Push the expense to the user's expenses in the database
        userExpensesRef.push().setValue(expense);

        // Optionally, handle success and error scenarios here
        finish(); // Finish the activity and return to the previous screen
    }
}
