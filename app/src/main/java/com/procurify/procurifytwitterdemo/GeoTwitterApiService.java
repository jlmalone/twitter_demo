package com.procurify.procurifytwitterdemo;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Joseph on 11.03.15.
 */
public class GeoTwitterApiService extends TwitterApiClient {
    public GeoTwitterApiService(TwitterSession session) {
        super(session);
    }

    /**
     * Provide GeoTweetService with defined endpoints
     */
    public GeoTweetService getGeoTweetService() {
        return getService(GeoTweetService.class);
    }




    // example users/geoTweets service endpoint
    public interface GeoTweetService
    {
        @GET("/1.1/search/tweets.json")
        void geoTweets(@Query("geocode") String geocode, @Query("count") int count, Callback<Response> cb);
    }




}

