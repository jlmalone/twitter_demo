package com.procurify.procurifytwitterdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends FragmentActivity implements InputLatLonFragment.CoordinateSelectionCallback, LoginFragment.LoginFragmentInterface{


    private static final String TAG = MainActivity.class.getSimpleName();
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "6usSEWieXvRSQj8p1NLoQ";//"zS210fGnQpe3WBQ64tinyUB74";
    private static final String TWITTER_SECRET = "DEakgzitFPh6F9CouqwSeX6HFemyxnqusH2Aflro8";//"RstXCD5DWMLWIYnOAmiZd3qN16JmFVysqhI40E6B4gnkThGCcB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
//            InputLatLonFragment latLonFragment = new InputLatLonFragment(this);
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container,latLonFragment )
//                    .commit();


            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setLoginFragmentInterface(this);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,loginFragment )
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        Log.v("TWITTER", "ON ACTIVITY RESULT ACTIVITY");
    }


    @Override
    public void loginSuccess() {
        Log.v(TAG, "Show Timeline Fragment on Login Success");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ShowTimelineFragment())
                .commit();
    }

    @Override
    public void selectCoordinates(double lat, double lon) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ShowTimelineFragment()).addToBackStack("twitter")
                .commit();
    }
}
