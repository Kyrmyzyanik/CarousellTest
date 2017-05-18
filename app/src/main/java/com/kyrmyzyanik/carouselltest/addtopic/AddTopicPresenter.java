package com.kyrmyzyanik.carouselltest.addtopic;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.support.annotation.NonNull;

import com.kyrmyzyanik.carouselltest.data.Topic;
import com.kyrmyzyanik.carouselltest.data.source.TopicsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link AddTopicFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddTopicPresenter implements AddTopicContract.Presenter{

    @NonNull
    private final TopicsDataSource mTopicsRepository;

    @NonNull
    private final AddTopicContract.View mAddTopicView;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add view.
     *
     * @param topicsDataSource a repository of data for topics
     * @param addTaskView the add view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddTopicPresenter(@NonNull TopicsDataSource topicsDataSource,
                             @NonNull AddTopicContract.View addTaskView, boolean shouldLoadDataFromRepo) {
        mTopicsRepository = checkNotNull(topicsDataSource);
        mAddTopicView = checkNotNull(addTaskView);
        mIsDataMissing = shouldLoadDataFromRepo;

        mAddTopicView.setPresenter(this);
    }

    @Override
    public void saveTask(String title, int upVote, int downVote) {
        createTask(title, upVote, downVote);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private void createTask(String title, int upVote, int downVote) {
        Topic newTopic = new Topic(title, upVote, downVote);
        mTopicsRepository.saveTopic(newTopic);
        mAddTopicView.showTopicsList();
    }

    @Override
    public void start() {

    }
}
