package com.fitness.curli;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;

import java.sql.*;

public class workoutHistoryDB {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    Cursor c = null;
    //private constructor
    public workoutHistoryDB(Context context){
        this.openHelper = new DatabaseOpenHelper(context, c, 1);
        this.db = openHelper.getWritableDatabase();

    }

    public void saveGroup(JSONObject json) {

       /* c=db.rawQuery("Select MainGroup From ExerciseTable Where Name = '"+name+"'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            GroupStr = c.getString(0);
            buffer.append(""+GroupStr);
        }
        return buffer.toString();*/
    }



}
