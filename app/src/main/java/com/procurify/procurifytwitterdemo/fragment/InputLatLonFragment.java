package com.procurify.procurifytwitterdemo.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
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
import android.widget.TextView;
import android.widget.Toast;

import com.procurify.procurifytwitterdemo.R;

import java.util.Locale;

/**
 * Created by Joseph on 10.03.15.
 */
public class InputLatLonFragment extends Fragment
{
    public interface CoordinateSelectionCallback
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
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mCoordinateSelectionCallback.selectCoordinates(Double.parseDouble(mLat.getText().toString()), Double.parseDouble(mLon.getText().toString()), Integer.parseInt(mRadius.getText().toString()));

                }
                catch (NumberFormatException nfe)
                {
                    Toast.makeText(getActivity(), "Invalid Numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });


        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.ACCURACY_HIGH);
        Location ll = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,true));
        if(ll !=null)
        {
            mLat.setText((ll.getLatitude() + "").substring(0,9));
            mLon.setText((ll.getLongitude() + "").substring(0,9));
            mRadius.setText(""+DEFAULT_RADIUS_KM);

        }
    }

    LocationManager locationManager;
    LocationListener locationListener;

    //onCreate and onCreateView to hook up components

    //signal activity to trigger

    //TODO Validation -90 +90  -180 180 for lat and lon

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location ll)
        {

            if(ll !=null && mLat.getText().toString().equals("")
                    && mLon.getText().toString().equals("")
                    && mRadius.getText().toString().equals(""))
            {
                mLat.setText((ll.getLatitude() + "").substring(0,9));
                mLon.setText((ll.getLongitude() + "").substring(0,9));
                mRadius.setText(""+DEFAULT_RADIUS_KM);

            }
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}
