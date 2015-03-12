package com.procurify.procurifytwitterdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Joseph on 10.03.15.
 */
public class InputLatLonFragment extends Fragment
{
    interface CoordinateSelectionCallback
    {
       void selectCoordinates(double lat, double lon, int radius);
    }

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
    }

    //onCreate and onCreateView to hook up components

    //signal activity to trigger

    //TODO Validation -90 +90  -180 180 for lat and lon

}
