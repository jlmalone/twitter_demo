package com.procurify.procurifytwitterdemo;

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
    private RecyclerView mRecyclerView;
    private TweetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GeoTwitterApiService mTwitterApiClient;

    interface GeoSearchCallback
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

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new TweetAdapter(new ArrayList<Tweet>(), getActivity());

        mRecyclerView.setAdapter(mAdapter);



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override public void onItemClick(View view, final int position)
                    {
                        final Tweet t = lcs.get(position);
                        mGeoSearchCallback.localTweetSelected(t.user.screenName, t.user.name, t.user.profileImageUrl, t.user.id);
                    }
                })
        );

        getTwitterWithSessionAndQuery((float)getArguments().getDouble(BundleKeys.LATITUDE), (float)getArguments().getDouble(BundleKeys.LONGITUDE), (float)getArguments().getInt(BundleKeys.RADIUS));
    }

    ArrayList<Tweet> lcs = new ArrayList<Tweet>();

    private void getTwitterWithSessionAndQuery(float lat, float lon, float kmDistance)
    {
        Log.v("TWITTER", "Twitter.getSessionManager().getActiveSession()" + Twitter.getSessionManager().getActiveSession().toString());

        mTwitterApiClient = new GeoTwitterApiService(Twitter.getSessionManager().getActiveSession());

        GeoTweetService geoTweetService = mTwitterApiClient.getGeoTweetService();

        geoTweetService.geoTweets(lat + "," + lon + "," + kmDistance + "km", 100, new Callback<Response>() {
            @Override
            public void success(Result<Response> listResult) {
                BufferedReader reader = null;
                final StringBuilder sb = new StringBuilder();
                try {
                    try {
                        reader = new BufferedReader(
                                new InputStreamReader(listResult.data.getBody().in()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        final String tweetJson = sb.toString();
                        System.out.println(tweetJson);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement elem = parser.parse(tweetJson);
                        JsonArray jArray = elem.getAsJsonObject().getAsJsonArray("statuses");

                        lcs.clear();

                        for (JsonElement obj : jArray) {
                            Tweet cse = gson.fromJson(obj, Tweet.class);
                            Log.v("TWITTER", "geo List "+cse.user.profileImageUrl+" " +cse.user.profileImageUrlHttps+" "+cse.coordinates.getLatitude()+" "+cse.coordinates.getLongitude() + cse.text.toString() + " " + cse.createdAt + " " + cse.user.screenName);

                            lcs.add(cse);
                        }

                        mAdapter.setData(lcs);
                        mAdapter.notifyDataSetChanged();
                    } finally {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.d("TWITTER", "failed to open and read stream");
                }
            }

            @Override
            public void failure(TwitterException e) {
                Log.v("TWITTER", "geoTweets twitter geo search failure " + e.getLocalizedMessage());
                //TODO NO DATA STATE

            }
        });
    }


}
