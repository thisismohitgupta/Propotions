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

/**
 * Created by thisismohit on 3/1/16.
 */
public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {
    protected ImageView displayImage ;
    protected ImageView ProfileImage ;
    protected TextView username;


    public HomeRecyclerViewHolder(View itemView) {
        super(itemView);
        this.displayImage = (ImageView) itemView.findViewById(R.id.displayImages);

        final DisplayMetrics matrix = this.displayImage.getContext().getResources().getDisplayMetrics();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(matrix.widthPixels,matrix.widthPixels);
        this.displayImage.setLayoutParams(params);
        this.ProfileImage = (ImageView) itemView.findViewById(R.id.profile_pic_thumb);
        this.username = (TextView) itemView.findViewById(R.id.DisplayName);

    }
}
