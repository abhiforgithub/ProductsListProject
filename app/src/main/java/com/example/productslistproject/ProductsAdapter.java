package com.example.productslistproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Products> listProducts;
    private ArrayList<Products> mArrayList;
    private SqliteDatabase mDatabase;
    public ProductsAdapter(Context context, ArrayList<Products> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        this.mArrayList = listProducts;
        mDatabase = new SqliteDatabase(context);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull ProductsAdapter.ViewHolder holder, int position) {
        final Products products = listProducts.get(position);
        holder.productName.setText(products.getProctName());
        holder.productPrice.setText(products.getProductPrice());
        holder.productDescription.setText(products.getProductDescription());
        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(products);
            }
        });
        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.deleteProcuct(products.getId());
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }

    private void editTaskDialog(Products products) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_products, null);
        final EditText nameField = subView.findViewById(R.id.enterName);
        final EditText priceField = subView.findViewById(R.id.enterPrice);
        final EditText descriptionField = subView.findViewById(R.id.enterDescription);
        if (products != null) {
            nameField.setText(products.getProctName());
            priceField.setText(String.valueOf(products.getProductPrice()));
            descriptionField.setText(String.valueOf(products.getProductDescription()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit product");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String price = priceField.getText().toString();
                final String description = descriptionField.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    mDatabase.updateProducts(new
                            Products(Objects.requireNonNull(products).getId(), name, price, description));
                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listProducts = mArrayList;
                }
                else {
                    ArrayList<Products> filteredList = new ArrayList<>();
                    for (Products products : mArrayList) {
                        if (products.getProctName().toLowerCase().contains(charString)) {
                            filteredList.add(products);
                        }
                    }
                    listProducts = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listProducts;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listProducts = (ArrayList<Products>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice,productDescription;
        AppCompatImageView editProduct,deleteProduct;
        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            editProduct = itemView.findViewById(R.id.editProduct);
            deleteProduct = itemView.findViewById(R.id.deleteProduct);
        }
    }
}
