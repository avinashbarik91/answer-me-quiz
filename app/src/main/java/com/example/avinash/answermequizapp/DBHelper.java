package com.example.avinash.answermequizapp;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Avinash Barik on 31/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Player";
    public static final String COL0 = "name";
    public static final String COL1 = "top_score";
    public static final String COL2 = "last_score";

    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " +TABLE_NAME+ " ("+COL0+" TEXT PRIMARY KEY, " +
                COL1+" INTEGER, "+ COL2+ " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    public boolean addData(Player aPlayer)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL0, aPlayer.getName());
        contentValues.put(COL1, aPlayer.getTopScore());
        contentValues.put(COL2, aPlayer.getLastScore());

        Log.d("DatabaseHelper", "addData: Adding Items to "+ TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }

    public boolean updateData(Player aPlayer)    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1, aPlayer.getTopScore());
        contentValues.put(COL2, aPlayer.getLastScore());

        Log.d("DatabaseHelper", "UpdateData: Updating Items to "+ TABLE_NAME);

        long result = db.update(TABLE_NAME, contentValues, COL0+"= '"+aPlayer.getName()+"'", null);
        if(result == -1)
        {
            return false;
        }else
        {
            return true;
        }
    }

    public Cursor getPlayerData(String playerName)
    {
        String[] params = new String[]{playerName};
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+COL1+","+COL2+" FROM "+TABLE_NAME+" WHERE "+COL0+" = ?";
        Cursor data = db.rawQuery(query, params);
        return data;
    }

    public Cursor getAllPlayers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
