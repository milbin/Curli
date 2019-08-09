package com.fitness.curli;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;


public class Handler{

    myDbHelper myHelper;

    //information of databases
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExerciseDb.db";
    public static final String TABLE_NAME = "";
    public static final String COLUMN_ID = "ExerciseID";
    public static final String COLUMN_NAME = "ExerciseName";

    //initialize the databases
    public Handler(Context context) {

        myHelper = new myDbHelper(context);
    }

    public long insertData(String name, String group, String muscles) { //fix array of muscles
        SQLiteDatabase dbb = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.GROUP, group);
        //contentValues.put(myDbHelper.MUSCLES, muscles);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public String getData()
    {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.GROUP, myDbHelper.MUSCLES};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String group = cursor.getString(cursor.getColumnIndex(myDbHelper.GROUP));
            String muscles = cursor.getString(cursor.getColumnIndex(myDbHelper.MUSCLES));
            buffer.append(cid+ "   " + name + "   " + group + "   " + muscles + " \n");
        }
        return buffer.toString();
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String[] whereArgs ={uname};

        int count = db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String NAME = "Name";    //Column II
        private static final String GROUP = "Group";    // Column III
        private static final String MUSCLES = "Muscles";    // Column IV
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME + " VARCHAR(255) , " + GROUP + MUSCLES;
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;

        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    public void loadHandler() {}
    public void addHandler(ExerciseDb exercise) {}
    public ExerciseDb findHandler(String ExerciseName) {}
    public boolean deleteHandler(int ID) {}
    public boolean updateHandler(int ID, String name) {}
}
    */


}