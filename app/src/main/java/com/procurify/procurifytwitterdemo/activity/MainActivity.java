package com.procurify.procurifytwitterdemo.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.procurify.procurifytwitterdemo.fragment.TwitterProfileFragment;
import com.procurify.procurifytwitterdemo.fragment.GeoSearchResultsFragment;
import com.procurify.procurifytwitterdemo.R;
import com.procurify.procurifytwitterdemo.fragment.InputLatLonFragment;
import com.procurify.procurifytwitterdemo.fragment.LoginFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;


import io.fabric.sdk.android.Fabric;


public class MainActivity extends FragmentActivity implements InputLatLonFragment.CoordinateSelectionCallback, LoginFragment.LoginFragmentInterface, GeoSearchResultsFragment.GeoSearchCallback
{
    private static final String TAG = MainActivity.class.getSimpleName();
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "6usSEWieXvRSQj8p1NLoQ";//"zS210fGnQpe3WBQ64tinyUB74";
    private static final String TWITTER_SECRET = "DEakgzitFPh6F9CouqwSeX6HFemyxnqusH2Aflro8";//"RstXCD5DWMLWIYnOAmiZd3qN16JmFVysqhI40E6B4gnkThGCcB";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setLoginFragmentInterface(this);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,loginFragment )
                    .commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if (fragment != null)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void loginSuccess()
    {
        InputLatLonFragment latLonFragment = new InputLatLonFragment(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, latLonFragment)
                .commit();
    }

    @Override
    public void selectCoordinates(double lat, double lon, int radius)
    {
        GeoSearchResultsFragment geoSearchResultsFragment =  new GeoSearchResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(GeoSearchResultsFragment.BundleKeys.LATITUDE, lat);
        bundle.putDouble(GeoSearchResultsFragment.BundleKeys.LONGITUDE, lon);
        bundle.putInt(GeoSearchResultsFragment.BundleKeys.RADIUS, radius);

        geoSearchResultsFragment.setArguments(bundle);
        geoSearchResultsFragment.setGeoSearchCallback(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, geoSearchResultsFragment).addToBackStack("twitter")
                .commit();
    }

    @Override
    public void localTweetSelected(String username, String fullname, String thumbnail, long userId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(TwitterProfileFragment.BundleKey.USER_ID, userId);
        bundle.putString(TwitterProfileFragment.BundleKey.FULL_NAME, fullname);
        bundle.putString(TwitterProfileFragment.BundleKey.THUMBNAIL, thumbnail);
        bundle.putString(TwitterProfileFragment.BundleKey.USERNAME, username);
        TwitterProfileFragment twitterProfileFragment = new TwitterProfileFragment();
        twitterProfileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, twitterProfileFragment).addToBackStack("twitter")
                .commit();
    }
}
