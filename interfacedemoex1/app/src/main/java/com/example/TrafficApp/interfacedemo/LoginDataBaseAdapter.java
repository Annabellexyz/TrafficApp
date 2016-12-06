package com.example.rasheduzzaman.interfacedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class LoginDataBaseAdapter 
{
		static final String DATABASE_NAME = "login3.db";
		static final int DATABASE_VERSION = 1;
		public static final int NAME_COLUMN = 1;
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String DATABASE_CREATE = "create table "+"LOGIN"+
		                             "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text,EMAIL text,PHONE text,ADDRESS text,IMAGE BLOB,FRIENDS text); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  LoginDataBaseAdapter(Context _context) 
		{
			context = _context;
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public  LoginDataBaseAdapter open() throws SQLException 
		{
			db = dbHelper.getWritableDatabase();
			return this;
		}
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{
			return db;
		}

		public void insertEntry(String userName,String password,String email,String phone,String address,byte[] imgdb)
				throws SQLiteException {
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put("USERNAME", userName);
			newValues.put("PASSWORD",password);
			newValues.put("EMAIL",email);
			newValues.put("PHONE",phone);
			newValues.put("ADDRESS",address);
			newValues.put("image", imgdb);
			// Insert the row into your table
			db.insert("LOGIN", null, newValues);
			///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
		}
		public int deleteEntry(String UserName)
		{
			//String id=String.valueOf(ID);
		    String where="USERNAME=?";
		    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
	       // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
	        return numberOFEntriesDeleted;
		}	
		public String getSinlgeEntry(String userName)
		{
			Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	        	return "NOT EXIST";
	        }
		    cursor.moveToFirst();
			String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
			cursor.close();
			return password;				
		}

	/*public String getFriends(String username)

	{
		Cursor c=db.rawQuery("SELECT * FROM student WHERE USERNAME='"+username+"'", null);
		c.moveToFirst();
		String rowvalue=c.getString(1)+c.getString(2);
		return rowvalue;
		/*Cursor cursor = db.query("LOGIN", null, "USERNAME=?",
				new String[]{username}, null, null, null);

		return cursor;

	}*/

	public Cursor getPerson(String username) {
		//SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery( "SELECT * FROM " + "LOGIN" + " WHERE " +
				"USERNAME" + "=?", new String[] { username } );
		return res;
	}

		public void  updateEntry(String i,String userName,String password,String em,String phn,String haddr,byte[] imgdb)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("USERNAME", userName);
			updatedValues.put("PASSWORD", password);
			updatedValues.put("EMAIL", em);
			updatedValues.put("PHONE", phn);
			updatedValues.put("ADDRESS", haddr);
			updatedValues.put("IMAGE", imgdb);
	        String where="ID = ?";
		    db.update("LOGIN", updatedValues, where, new String[]{i});
		}		
}