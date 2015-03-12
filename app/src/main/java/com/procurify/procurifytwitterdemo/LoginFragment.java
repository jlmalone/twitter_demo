package com.procurify.procurifytwitterdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

/**
 * Created by Joseph on 08.03.15.
 */
public class LoginFragment extends Fragment {

    interface LoginFragmentInterface
    {
        void loginSuccess();
    }
    LoginFragmentInterface callback;

    TwitterLoginButton mTwitterLoginButton;

    public void setLoginFragmentInterface(LoginFragmentInterface lfi)
    {
        callback = lfi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

//
//
//    private void getTwitterWithSessionAndQuery()
//    {
//
//        Log.v("TWITTER", "Twitter.getSessionManager().getActiveSession()" + Twitter.getSessionManager().getActiveSession().toString());
//
//        mTwitterApiClient = new TwitterApiClient(Twitter.getSessionManager().getActiveSession());
//
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
//                    for (Tweet t : listResult.data) {
//                        Log.v("TWITTER", "List " + t.text.toString() + " " + t.createdAt + " " + t.user.screenName);
//                    }
//                }
//                mTwitterLoginButton.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void failure(TwitterException e) {
//                Log.v("TWITTER", "user timeline failure " + e.getLocalizedMessage());
//
//
//            }
//        });
//
//        Log.v("TWITTER","user timeline request");
//
//    }

    @Override
    public void onViewCreated(View v, Bundle b)
    {
        super.onViewCreated(v,b);
        mTwitterLoginButton = (TwitterLoginButton)
                v.findViewById(R.id.login_button);

//        Twitter.getSessionManager().clearActiveSession();

        if(Twitter.getSessionManager().getActiveSession() != null)
        {
//            mTwitterLoginButton.setVisibility(View.GONE);
            if(callback==null && isAdded())
            {
                callback = (LoginFragmentInterface)getActivity();

            }
            callback.loginSuccess();

//            getTwitterWithSessionAndQuery();
        }
        else
        {
            mTwitterLoginButton.setVisibility(View.VISIBLE);
        }

        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {


//                mTwitterLoginButton.setVisibility(View.GONE);

                Log.v("TWITTER", " login success " + result.toString());

//                getTwitterWithSessionAndQuery();

                callback.loginSuccess();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.v("TWITTER", "Twitter Exception " + exception.getLocalizedMessage());
                // Do something on failure
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("TWITTER", "ON ACTIVITY RESULT FRAGMENT");
        // Pass the activity result to the login button.
        mTwitterLoginButton.onActivityResult(requestCode, resultCode,
                data);
    }

}