package com.example.rasheduzzaman.interfacedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by annabelle on 12/3/15.
 */
public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "googlemapaddress_db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_ADDRESS = "CREATE TABLE IF NOT EXISTS " + AddressTb.TABLE  + "("
                + AddressTb.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + AddressTb.ADDRESS + " TEXT, "
                + AddressTb.MODIFYTIME + " DATETIME);";

        db.execSQL(CREATE_TABLE_ADDRESS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + AddressTb.TABLE);

        // Create tables again
        onCreate(db);

    }

}