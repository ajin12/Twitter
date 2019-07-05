package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetDetailsActivity extends AppCompatActivity {

    // the tweet to display
    Tweet tweet;
    TwitterClient client;
    public static final int REPLY_TWEET_REQUEST_CODE = 1000;

    // the view objects
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvUserHandle) TextView tvUserHandle;
    @BindView(R.id.tvTimestamp) TextView tvTimestamp;
    @BindView(R.id.ibReply) ImageButton ibReply;
    @BindView(R.id.ibRetweet) ImageButton ibRetweet;
    @BindView(R.id.ibHeart) ImageButton ibHeart;

    // context for rendering
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        // resolve the view objects
        ButterKnife.bind(this);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        client = TwitterApp.getRestClient(this);

        // populate the views
        tvUserName.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvUserHandle.setText("@" + tweet.user.screenName);
        tvTimestamp.setText(tweet.createdAt);

        // set profile picture
        context = ivProfileImage.getContext();
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);

        ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyTweet(v);
            }
        });

        // set heart button depending on whether tweet is already liked
        if (tweet.favorited) {
            ibHeart.setImageResource(R.drawable.ic_vector_heart);
        } else {
            ibHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

        ibHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.favorited) {
                    client.likeTweet(tweet.uid, new JsonHttpResponseHandler());
                    ibHeart.setImageResource(R.drawable.ic_vector_heart);
                } else {
                    client.unlikeTweet(tweet.uid, new JsonHttpResponseHandler());
                    ibHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
                }
            }
        });

        // set retweet button depending on whether already retweeted
        if (tweet.retweeted) {
            ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
            ibRetweet.setTag(R.drawable.ic_vector_retweet);
        } else {
            ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
            ibRetweet.setTag(R.drawable.ic_vector_retweet_stroke);
        }
    }

    private void replyTweet(View v) {
        // open ComposeActivity to reply to a tweet
        Intent reply = new Intent(v.getContext(), ComposeActivity.class);
        reply.putExtra("user_handle", tweet.user.screenName);
        reply.putExtra("tweet_id", tweet.uid);
        ((Activity) context).startActivityForResult(reply, REPLY_TWEET_REQUEST_CODE);
    }
}
