package com.procurify.procurifytwitterdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Joseph on 11.03.15.
 */
public class TwitterProfile extends Fragment
{
    public class BundleKey
    {
        public static final String USERNAME = "USERNAME";
        public static final String THUMBNAIL = "THUMBNAIL";
        public static final String FULL_NAME = "FULL_NAME";
        public static final String USER_ID = "USER_ID";
    }

    private RecyclerView mRecyclerView;
    private TweetAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GeoTwitterApiService mTwitterApiClient;
    private TextView mScreenName;
    private TextView mFullName;
    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.twitter_profile, container, false);
      Log.v("TWITTER", "has full name " + getArguments().containsKey(BundleKey.FULL_NAME));
        return rootView;
    }


    @Override
    public void onViewCreated(View v, Bundle b)
    {
        super.onViewCreated(v, b);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mScreenName = (TextView) v.findViewById(R.id.username);
        mFullName = (TextView) v.findViewById(R.id.full_name);
        mImageView = (ImageView) v.findViewById(R.id.thumbnail);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        mAdapter = new TweetAdapter(new ArrayList<Tweet>(), getActivity());

        mRecyclerView.setAdapter(mAdapter);

        mFullName.setText(getArguments().getString(BundleKey.FULL_NAME));
        mScreenName.setText(getArguments().getString(BundleKey.USERNAME));
        Picasso.with(getActivity())
                .load(getArguments().getString(BundleKey.THUMBNAIL).replace("normal", "bigger"))
                .placeholder(R.drawable.twitter_default_user)
                .into(mImageView);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override public void onItemClick(View view, int position)
                    {
                        // do whatever

                        Toast.makeText(getActivity(), R.string.item_clicked, Toast.LENGTH_SHORT).show();
                    }
                })
        );

        getTwitterWithSessionAndQuery(getArguments().getLong(BundleKey.USER_ID));

    }


    private void getTwitterWithSessionAndQuery(long userId)
    {
        mTwitterApiClient = new GeoTwitterApiService(Twitter.getSessionManager().getActiveSession());

        StatusesService ss = mTwitterApiClient.getStatusesService();

        Log.v("TWITTER","get status service "+ss.toString());

        ss.userTimeline(userId, null, 20, null, null, null, null, null, null, new Callback<List<Tweet>>()
        {
            @Override
            public void success(Result<List<Tweet>> listResult)
            {
                if (listResult != null && listResult.data != null)
                {
                    mAdapter.setData(listResult.data);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(TwitterException e)
            {
                Log.v("TWITTER", "user timeline failure " + e.getLocalizedMessage());
            }
        });

    }
}
