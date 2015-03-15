package com.procurify.procurifytwitterdemo.model;

import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.Place;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by Joseph on 14.03.15.
 */
public class GeoSortableTweet extends Tweet implements Comparable<GeoSortableTweet>
{
    //    private static final double EARTH_RADIUS = 6371.00; // Radius in Kilometers default
    
    double[] mCurrentLocation;


    public GeoSortableTweet(Coordinates coordinates, String createdAt, Object currentUserRetweet, TweetEntities entities, Integer favoriteCount, boolean favorited, String filterLevel, long id, String idStr, String inReplyToScreenName, long inReplyToStatusId, String inReplyToStatusIdStr, long inReplyToUserId, String inReplyToUserIdStr, String lang, Place place, boolean possiblySensitive, Object scopes, int retweetCount, boolean retweeted, Tweet retweetedStatus, String source, String text, boolean truncated, User user, boolean withheldCopyright, List<String> withheldInCountries, String withheldScope) {
        super(coordinates, createdAt, currentUserRetweet, entities, favoriteCount, favorited, filterLevel, id, idStr, inReplyToScreenName, inReplyToStatusId, inReplyToStatusIdStr, inReplyToUserId, inReplyToUserIdStr, lang, place, possiblySensitive, scopes, retweetCount, retweeted, retweetedStatus, source, text, truncated, user, withheldCopyright, withheldInCountries, withheldScope);
    }

    public void setCurrentLocation(double lat, double lon)
    {
        mCurrentLocation = new double[]{lat, lon};
    }

    public void setCurrentLocation(double[] location)
    {
        mCurrentLocation = location;
    }

    public double[] getCurrentLocation()
    {
        return mCurrentLocation;
    }
    


    //set to nearest to furthest
    @Override
    public int compareTo(GeoSortableTweet another) {
        if(mCurrentLocation==null)
        {
            return 0;
        }

        double difference = calculateTweetDistanceRadians(this)-calculateTweetDistanceRadians(another);

        if(difference == 0)
            return 0;
        else if(difference > 0)
            return 1;
        else
            return -1;
    }

    public static double calculateTweetDistanceRadians(GeoSortableTweet t)
    {
        return calculateDistanceRadians(t.getCurrentLocation()[0],t.getCurrentLocation()[1]
                ,t.coordinates.getLatitude().doubleValue(), t.coordinates.getLongitude().doubleValue());
    };

    public static double calculateDistanceRadians(double lat1, double lon1, double lat2,   double lon2)
    {
//        double Radius = EARTH_RADIUS; //6371.00;
        double dLat = toRadians(lat2-lat1);
        double dLon = toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(toRadians(lat1)) *   Math.cos(toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return  c;// * EARTH RADIUS if you want to return earth surface distance
    }

    public static double toRadians(double degree){
        // Value degree * Pi/180
        double res = degree * 3.1415926 / 180;
        return res;
    }
}
