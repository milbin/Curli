package com.fitness.curli;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {

    //constructor
    public DatabaseOpenHelper(Context context, int databaseVersion, String databaseName){
        super(context, databaseName, null, databaseVersion);
    }

}

