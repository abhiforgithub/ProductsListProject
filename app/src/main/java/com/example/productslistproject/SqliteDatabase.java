package com.example.productslistproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Products";
    private static final String TABLE_PRODUCTS = "Products";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "productName";
    private static final String COLUMN_PRICE = "productPrice";
    private static final String COLUMN_DESCRIPTION = "productDescription";

    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE "
                + TABLE_PRODUCTS + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " INTEGER,"
                + COLUMN_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    ArrayList<Products> listProducts() {
        String sql = "select * from " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Products> storeProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String price = cursor.getString(2);
                String description = cursor.getString(3);
                storeProducts.add(new Products(id, name, price,description));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeProducts;
    }

    void addProducts(Products products) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, products.getProctName());
        values.put(COLUMN_PRICE, products.getProductPrice());
        values.put(COLUMN_DESCRIPTION, products.getProductDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
    }

    void updateProducts(Products products) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, products.getProctName());
        values.put(COLUMN_PRICE, products.getProductPrice());
        values.put(COLUMN_DESCRIPTION, products.getProductDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(products.getId())});
    }
    void deleteProcuct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
