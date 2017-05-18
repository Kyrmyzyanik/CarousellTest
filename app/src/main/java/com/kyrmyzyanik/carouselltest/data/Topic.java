package com.kyrmyzyanik.carouselltest.data;

/**
 * Created by Kyrmyzyanik on 5/17/2017.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Immutable model class for a Topic.
 */
public class Topic {

    @NonNull
    private String mId = new String();

    @Nullable
    private String mTitle = new String();

    @NonNull
    private int mUpVote = 0;
    @NonNull
    private int mDownVote = 0;


    /**
     * Use this constructor to create a new active Topic.
     *
     * @param title       title of the topic
     * @param upVote      number of up votes of the topic
     * @param downVote    number of down votes of the topic
     */
    public Topic(@Nullable String title, int upVote, int downVote) {
        this(title, upVote, downVote, UUID.randomUUID().toString());
    }

    /**
     * Use this constructor to create a new Topic if the Topic already has an id (copy of another
     * Topic).
     *
     * @param title       title of the topic
     * @param upVote      number of up votes of the topic
     * @param downVote    number of down votes of the topic
     * @param id          id of the topic
     */
    public Topic(@Nullable String title, int upVote, int downVote, @NonNull String id) {
        mId = id;
        mTitle = title;
        mUpVote = upVote;
        mDownVote = downVote;
    }

    public void setTopic(Topic t) {
        mId = t.mId;
        mTitle = t.mTitle;
        mUpVote = t.mUpVote;
        mDownVote = t.mDownVote;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public int getUpVote() {
        return mUpVote;
    }

    @Nullable
    public int getDownVote() {
        return mDownVote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equal(mId, topic.mId) &&
                Objects.equal(mTitle, topic.mTitle) &&
                Objects.equal(mUpVote, topic.mUpVote) &&
                Objects.equal(mDownVote, topic.mDownVote);
    }

    public boolean isEmpty() {
        if(mTitle.length() ==0 || mTitle == null) {
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mUpVote, mUpVote);
    }

    @Override
    public String toString() {
        return "Topic with title " + mTitle;
    }


    public void setId(@NonNull String mId) {
        this.mId = mId;
    }

    public void setTitle(@Nullable String mTitle) {
        this.mTitle = mTitle;
    }

    public void setUpVote(@NonNull int mUpVote) {
        this.mUpVote = mUpVote;
    }

    public void setDownVote(@NonNull int mDownVote) {
        this.mDownVote = mDownVote;
    }

}

