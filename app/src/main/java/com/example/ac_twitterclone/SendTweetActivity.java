package com.example.ac_twitterclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtSendTweets;
    private Button btnViewTweets;
    private ListView listTweetsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweets = findViewById(R.id.edtSendTweet);
        btnViewTweets = findViewById(R.id.btnViewTweets);
        listTweetsView = findViewById(R.id.listTweetsView);

        btnViewTweets.setOnClickListener(this);
        // btnSendTweet = findViewById(R.id.btnSendTweet);


    }

    public void sendTweet(View v) {
        ParseObject parseObject = new ParseObject("MyTweet");
        if (edtSendTweets.getText().toString().equals("")) {
            return;

        }
        parseObject.put("tweet", edtSendTweets.getText().toString());
        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {

                    showToast("Posted ", FancyToast.INFO);


                } else {

                    showToast(e.getMessage(), FancyToast.ERROR);
                }

            }
        });


    }

    private void showToast(String message, int type) {

        FancyToast
                .makeText(SendTweetActivity.this
                        , message
                        , FancyToast.LENGTH_SHORT, type
                        , false)
                .show();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnViewTweets) {
            try {

                final ArrayList<HashMap<String, String>> listTweet = new ArrayList<>();
                final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this, listTweet, android.R.layout.simple_list_item_2, new String[]{"username", "tweet"}, new int[]{android.R.id.text1, android.R.id.text2});

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("fanOf"));
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0 && e == null) {

                            for (ParseObject object : objects) {

                                HashMap<String, String> tweetItem = new HashMap<>();
                                ;
                                tweetItem.put("username", object.getString("username"));
                                tweetItem.put("tweet", object.getString("tweet"));

                                listTweet.add(tweetItem);


                            }
                            listTweetsView.setAdapter(adapter);

                        } else {
                            showToast(e.getMessage(), FancyToast.ERROR);

                        }
                    }
                });


            } catch (Exception e) {

                e.printStackTrace();

            }

        }
    }
}
