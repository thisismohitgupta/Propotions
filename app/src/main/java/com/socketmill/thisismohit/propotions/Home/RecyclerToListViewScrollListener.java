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


    Float pixelPerDp;

    public RecyclerToListViewScrollListener(Context _context) {
        super(_context);
        pixelPerDp = _context.getResources().getDisplayMetrics().density + 0.5f;



    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {

       // Log.e("LAYOUT",String.valueOf(super.getExtraLayoutSpace(state)));


        return (int) (pixelPerDp * 1000);
    }
}