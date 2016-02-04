package com.socketmill.thisismohit.propotions.Home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socketmill.thisismohit.propotions.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by thisismohit on 3/1/16.
 */
public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {
    protected ImageView displayImage ;
    protected CircleImageView ProfileImage;
    protected TextView username;
    protected ImageView likeButton;
    protected TextView likeButtonCount;
    protected ImageView commentView;
    protected RelativeLayout mainimages;


    public HomeRecyclerViewHolder(View itemView) {
        super(itemView);
        this.displayImage = (ImageView) itemView.findViewById(R.id.displayImages);

        final DisplayMetrics matrix = this.displayImage.getContext().getResources().getDisplayMetrics();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(matrix.widthPixels,matrix.widthPixels);
        this.displayImage.setLayoutParams(params);
        this.ProfileImage = (CircleImageView) itemView.findViewById(R.id.profile_pic_thumb);
        this.username = (TextView) itemView.findViewById(R.id.DisplayName);

        this.likeButton = (ImageView) itemView.findViewById(R.id.likeButton);
        this.commentView = (ImageView) itemView.findViewById(R.id.commentButton);

        this.mainimages = (RelativeLayout) itemView.findViewById(R.id.mainimages);
        this.likeButtonCount = (TextView) itemView.findViewById(R.id.likeButtonCount);

    }
}
