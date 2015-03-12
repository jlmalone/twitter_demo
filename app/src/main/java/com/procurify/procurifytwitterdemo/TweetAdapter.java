package com.procurify.procurifytwitterdemo;

import android.content.Context;
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
        public TextView tweetText;
        public ImageView imageView;
            public FrameLayout container;

        public ViewHolder(FrameLayout ll) {
            super(ll);
            container = ll;
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
        vh.tweetText =(TextView) v.findViewById(R.id.tweet);
        vh.imageView = (ImageView)v.findViewById(R.id.thumbnail);
//        vh.container = (FrameLayout)v.findViewById(R.id.item_layout_container);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        holder.tweetText.setText(mDataset.get(position).text);//.setTweet(mDataset.get(position));
//        holder.imageView.

        Picasso.with(holder.imageView.getContext())
                .load(mDataset.get(position).user.profileImageUrl)
                .into(holder.imageView);

        // Here you apply the animation when the view is bound
        setAnimation(holder.container, position);
    }

    private int lastPosition = -1;
    private Context context;
    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}