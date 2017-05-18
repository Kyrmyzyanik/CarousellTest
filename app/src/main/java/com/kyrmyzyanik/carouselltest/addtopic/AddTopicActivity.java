package com.kyrmyzyanik.carouselltest.addtopic;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kyrmyzyanik.carouselltest.R;
import com.kyrmyzyanik.carouselltest.data.source.TopicsRepository;
import com.kyrmyzyanik.carouselltest.data.source.local.TopicsLocalDataSource;
import com.kyrmyzyanik.carouselltest.utils.ActivityUtils;

/**
 * Displays an add topic screen.
 */
public class AddTopicActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddTopicPresenter mAddTopicPresenter;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtopic_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(R.string.add_topic);

        AddTopicFragment addTopicFragment = (AddTopicFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addTopicFragment == null) {
            addTopicFragment = AddTopicFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addTopicFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddTopicPresenter = new AddTopicPresenter(
                TopicsRepository.getInstance(TopicsLocalDataSource.getInstance(getApplicationContext())),
                addTopicFragment,
                shouldLoadDataFromRepo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddTopicPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
