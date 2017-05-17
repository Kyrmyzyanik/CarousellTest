package com.kyrmyzyanik.carouselltest.data.source.local;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TopicsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Topics.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TopicsPersistenceContract.TopicEntry.TABLE_NAME + " (" +
                    TopicsPersistenceContract.TopicEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    TopicsPersistenceContract.TopicEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    TopicsPersistenceContract.TopicEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TopicsPersistenceContract.TopicEntry.COLUMN_NAME_UPVOTE + INTEGER_TYPE + COMMA_SEP +
                    TopicsPersistenceContract.TopicEntry.COLUMN_NAME_DOWNVOTE + INTEGER_TYPE +
            " )";

    public TopicsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //no required as it is version 1
    }
}
