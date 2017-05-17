package com.kyrmyzyanik.carouselltest.data.source.local;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the topics locally.
 */
public final class TopicsPersistenceContract {

    private TopicsPersistenceContract() {}

    public static abstract class TopicEntry implements BaseColumns {
        public static final String TABLE_NAME = "topic";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_UPVOTE = "upvote";
        public static final String COLUMN_NAME_DOWNVOTE = "downvote";
    }
}
