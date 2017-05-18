package com.kyrmyzyanik.carouselltest.topics;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import com.kyrmyzyanik.carouselltest.BasePresenter;
import com.kyrmyzyanik.carouselltest.BaseView;
import com.kyrmyzyanik.carouselltest.data.Topic;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface TopicsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTopics(List<Topic> topics);

        void showAddTopic();

        void showLoadingTopicsError();

        void showNoTopics();

        boolean isActive();

        void showSuccessfullySavedMessage();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTopics(boolean forceUpdate);

        void addNewTopic();
    }
}
