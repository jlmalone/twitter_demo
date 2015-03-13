package com.procurify.procurifytwitterdemo;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Joseph on 10.03.15.
 */
public class InputLatLonFragment extends Fragment
{
    interface CoordinateSelectionCallback
    {
       void selectCoordinates(double lat, double lon, int radius);
    }

    private static final int DEFAULT_RADIUS_KM = 10;
    // Input fields, lat and lon
    private Button mEnter;
    private EditText mLat;
    private EditText mLon;
    private EditText mRadius;

    private CoordinateSelectionCallback mCoordinateSelectionCallback;


    public InputLatLonFragment(CoordinateSelectionCallback coordinateSelectionCallback)
    {
        super();
        mCoordinateSelectionCallback = coordinateSelectionCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
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
        mRadius = (EditText)v.findViewById(R.id.radius);
        mEnter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    mCoordinateSelectionCallback.selectCoordinates(Double.parseDouble(mLat.getText().toString()), Double.parseDouble(mLon.getText().toString()), Integer.parseInt(mRadius.getText().toString()));

                }catch (NumberFormatException nfe)
                {
                    Toast.makeText(getActivity(), "Invalid Numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

//        LocationListener locationListener = new MyLocationListener();
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        Location ll = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(ll!=null)
        {
            mLat.setText((ll.getLatitude() + "").substring(0,9));
            mLon.setText((ll.getLongitude() + "").substring(0,9));
            mRadius.setText(""+DEFAULT_RADIUS_KM);
        }
    }

    //onCreate and onCreateView to hook up components

    //signal activity to trigger

    //TODO Validation -90 +90  -180 180 for lat and lon

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            String longitude = "Longitude: " + loc.getLongitude();
            String latitude = "Latitude: " + loc.getLatitude();

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}
