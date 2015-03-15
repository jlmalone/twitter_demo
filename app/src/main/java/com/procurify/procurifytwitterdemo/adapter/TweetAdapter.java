package com.procurify.procurifytwitterdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.procurify.procurifytwitterdemo.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Created by Joseph on 08.03.15.
 */
public class TweetAdapter<T extends Tweet> extends RecyclerView.Adapter<TweetAdapter.ViewHolder>
{
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTweetText;
        public TextView mUsername;
        public ImageView mImageView;
        public TextView mRetweets;
        public TextView mFavourites;
        public TextView mDate;
        public FrameLayout mContainer;

        public ViewHolder(FrameLayout ll)
        {
            super(ll);
            mContainer = ll;
        }
    }

    private List<T> mDataset;
    private Context context;

    public TweetAdapter(List<T> myDataset, Context context)
    {
        mDataset = myDataset;
        this.context = context;
    }

    public void setData(List<T> myDataset)
    {
        mDataset = myDataset;
    }

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        FrameLayout v =(FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.mUsername = (TextView) v.findViewById(R.id.username);
        vh.mRetweets = (TextView) v.findViewById(R.id.retweets);
        vh.mFavourites = (TextView) v.findViewById(R.id.favourites);
        vh.mDate = (TextView) v.findViewById(R.id.date);
        vh.mTweetText =(TextView) v.findViewById(R.id.tweet);
        vh.mImageView = (ImageView)v.findViewById(R.id.thumbnail);
        vh.mContainer = (FrameLayout)v.findViewById(R.id.container);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        holder.mTweetText.setText(mDataset.get(position).text);
        holder.mUsername.setText("@"+mDataset.get(position).user.screenName);
        holder.mFavourites.setText(String.format(context.getResources().getString(R.string.favourites),
                mDataset.get(position).favoriteCount));
        holder.mRetweets.setText(String.format(context.getResources().getString(R.string.retweets),mDataset.get(position).retweetCount));
        holder.mDate.setText(mDataset.get(position).createdAt.toString());
        Picasso.with(holder.mImageView.getContext())
                .load(mDataset.get(position).user.profileImageUrl.replace("normal", "bigger"))
                .placeholder(R.drawable.twitter_default_user)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }
}