package com.fitness.curli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WorkoutHistoryDB {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

     Cursor c = null;
    //private constructor
    public WorkoutHistoryDB(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
        this.db = openHelper.getWritableDatabase();

    }

    public void saveWorkout(HashMap json) {
        int id = 1;
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(calendar);
        System.out.println(date);
        String title = (String) json.get("title");
        Gson gson = new Gson();
        String jsonString = gson.toJson(json);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("date", date);
        values.put("title", title);
        values.put("json", jsonString);

        db.insert("WorkoutHistory", null, values);
        c = db.rawQuery("SELECT * FROM WorkoutHistory;", new String[]{});
        c.moveToFirst();
        System.out.println(c.getString(2));
        System.out.println("HERE123");
        db.close();
    }



}
