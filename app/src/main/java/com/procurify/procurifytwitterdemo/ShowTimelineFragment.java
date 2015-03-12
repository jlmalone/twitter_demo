package com.procurify.procurifytwitterdemo;

import android.os.Bundle;
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
import com.google.gson.reflect.TypeToken;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.client.Response;

/**
 * Created by Joseph on 08.03.15.
 */
public class ShowTimelineFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TweetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    MyTwitterApiClient mTwitterApiClient;


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
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new TweetAdapter(new ArrayList<Tweet>(), getActivity());

        mRecyclerView.setAdapter(mAdapter);
        getTwitterWithSessionAndQuery();

    }


    private void getTwitterWithSessionAndQuery()
    {

        Log.v("TWITTER", "Twitter.getSessionManager().getActiveSession()" + Twitter.getSessionManager().getActiveSession().toString());

        mTwitterApiClient = new MyTwitterApiClient(Twitter.getSessionManager().getActiveSession());

        CustomService customService = mTwitterApiClient.getCustomService();

        customService.show("49.25,-123.1,2mi", new Callback<Response>()
        {
            @Override
            public void success(Result<Response> listResult)
            {

                Log.v("TWITTER", " group returned " + listResult.response.getBody().toString());

                BufferedReader reader = null;
                final StringBuilder sb = new StringBuilder();
                try
                {
                    try
                    {
                        reader = new BufferedReader(
                                new InputStreamReader(listResult.data.getBody().in()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        final String tweetJson = sb.toString();//.replace("{\"statuses\":[{","\"statuses\":[{").replace("]}","]");
                        System.out.println(tweetJson);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement elem = parser.parse(tweetJson);
                        JsonArray jArray = elem.getAsJsonObject().getAsJsonArray("statuses");
//                        JsonArray jArray = parser.parse(tweetJson).getAsJsonArray();

                        ArrayList<Tweet> lcs = new ArrayList<Tweet>();

                        for (JsonElement obj : jArray)
                        {
                            Tweet cse = gson.fromJson(obj, Tweet.class);
                            lcs.add(cse);
                        }

                        Log.d("TWITTER", tweetJson + " ");

                        mAdapter.setData(lcs);
                        mAdapter.notifyDataSetChanged();
                    }
                    finally
                    {
                        reader.close();
                    }
                } catch (IOException e)
                {
                    Log.d("TWITTER", "failed to open and read stream");
                }


//                if (listResult != null && listResult.data != null) {
//                    mAdapter.setData(listResult.data);
//                    mAdapter.notifyDataSetChanged();
//                    for (Tweet t : listResult.data) {
//                        Log.v("TWITTER", "List " + t.text.toString() + " " + t.createdAt + " " + t.user.screenName);
//                    }
//                }
//                //TODO
//                Log.v("", "TODO report error");
//                mTwitterLoginButton.setVisibility(View.GONE);
            }

            @Override
            public void failure(TwitterException e) {
                Log.v("TWITTER", "user timeline failure " + e.getLocalizedMessage());


            }
        });

//        StatusesService ss = mTwitterApiClient.getStatusesService();
//
//        Log.v("TWITTER","get status service "+ss.toString());
//
//
//        ss.userTimeline(null, "shazamfeed", 200, null, null, null, null, null, null, new Callback<List<Tweet>>() {
//            @Override
//            public void success(Result<List<Tweet>> listResult) {
//
//                Log.v("TWITTER", " group returned ");
//                if (listResult != null && listResult.data != null) {
//                    mAdapter.setData(listResult.data);
//                    mAdapter.notifyDataSetChanged();
//                    for (Tweet t : listResult.data) {
//                        Log.v("TWITTER", "List " + t.text.toString() + " " + t.createdAt + " " + t.user.screenName);
//                    }
//                }
////                //TODO
////                Log.v(TAG, "TODO report error");
////                mTwitterLoginButton.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void failure(TwitterException e) {
//                Log.v("TWITTER", "user timeline failure " + e.getLocalizedMessage());
//
//
//            }
//        });

        Log.v("TWITTER", "user timeline request");

    }


}
