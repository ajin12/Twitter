package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    public static final int REPLY_TWEET_REQUEST_CODE = 1000;

    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate the layout and cache references into ViewHolder
    // only invoked when need to create new row
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate tweet row
        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the data according to position
        Tweet tweet = mTweets.get(i);

        // populate the views according to this data
        viewHolder.tvUsername.setText(tweet.user.name);
        viewHolder.tvHandle.setText("@" + tweet.user.screenName);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvTimestamp.setText(tweet.createdAt);

        // load profile picture and crop to a circle
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.ivProfileImage);
    }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        // automatically finds each field by the specified ID
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUsername;
        @BindView(R.id.tvUserHandle) TextView tvHandle;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTimestamp) TextView tvTimestamp;
        @BindView(R.id.ibHeart) ImageButton ibHeart;
        @BindView(R.id.ibReply) ImageButton ibReply;
        @BindView(R.id.ibRetweet) ImageButton ibRetweet;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replyTweet(v);
                }
            });
        }

        private void replyTweet(View v) {
            // get tweet user is responding to
            Tweet tweet = mTweets.get(getAdapterPosition());
            // open ComposeActivity to reply to a tweet
            Intent reply = new Intent(v.getContext(), ComposeActivity.class);
            reply.putExtra("user_handle", tweet.user.screenName);
            reply.putExtra("tweet_id", tweet.uid);
            ((Activity) context).startActivityForResult(reply, REPLY_TWEET_REQUEST_CODE);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
