package com.procurify.procurifytwitterdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.List;

/**
 * Created by Joseph on 08.03.15.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<Tweet> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public TextView mTextView;
        public TextView mTweetText;
        public TextView mUsername;
        public ImageView mImageView;
        public TextView mDate;
        public FrameLayout mContainer;
        public View.OnClickListener mClickListener;

        public ViewHolder(FrameLayout ll) {
            super(ll);
            mContainer = ll;
//            mCompactTweetView =
//            tweetText = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TweetAdapter(List<Tweet> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    public void setData(List<Tweet> myDataset)
    {
        mDataset = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
//        CompactTweetView v;
//        v = new CompactTweetView(
//                parent.getContext(), new Tweet(null, null, null, null, null, false, null, 1L, null,
//                null, 0L, null, 0L, null, null, null, false, null, 0, false, null, null, null,
//                false, null, false, null, null));

        FrameLayout v =(FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_item, parent, false);



        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        vh.mUsername = (TextView) v.findViewById(R.id.username);
        vh.mDate = (TextView) v.findViewById(R.id.date);
        vh.mTweetText =(TextView) v.findViewById(R.id.tweet);
        vh.mImageView = (ImageView)v.findViewById(R.id.thumbnail);
        vh.mContainer = (FrameLayout)v.findViewById(R.id.container);
        vh.mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTweetText.setText(mDataset.get(position).text);//.setTweet(mDataset.get(position));
        holder.mUsername.setText(mDataset.get(position).user.screenName);
        holder.mDate.setText(mDataset.get(position).createdAt.toString());
        Picasso.with(holder.mImageView.getContext())
                .load(mDataset.get(position).user.profileImageUrl.replace("normal", "bigger"))
                .placeholder(R.drawable.twitter_default_user)
                .into(holder.mImageView);
    }

    private int lastPosition = -1;
    private Context context;


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}