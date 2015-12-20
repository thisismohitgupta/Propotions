package com.socketmill.thisismohit.propotions.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socketmill.thisismohit.propotions.R;

/**
 * Created by thisismohit on 03/12/15.
 */
public class Comments extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.commentfragment,container,false);
        return view ;
    }
}
