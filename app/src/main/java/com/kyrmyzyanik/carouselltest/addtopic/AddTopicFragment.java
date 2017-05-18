package com.kyrmyzyanik.carouselltest.addtopic;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.kyrmyzyanik.carouselltest.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main UI for the add topic screen. Users can enter a topic title.
 */
public class AddTopicFragment extends Fragment implements AddTopicContract.View {

    private AddTopicContract.Presenter mPresenter;

    private TextView mTitle;

    public static AddTopicFragment newInstance() {
        return new AddTopicFragment();
    }

    public AddTopicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(@NonNull AddTopicContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_topic_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveTask(mTitle.getText().toString(), 0, 0);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addtopic_frag, container, false);
        mTitle = (TextView) root.findViewById(R.id.add_topic_title);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void showTopicsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }
}
