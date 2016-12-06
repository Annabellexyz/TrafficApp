package com.example.rasheduzzaman.interfacedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by annabelle on 12/3/15.
 */
public class AddressRepo {
    private DBHelper dbHelper;

    public AddressRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(AddressTb addressDb) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(AddressTb.ADDRESS, addressDb.address_data);
        values.put(AddressTb.MODIFYTIME,addressDb.modify_time);

        // Inserting Row
        long address_id = db.insert(AddressTb.TABLE, null, values);
        //db.endTransaction();
        db.close(); // Closing database connection
        return (int) address_id;
    }

    public void delete(int address_id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(AddressTb.TABLE, AddressTb.KEY_ID + "= ?", new String[] { String.valueOf(address_id) });
        db.close(); // Closing database connection
    }

    public void update(AddressTb addressTb) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(AddressTb.ADDRESS, addressTb.address_data);
        values.put(AddressTb.MODIFYTIME,addressTb.modify_time);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(AddressTb.TABLE, values, AddressTb.KEY_ID + "= ?", new String[] { String.valueOf(addressTb.address_id) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getAddressList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AddressTb.KEY_ID + "," +
                AddressTb.ADDRESS + "," +
                AddressTb.MODIFYTIME +
                " FROM " + AddressTb.TABLE + " order by " + AddressTb.KEY_ID + " desc ;";

        //Student student = new Student();
        ArrayList<HashMap<String, String>> addressList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> addr = new HashMap<String, String>();
                addr.put("id", cursor.getString(cursor.getColumnIndex(AddressTb.KEY_ID)));
                addr.put("addr", cursor.getString(cursor.getColumnIndex(AddressTb.ADDRESS)));
                addressList.add(addr);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return addressList;

    }

    public AddressTb getAddressById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AddressTb.KEY_ID + "," +
                AddressTb.ADDRESS + "," +
                AddressTb.MODIFYTIME +
                " FROM " + AddressTb.TABLE
                + " WHERE " +
                AddressTb.KEY_ID + "=? ;";

        int iCount =0;
        AddressTb addressTb = new AddressTb();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                addressTb.address_id =cursor.getInt(cursor.getColumnIndex(AddressTb.KEY_ID));
                addressTb.address_data =cursor.getString(cursor.getColumnIndex(AddressTb.ADDRESS));
                addressTb.modify_time  =cursor.getString(cursor.getColumnIndex(AddressTb.MODIFYTIME));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return addressTb;
    }

}