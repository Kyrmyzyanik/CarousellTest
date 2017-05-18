package com.kyrmyzyanik.carouselltest.topics;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kyrmyzyanik.carouselltest.addtopic.AddTopicActivity;
import com.kyrmyzyanik.carouselltest.data.Topic;
import com.kyrmyzyanik.carouselltest.data.source.TopicsDataSource;
import com.kyrmyzyanik.carouselltest.data.source.TopicsRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link TopicsFragment}), retrieves the data and updates the
 * UI as required.
 */
public class TopicsPresenter implements TopicsContract.Presenter {

    private final TopicsRepository mTopicsRepository;

    private final TopicsContract.View mTopicsView;

    private boolean mFirstLoad = true;

    public TopicsPresenter(@NonNull TopicsRepository topicsRepository, @NonNull TopicsContract.View topicsView) {
        mTopicsRepository = checkNotNull(topicsRepository, "topicsRepository cannot be null");
        mTopicsView = checkNotNull(topicsView, "topicsView cannot be null!");

        mTopicsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTopics(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddTopicActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mTopicsView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTopics(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTopicsView.setLoadingIndicator(true);
        }

        if (forceUpdate) {
            mTopicsRepository.refreshTopics();
        }

        mTopicsRepository.getTopics(new TopicsDataSource.LoadTopicsCallback() {
            @Override
            public void onTopicsLoaded(List<Topic> topics) {
                List<Topic> topicsToShow = new ArrayList<Topic>();

                for (Topic topic : topics) {
                    topicsToShow.add(topic);
                }
                // The view may not be able to handle UI updates anymore
                if (!mTopicsView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTopicsView.setLoadingIndicator(false);
                }

                processTopics(topicsToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTopicsView.isActive()) {
                    return;
                }
                mTopicsView.showLoadingTopicsError();
            }
        });
    }

    private void processTopics(List<Topic> topics) {
        if (topics.isEmpty()) {
            processEmptyTopics();
        } else {
            // Show the list of topics
            mTopicsView.showTopics(topics);
        }
    }

    private void processEmptyTopics() {
        mTopicsView.showNoTopics();
    }

    @Override
    public void addNewTopic() {
        mTopicsView.showAddTopic();
    }

    @Override
    public void upVote(Topic upVote) {
        mTopicsRepository.upVoteTppic(upVote);
        Log.e(" --- "," --- upvote 1");
        loadTasks(false, false);
    }

    @Override
    public void downVote(Topic downVote) {
        mTopicsRepository.downVoteTppic(downVote);
        loadTasks(false, false);
    }
}
