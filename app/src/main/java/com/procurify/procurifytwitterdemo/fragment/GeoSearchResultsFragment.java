package com.procurify.procurifytwitterdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.procurify.procurifytwitterdemo.GeoTwitterApiService;
import com.procurify.procurifytwitterdemo.R;
import com.procurify.procurifytwitterdemo.RecyclerItemClickListener;
import com.procurify.procurifytwitterdemo.adapter.TweetAdapter;
import com.procurify.procurifytwitterdemo.model.GeoSortableTweet;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import retrofit.client.Response;

/**
 * Created by Joseph on 08.03.15.
 */
public class GeoSearchResultsFragment extends Fragment
{
    public static final class BundleKeys
    {
        public static final String LATITUDE = "LATITUDE";
        public static final String LONGITUDE = "LONGITUDE";
        public static final String RADIUS = "RADIUS";
    }

    private static final long CLICK_DELAY_MILLIS = 500L;

    private RecyclerView mRecyclerView;
    private TweetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GeoTwitterApiService mTwitterApiClient;
    private double mCurrentLat;
    private double mCurrentLon;
    private ArrayList<GeoSortableTweet> lcs = new ArrayList<>();

    public interface GeoSearchCallback
    {
        void localTweetSelected(String username, String fullname, String thumbnail,  long userId);
    }

    private GeoSearchCallback mGeoSearchCallback;

    public void setGeoSearchCallback(GeoSearchCallback geoSearchCallback)
    {
        mGeoSearchCallback = geoSearchCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.display_tweets, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View v, Bundle b)
    {
        super.onViewCreated(v, b);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new TweetAdapter(new ArrayList<Tweet>(), getActivity());

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, final int position)
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                final Tweet t = lcs.get(position);
                                mGeoSearchCallback.localTweetSelected(t.user.screenName, t.user.name, t.user.profileImageUrl, t.user.id);
                            }
                        }, CLICK_DELAY_MILLIS);

                    }
                })
        );

        mCurrentLat = getArguments().getDouble(BundleKeys.LATITUDE);
        mCurrentLon = getArguments().getDouble(BundleKeys.LONGITUDE);

        getTwitterWithSessionAndQuery(getArguments().getDouble(BundleKeys.LATITUDE),getArguments().getDouble(BundleKeys.LONGITUDE), getArguments().getInt(BundleKeys.RADIUS));
    }

    private void getTwitterWithSessionAndQuery(double lat, double lon, double kmDistance)
    {
        mTwitterApiClient = new GeoTwitterApiService(Twitter.getSessionManager().getActiveSession());

        GeoTwitterApiService.GeoTweetService geoTweetService = mTwitterApiClient.getGeoTweetService();

        geoTweetService.geoTweets(String.format(getResources().getString(R.string.twitter_geo_submit),lat,lon,(int)kmDistance), 100, new Callback<Response>()
        {
            @Override
            public void success(Result<Response> listResult)
            {
                BufferedReader reader = null;
                final StringBuilder sb = new StringBuilder();
                try
                {
                    try
                    {
                        reader = new BufferedReader(
                                new InputStreamReader(listResult.data.getBody().in()));
                        String line;

                        while ((line = reader.readLine()) != null)
                        {
                            sb.append(line);
                        }

                        final String tweetJson = sb.toString();

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement elem = parser.parse(tweetJson);
                        JsonArray jArray = elem.getAsJsonObject().getAsJsonArray("statuses");

                        lcs.clear();

                        for (JsonElement obj : jArray)
                        {
                            GeoSortableTweet cse = gson.fromJson(obj, GeoSortableTweet.class);
                            cse.setCurrentLocation(mCurrentLat, mCurrentLon );
                            lcs.add(cse);
                        }

                        Collections.sort(lcs);

                        //TODO REMOVE LOGGING
                        for(GeoSortableTweet t:lcs)
                        {
                            Log.v("TWITTER", "Distance from User: "+GeoSortableTweet.calculateTweetDistanceRadians(t));
                        }

                        mAdapter.setData(lcs);
                        mAdapter.notifyDataSetChanged();
                    }
                    finally
                    {
                        reader.close();
                    }
                }
                catch (IOException e)
                {
                    Log.d("TWITTER", "failed to open and read stream");
                }
            }

            @Override
            public void failure(TwitterException e)
            {
                Log.v("TWITTER", "geoTweets twitter geo search failure " + e.getLocalizedMessage());
                //TODO NO DATA STATE
            }
        });
    }
}