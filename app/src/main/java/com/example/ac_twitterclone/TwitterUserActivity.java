package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

   private ListView listView;
   private ArrayAdapter<String> arrayAdapter;
   private  ArrayList <String> arrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_user);
        setTitle("User Activity");

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList();




        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        listView.animate().setDuration(1).rotation(180);


        try{
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects!=null && e == null){
                        if(objects.size()>0){

                            for(ParseUser user:objects){

                                arrayList.add(user.getUsername());

                            }
                            listView.setAdapter(arrayAdapter);

                            List<String>fanOf = ParseUser.getCurrentUser().getList("fanOf");
                            if(fanOf!=null){
                              for(String user:arrayList){

                                  if(fanOf.contains(user)){
                                      listView.setItemChecked(arrayList.indexOf(user),true);

                                  }

                              }
                            }



                            listView.animate().setDuration(2000).rotation(360);



                        }
                    }else{

                        showToast("There no one here",FancyToast.INFO);



                    }
                }
            });
        }catch(Exception e){

            showToast(e.getMessage(),FancyToast.ERROR);

        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutUserItem:
                ParseUser.logOut();
                transitionToLoginActivity();

                break;
            case R.id.sendTwitItem:
                transitionToSendTwitActivity();
                


        }

        return super.onOptionsItemSelected(item);
    }

    private void transitionToSendTwitActivity() {
        Intent intent = new Intent(this, SendTweetActivity.class);
        startActivity(intent);

    }

    private void transitionToLoginActivity() {

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void showToast(String message, int type) {

        FancyToast
                .makeText(TwitterUserActivity.this
                        ,message
                        ,FancyToast.LENGTH_SHORT,type
                        ,false)
                .show();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()){
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));

            showToast(arrayList.get(position)+" is followed",FancyToast.INFO);
        }else{

            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List fanOf = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",fanOf);

            showToast(arrayList.get(position)+" is not followed",FancyToast.INFO);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){

                    showToast("Saving was completed",FancyToast.SUCCESS);

                }else{

                    showToast(e.getMessage(),FancyToast.ERROR);

                }
            }
        });



    }

}
