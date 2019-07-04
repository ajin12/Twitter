package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    public static final String RESULT_TWEET_KEY = "result_tweet";
    TwitterClient client;
    String userHandle;
    long tweetID;
    // automatically finds each field by the specified ID.
    @BindView(R.id.etTweetInput) EditText etTweetInput;
    @BindView(R.id.bTweet) Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);

        userHandle = getIntent().getStringExtra("user_handle");
        tweetID = getIntent().getLongExtra("tweet_id", 0);

        // check if intent was created by clicking on reply or compose
        if (userHandle != null) {
            etTweetInput.setText("@" + userHandle + " ");
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if intent was created by clicking on reply or compose
                if (userHandle == null && tweetID == 0) {
                    sendTweet();
                } else {
                    replyTweet();
                }
            }
        });

        client = TwitterApp.getRestClient(this);
    }

//    TODO -- refactor code for sendTweet and replyTweet; a lot of copy and paste
    private void sendTweet() {
        client.sendTweet(etTweetInput.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        // parsing response
                        JSONObject responseJson = new JSONObject(new String(responseBody));
                        Tweet resultTweet = Tweet.fromJSON(responseJson);

                        // return result to calling activity
                        Intent resultData = new Intent();
                        // serialize the tweet using parceler
                        resultData.putExtra(RESULT_TWEET_KEY, Parcels.wrap(resultTweet));
                        setResult(RESULT_OK, resultData);
                        finish();
                    } catch (JSONException e) {
                        Log.e("ComposeActivity", "Error parsing response", e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void replyTweet() {
        client.replyTweet(etTweetInput.getText().toString(), tweetID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        // parsing response
                        JSONObject responseJson = new JSONObject(new String(responseBody));
                        Tweet resultTweet = Tweet.fromJSON(responseJson);

                        // return result to calling activity
                        Intent resultData = new Intent();
                        // serialize the tweet using parceler
                        resultData.putExtra(RESULT_TWEET_KEY, Parcels.wrap(resultTweet));
                        setResult(RESULT_OK, resultData);
                        finish();
                    } catch (JSONException e) {
                        Log.e("ComposeActivity", "Error parsing response", e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
