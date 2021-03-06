package com.kyrmyzyanik.carouselltest.data.source;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.support.annotation.NonNull;
import com.kyrmyzyanik.carouselltest.data.Topic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load topics from the data sources into a cache.
 *
 */
public class TopicsRepository implements TopicsDataSource {

    private static TopicsRepository INSTANCE = null;

    private final TopicsDataSource mTopicsLocalDataSource;

    Map<String, Topic> mCachedTopics;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    private TopicsRepository(@NonNull TopicsDataSource topicsLocalDataSource) {
        mTopicsLocalDataSource = checkNotNull(topicsLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param topicsLocalDataSource  the device storage data source
     * @return the {@link TopicsRepository} instance
     */
    public static TopicsRepository getInstance(TopicsDataSource topicsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TopicsRepository(topicsLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Gets topics from cache, local data source (SQLite).
     */
    @Override
    public void getTopics(@NonNull final LoadTopicsCallback callback) {
        checkNotNull(callback);

        if (mCachedTopics != null && !mCacheIsDirty) {
            callback.onTopicsLoaded(new ArrayList<>(mCachedTopics.values()));
            return;
        }

        mTopicsLocalDataSource.getTopics(new LoadTopicsCallback() {
            @Override
            public void onTopicsLoaded(List<Topic> topics) {
                refreshCache(topics);
                callback.onTopicsLoaded(new ArrayList<>(mCachedTopics.values()));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void saveTopic(@NonNull Topic topic) {
        checkNotNull(topic);
        mTopicsLocalDataSource.saveTopic(topic);

        if (mCachedTopics == null) {
            mCachedTopics = new LinkedHashMap<>();
        }
        mCachedTopics.put(topic.getId(), topic);
    }

    @Override
    public void deleteAllTopics() {
        mTopicsLocalDataSource.deleteAllTopics();

        if (mCachedTopics == null) {
            mCachedTopics = new LinkedHashMap<>();
        }
        mCachedTopics.clear();
    }

    @Override
    public void refreshTopics() {
        mCacheIsDirty = true;
    }

    @Override
    public void upVoteTppic(@NonNull Topic topic) {
        saveTopic(topic);
    }

    @Override
    public void downVoteTppic(@NonNull Topic topic) {
        saveTopic(topic);
    }

    private void refreshCache(List<Topic> topics) {
        if (mCachedTopics == null) {
            mCachedTopics = new LinkedHashMap<>();
        }
        mCachedTopics.clear();

        for (Topic topic : topics) {
            mCachedTopics.put(topic.getId(), topic);
        }
    }
}
