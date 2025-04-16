package br.com.stilldistribuidora.partners.Base.navegador;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.resources.Resources;

public class NavegationsReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NavegationReader.db";
    public NavegationsReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NavegationConf.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(NavegationConf.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void initInfo() {
        List<NavegationModel> confToken = getByProps(Resources._SYSTEM_LAST_POINT_);
        if (confToken.size() == 0) {
            insert(Resources._SYSTEM_LAST_POINT_, "");
        }
    }
    public long insert(String action, String struct) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Navegation.NavegationEntry.COLUMN_NAME_PROPS, action);
        values.put(Navegation.NavegationEntry.COLUMN_NAME_CONTENT, struct);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Navegation.NavegationEntry.TABLE_NAME, null, values);
        return newRowId;
    }
    public List<NavegationModel> getByProps(String props) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                Navegation.NavegationEntry.COLUMN_NAME_ID,
                Navegation.NavegationEntry.COLUMN_NAME_PROPS,
                Navegation.NavegationEntry.COLUMN_NAME_CONTENT
        };
        String selection = Navegation.NavegationEntry.COLUMN_NAME_PROPS + "=?";
        String[] selectionArgs = {props};
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Navegation.NavegationEntry.COLUMN_NAME_ID + " DESC";
        Cursor cursor = db.query(
                Navegation.NavegationEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List<NavegationModel> itens = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(Navegation.NavegationEntry.COLUMN_NAME_ID));
            String props_ = cursor.getString(cursor.getColumnIndexOrThrow(Navegation.NavegationEntry.COLUMN_NAME_PROPS));
            String content_ = cursor.getString(cursor.getColumnIndexOrThrow(Navegation.NavegationEntry.COLUMN_NAME_CONTENT));
            itens.add(new NavegationModel(itemId, props_.trim(), content_.trim()));
        }
        cursor.close();
        return itens;
    }
    public int deleted(String props) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = Navegation.NavegationEntry.COLUMN_NAME_PROPS + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {props};
        // Issue SQL statement.
        int deletedRows = db.delete(Navegation.NavegationEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }

    public long update(@NonNull NavegationModel NavegationModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Navegation.NavegationEntry.COLUMN_NAME_CONTENT, NavegationModel.getContent());
        String selection = Navegation.NavegationEntry.COLUMN_NAME_PROPS + " = ?";
        String[] selectionArgs = {NavegationModel.getProps()};

        int count = db.update(
                Navegation.NavegationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }
}