package com.kyrmyzyanik.carouselltest.topics;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kyrmyzyanik.carouselltest.R;
import com.kyrmyzyanik.carouselltest.addtopic.AddTopicActivity;
import com.kyrmyzyanik.carouselltest.data.Topic;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Topic}s.
 */
public class TopicsFragment extends Fragment implements TopicsContract.View {

    private TopicsContract.Presenter mPresenter;

    private TopicsAdapter mListAdapter;

    private View mNoTopicsView;

    private TextView mNoTopicMainView;
    private TextView mNoTopicAddView;
    private LinearLayout mTopicsView;


    public TopicsFragment() {
        // Requires empty public constructor
    }

    public static TopicsFragment newInstance() {
        return new TopicsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TopicsAdapter(new ArrayList<Topic>(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull TopicsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.topics_frag, container, false);

        // Set up topics view
        ListView listView = (ListView) root.findViewById(R.id.topics_list);
        listView.setAdapter(mListAdapter);
        mTopicsView = (LinearLayout) root.findViewById(R.id.topicsLL);

        // Set up  no topics view
        mNoTopicsView = root.findViewById(R.id.noTopics);
        mNoTopicMainView = (TextView) root.findViewById(R.id.noTopicsMain);
        mNoTopicAddView = (TextView) root.findViewById(R.id.noTopicsAdd);
        mNoTopicAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTopic();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_topic);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTopic();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTopics(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showTopics(List<Topic> topics) {
        mListAdapter.replaceData(topics);
        mTopicsView.setVisibility(View.VISIBLE);
        mNoTopicsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoTopics() {
        showNoTopicsViews(
                getResources().getString(R.string.no_topics_all),
                false
        );
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_topic_message));
    }

    private void showNoTopicsViews(String mainText, boolean showAddView) {
        mTopicsView.setVisibility(View.GONE);
        mNoTopicsView.setVisibility(View.VISIBLE);

        mNoTopicMainView.setText(mainText);
        mNoTopicAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAddTopic() {
        Intent intent = new Intent(getContext(), AddTopicActivity.class);
        startActivityForResult(intent, AddTopicActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showLoadingTopicsError() {
        showMessage(getString(R.string.loading_topics_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class TopicsAdapter extends BaseAdapter {

        private List<Topic> mTopics;

        public TopicsAdapter(List<Topic> topics) {
            setList(topics);
        }

        public void replaceData(List<Topic> topics) {
            setList(topics);
            notifyDataSetChanged();
        }

        private void setList(List<Topic> topics) {
            mTopics = checkNotNull(topics);
        }

        @Override
        public int getCount() {
            return mTopics.size();
        }

        @Override
        public Topic getItem(int i) {
            return mTopics.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.topic_item, viewGroup, false);
            }

            final Topic topic = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.title);
            titleTV.setText(topic.getTitle());

            return rowView;
        }
    }
}
