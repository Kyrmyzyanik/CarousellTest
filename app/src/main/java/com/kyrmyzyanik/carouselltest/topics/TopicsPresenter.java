package com.kyrmyzyanik.carouselltest.topics;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Strings;
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
                List<Topic> topicsSorted = showTopTopics(topics);
                for (Topic topic : topicsSorted) {
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

    //Selection sort ascending order by upvotes and downvotes
    public List<Topic> doSortShowTopTopics(List<Topic> topics) {
        for (int i = 0; i < topics.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < topics.size(); j++) {
                if ((topics.get(j).getUpVote() - topics.get(j).getDownVote()) >
                        (topics.get(index).getUpVote() - topics.get(index).getDownVote())) {
                    index = j;
                }
            }
            Topic smallerNumber = new Topic(topics.get(index).getTitle(),
                    topics.get(index).getUpVote(), topics.get(index).getDownVote(), topics.get(index).getId());

            topics.get(index).setTopic(topics.get(i));
            topics.get(i).setTopic(smallerNumber);
        }
        return topics;
    }
    //Show top 20 topics first
    public List<Topic> showTopTopics(List<Topic> topics) {
        List<Topic> topicsResult = new ArrayList<Topic>();
        List<Topic> topicsSorted = doSortShowTopTopics(topics);
        ArrayList<String> ids = new ArrayList<String>();

        //Add to topicsResult top 20 topics
        if(topicsSorted.size() > 20) {
            for(int i = 0; i < 20; i++) {
                topicsResult.add(topicsSorted.get(i));
                ids.add(topicsSorted.get(i).getId());
            }
        } else {
            for(int i = 0; i < topicsSorted.size(); i++) {
                topicsResult.add(topicsSorted.get(i));
                ids.add(topicsSorted.get(i).getId());
            }
        }

        //Add to topicsResult another topics ecxept 20 top topics
        for(int i = 0; i < topics.size(); i++) {
            boolean isEqual = false;
            for(int j = 0; j < ids.size(); j++) {
                if(topics.get(i).getId().equals(ids.get(j))) {
                    isEqual = true;
                }
            }

            if(!isEqual){
                topicsResult.add(topics.get(i));
            }
        }
        return topicsResult;
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
        loadTasks(false, false);
    }

    @Override
    public void downVote(Topic downVote) {
        mTopicsRepository.downVoteTppic(downVote);
        loadTasks(false, false);
    }
}
