package com.example.android.dropr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    private SimpleScannerActivity scannerActivity = new SimpleScannerActivity();
    ;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    protected DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context
     * @return the instance of DatabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a list of quotes
     */
    public List<String> getCodes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name, upc14 FROM Barcodes", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getInfo(String rawContent) {
        String TAG = "Getinfo():";
        String content = "00" + rawContent;
        String brandName = "";
        Cursor cursor = database.rawQuery("SELECT name, upc12 from Barcodes WHERE '" + content + "' = upc12", null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            brandName = cursor.getString(cursor.getColumnIndex("name"));

            cursor.close();
        } else {
            Log.v(TAG, "uh oh, something went wrong in the if loop! ");
        }

        return brandName;
    }
}
