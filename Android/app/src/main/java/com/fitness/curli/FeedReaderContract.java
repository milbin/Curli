package com.fitness.curli;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    private static final String SQL_CREATE_ENTRIES;

    static {
        SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                FeedEntry._ID + " INTEGER PRIMARY KEY," +
                FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";
    }

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
