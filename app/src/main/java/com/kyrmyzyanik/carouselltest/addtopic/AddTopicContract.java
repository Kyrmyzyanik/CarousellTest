package com.kyrmyzyanik.carouselltest.addtopic;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import com.kyrmyzyanik.carouselltest.BasePresenter;
import com.kyrmyzyanik.carouselltest.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddTopicContract {

    interface View extends BaseView<Presenter> {

        void showTopicsList();

        void setTitle(String title);
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, int upVote, int downVote);

        boolean isDataMissing();
    }
}
