package com.socketmill.thisismohit.propotions.Home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by thisismohit on 6/1/16.
 */
public class RecyclerToListViewScrollListener extends LinearLayoutManager {

    public RecyclerToListViewScrollListener(Context context) {
        super(context);
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {

       // Log.e("LAYOUT",String.valueOf(super.getExtraLayoutSpace(state)));
       return 1000  ;
    }
}