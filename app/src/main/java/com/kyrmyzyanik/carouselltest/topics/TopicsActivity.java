package com.kyrmyzyanik.carouselltest.topics;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.kyrmyzyanik.carouselltest.R;
import com.kyrmyzyanik.carouselltest.data.source.TopicsRepository;
import com.kyrmyzyanik.carouselltest.data.source.local.TopicsLocalDataSource;
import com.kyrmyzyanik.carouselltest.utils.ActivityUtils;

public class TopicsActivity extends AppCompatActivity {

    TopicsPresenter mTopicsPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TopicsFragment topicsFragment =
                (TopicsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (topicsFragment == null) {
            // Create the fragment
            topicsFragment = TopicsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), topicsFragment, R.id.contentFrame);
        }

        // Create the presenter
        mTopicsPresenter = new TopicsPresenter(TopicsRepository.getInstance(
                TopicsLocalDataSource.getInstance(getApplicationContext())), topicsFragment);
    }

}
