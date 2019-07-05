package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel // annotation indicates class is Parcelable
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;
    public boolean favorited;
    public boolean retweeted;
    public String mediaUrl;
    public int favoriteCount;
    public int retweetCount;
    public int replyCount;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        if (jsonObject.has("favorite_count")) {
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
        }
        if (jsonObject.has("retweet_count")) {
            tweet.retweetCount = jsonObject.getInt("retweet_count");
        }
        if (jsonObject.has("reply_count")) {
            tweet.replyCount = jsonObject.getInt("reply_count");
        }

        JSONObject entities = jsonObject.getJSONObject("entities");
        // check if there is media
        if (entities.has("media")) {
            // extract media url from the first image entity
            tweet.mediaUrl = entities.getJSONArray("media").getJSONObject(0)
                    .getString("media_url_https");
        }

        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }
}
