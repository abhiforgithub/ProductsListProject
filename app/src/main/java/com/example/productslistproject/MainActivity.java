package com.example.productslistproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SqliteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView productsList = findViewById(R.id.productsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(linearLayoutManager);
        productsList.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        ArrayList<Products> allProducts = mDatabase.listProducts();

        if (allProducts.size() > 0) {
            productsList.setVisibility(View.VISIBLE);
            ProductsAdapter mAdapter = new ProductsAdapter(this, allProducts);
            productsList.setAdapter(mAdapter);
        }
        else {
            productsList.setVisibility(View.GONE);
            Toast.makeText(this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskDialog();
            }
        });
    }

    private void addTaskDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_products, null);
        final EditText nameField = subView.findViewById(R.id.enterName);
        final EditText priceField = subView.findViewById(R.id.enterPrice);
        final EditText descriptionField = subView.findViewById(R.id.enterDescription);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new PRODUCT");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String price = priceField.getText().toString();
                final String description = descriptionField.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else {
                    Products newProduct = new Products(name, price, description);
                    mDatabase.addProducts(newProduct);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
}