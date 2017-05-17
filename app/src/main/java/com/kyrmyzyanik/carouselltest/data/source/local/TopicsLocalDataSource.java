package com.kyrmyzyanik.carouselltest.data.source.local;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.kyrmyzyanik.carouselltest.data.Topic;
import com.kyrmyzyanik.carouselltest.data.source.TopicsDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class TopicsLocalDataSource implements TopicsDataSource {

    private static TopicsLocalDataSource INSTANCE;

    private TopicsDbHelper mDbHelper;

    private TopicsLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new TopicsDbHelper(context);
    }

    public static TopicsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TopicsLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadTopicsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTopics(@NonNull LoadTopicsCallback callback) {
        List<Topic> topics = new ArrayList<Topic>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TopicsPersistenceContract.TopicEntry.COLUMN_NAME_ENTRY_ID,
                TopicsPersistenceContract.TopicEntry.COLUMN_NAME_TITLE,
                TopicsPersistenceContract.TopicEntry.COLUMN_NAME_UPVOTE,
                TopicsPersistenceContract.TopicEntry.COLUMN_NAME_DOWNVOTE
        };

        Cursor c = db.query(
                TopicsPersistenceContract.TopicEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow
                        (TopicsPersistenceContract.TopicEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow
                        (TopicsPersistenceContract.TopicEntry.COLUMN_NAME_TITLE));
                int upVote = c.getInt(c.getColumnIndexOrThrow
                        (TopicsPersistenceContract.TopicEntry.COLUMN_NAME_UPVOTE));
                int downVote = c.getInt(c.getColumnIndexOrThrow
                        (TopicsPersistenceContract.TopicEntry.COLUMN_NAME_DOWNVOTE));
                Topic topic = new Topic(title, upVote, downVote, itemId);
                topics.add(topic);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if (topics.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onTopicsLoaded(topics);
        }
    }

    @Override
    public void saveTopic(@NonNull Topic topic) {
        checkNotNull(topic);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TopicsPersistenceContract.TopicEntry.COLUMN_NAME_ENTRY_ID, topic.getId());
        values.put(TopicsPersistenceContract.TopicEntry.COLUMN_NAME_TITLE, topic.getTitle());
        values.put(TopicsPersistenceContract.TopicEntry.COLUMN_NAME_UPVOTE, topic.getUpVote());
        values.put(TopicsPersistenceContract.TopicEntry.COLUMN_NAME_DOWNVOTE, topic.getDownVote());

        db.insert(TopicsPersistenceContract.TopicEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void deleteAllTopics() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TopicsPersistenceContract.TopicEntry.TABLE_NAME, null, null);

        db.close();
    }
}
