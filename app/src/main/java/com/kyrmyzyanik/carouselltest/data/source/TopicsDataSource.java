package com.kyrmyzyanik.carouselltest.data.source;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.support.annotation.NonNull;

import com.kyrmyzyanik.carouselltest.data.Topic;

import java.util.List;

/**
 * Main entry point for accessing topics data.
 * <p>
 * For simplicity, only getTopics() and getTopic() have callbacks. Consider adding
 * callbacks to other methods to inform the user of database errors or successful operations.
 */

public interface TopicsDataSource {

    interface LoadTopicsCallback {

        void onTopicsLoaded(List<Topic> topics);

        void onDataNotAvailable();
    }

    void getTopics(@NonNull LoadTopicsCallback callback);

    void saveTopic(@NonNull Topic topic);

    void deleteAllTopics();
}
