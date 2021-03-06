package com.wit.edu.leachc1.discoop;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Discoop
 * Senior Project - Computer Science
 * Created by Crissy Leach and Sam Kanner
 * Wentworth Institute of Technology
 */

public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "discountsInformation";
    // Contacts table name
    private static final String TABLE_DISCOUNTS = "discounts";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ADDR = "address";
    private static final String KEY_EXPR = "expiration";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_NAME = "name";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DISCOUNTS_TABLE = "CREATE TABLE " + TABLE_DISCOUNTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TYPE + " TEXT, " + KEY_ADDR + " TEXT, "
                + KEY_EXPR + " TEXT, " + KEY_DETAILS + " TEXT, " + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_DISCOUNTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISCOUNTS);
    // Creating tables again
        onCreate(db);
    }

    // Adding new discount
    public void addDiscount(Discount discount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, discount.getType()); // Discount Type
        values.put(KEY_ADDR, discount.getAddress()); // Discount Address
        values.put(KEY_EXPR, discount.getExpiration()); // Expiration Date
        values.put(KEY_DETAILS, discount.getDetails()); // Discount Details
        values.put(KEY_NAME, discount.getName()); //Establishment name

        //Inserting row
        db.insert(TABLE_DISCOUNTS, null, values);
        db.close(); // Close database connection
    }

    // Getting one discount
    public Discount getDiscount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DISCOUNTS, new String[] { KEY_ID,
        KEY_TYPE, KEY_ADDR, KEY_EXPR, KEY_DETAILS, KEY_NAME }, KEY_ID + "=?",
                new String[] {String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Discount discount = new Discount(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return discount
        return discount;
    }

    // Getting all discounts
    public List<Discount> getAllDiscounts() {
        List<Discount> discountList = new ArrayList<Discount>();
        // Select All query
        String selectQuery = "SELECT * FROM " + TABLE_DISCOUNTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Discount discount = new Discount();
                discount.setId(Integer.parseInt(cursor.getString(0)));
                discount.setType(cursor.getString(1));
                discount.setAddress(cursor.getString(2));
                discount.setExpiration(cursor.getString(3));
                discount.setDetails(cursor.getString(4));
                discount.setName(cursor.getString(5));
                // Adding discount to list
                discountList.add(discount);
            } while (cursor.moveToNext());
        }
        //return discount list
        return discountList;
    }

    // Getting discounts count
    public int getDiscountsCount() {
        String countQuery = "SELECT * FROM " + TABLE_DISCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    // Updating a discount
    public int updateDiscount(Discount discount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, discount.getType());
        values.put(KEY_ADDR, discount.getAddress());
        values.put(KEY_EXPR, discount.getExpiration());
        values.put(KEY_DETAILS, discount.getDetails());
        values.put(KEY_NAME, discount.getName());

        // updating row
        return db.update(TABLE_DISCOUNTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(discount.getId())});
    }

    // Deleting a discount
    public void deleteDiscount(Discount discount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DISCOUNTS, KEY_ID + " = ?",
                new String[] { String.valueOf(discount.getId()) });
        db.close();
    }
}
