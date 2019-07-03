package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

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

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.ivProfileImage);
    }

    // create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvHandle;
        public TextView tvBody;
        public TextView tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvHandle = (TextView) itemView.findViewById(R.id.tvUserHandle);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
