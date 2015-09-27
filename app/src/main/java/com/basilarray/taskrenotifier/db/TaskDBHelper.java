package com.basilarray.taskrenotifier.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
//        String sqlQuery =
//                String.format("CREATE TABLE %s (" +
//                                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                                "%s TEXT)",
//                        TaskContract.Tasks.TABLENAME,
//                        TaskContract.Tasks._ID,
//                        TaskContract.Tasks.TITLE);
        String sqlQuery = "CREATE TABLE " + TaskContract.Tasks.TABLENAME + " (" +
                TaskContract.Tasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.Tasks.TITLE + " TEXT, " +
                TaskContract.Tasks.DESCRIPTION + " TEXT, " +
                TaskContract.Tasks.FREQTYPE + " INTEGER, " +
                TaskContract.Tasks.FREQNUM + " INTEGER, " +
                TaskContract.Tasks.FREQUNITS + " INTEGER, " +
                TaskContract.Tasks.DT_START + " INTEGER, " +
                TaskContract.Tasks.ENABLED + " INTEGER, " +
                TaskContract.Tasks.ALLPUSHOUT + " INTEGER)";

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);

        sqlQuery = "CREATE TABLE " + TaskContract.Instances.TABLENAME + " (" +
                TaskContract.Instances.PARENT_ID + " INTEGER, " +
                TaskContract.Instances._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.Instances.DT_DUE + " INTEGER, " +
                TaskContract.Instances.DT_DONE + " INTEGER)";

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.Tasks.TABLENAME);
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.Instances.TABLENAME);
        onCreate(sqlDB);
    }
}

