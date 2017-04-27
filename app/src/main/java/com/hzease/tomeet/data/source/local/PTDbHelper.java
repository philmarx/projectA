package com.hzease.tomeet.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

/**
 * Created by Key on 2017/2/20 12:18
 * email: MrKey.K@gmail.com
 * description:
 */

public class PTDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Project_T.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PTPersistenceContract.PTEntry.TABLE_NAME + " (" +
                    PTPersistenceContract.PTEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PTPersistenceContract.PTEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PTPersistenceContract.PTEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PTPersistenceContract.PTEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    PTPersistenceContract.PTEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE +
                    " )";

    public PTDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.d(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
