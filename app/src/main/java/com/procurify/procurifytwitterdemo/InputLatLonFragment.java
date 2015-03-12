package com.procurify.procurifytwitterdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by Joseph on 10.03.15.
 */
public class InputLatLonFragment extends Fragment
{
    interface CoordinateSelectionCallback
    {
       void selectCoordinates(double lat, double lon);
    }

    // Input fields, lat and lon
    private Button mEnter;
    private EditText mLat;
    private EditText mLon;

    public InputLatLonFragment(CoordinateSelectionCallback coordinateSelectionCallback)
    {
        super();
        mCoordinateSelectionCallback = coordinateSelectionCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_location, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View v, Bundle b)
    {
        super.onViewCreated(v,b);
        mEnter = (Button)v.findViewById(R.id.enter);
        mLat = (EditText)v.findViewById(R.id.lat);
        mLon = (EditText)v.findViewById(R.id.lon);
    }



    CoordinateSelectionCallback mCoordinateSelectionCallback;



    //onCreate and onCreateView to hook up components

    //signal activity to trigger

    //TODO Validation -90 +90  -180 180 for lat and lon

}
