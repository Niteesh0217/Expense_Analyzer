package com.example.expense_analyzer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity {

    private EditText categoryNameEditText;
    private Button addCategoryButton;
    private ListView categoryListView;
    private List<String> categoryList;
    private ArrayAdapter<String> categoryAdapter;
    private DatabaseReference userCategoriesRef;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_manage_categories);

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userCategoriesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid)
                .child("categories");

        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        addCategoryButton = findViewById(R.id.addCategoryButton);
        categoryListView = findViewById(R.id.categoryListView);

        categoryList = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        categoryListView.setAdapter(categoryAdapter);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedCategory = categoryList.get(position);
                showEditDeleteDialog(selectedCategory);
            }
        });

        loadCategories();
    }

    private void addCategory() {
        String categoryName = categoryNameEditText.getText().toString();
        if (!categoryName.isEmpty()) {
            DatabaseReference currentUserCategoriesRef = userCategoriesRef.child(categoryName);
            currentUserCategoriesRef.setValue(true); // Use a boolean value as a placeholder
            categoryNameEditText.setText("");
        }
    }

    private void showEditDeleteDialog(final String categoryName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit or Delete Category");

        final EditText input = new EditText(this);
        input.setText(categoryName);
        builder.setView(input);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategoryName = input.getText().toString();
                editCategory(categoryName, newCategoryName);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCategory(categoryName);
            }
        });

        builder.show();
    }

    private void editCategory(final String oldCategoryName, final String newCategoryName) {
        if (!newCategoryName.isEmpty()) {
            DatabaseReference oldCategoryRef = userCategoriesRef.child(oldCategoryName);
            DatabaseReference newCategoryRef = userCategoriesRef.child(newCategoryName);

            oldCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Object categoryValue = dataSnapshot.getValue();
                    oldCategoryRef.removeValue(); // Delete the old category
                    newCategoryRef.setValue(categoryValue); // Create the new category
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void deleteCategory(String categoryName) {
        DatabaseReference categoryRef = userCategoriesRef.child(categoryName);
        categoryRef.removeValue();
    }

    private void loadCategories() {
        userCategoriesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                String category = dataSnapshot.getKey();
                categoryList.add(category);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // Handle category update
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String category = dataSnapshot.getKey();
                categoryList.remove(category);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // Handle category move
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
