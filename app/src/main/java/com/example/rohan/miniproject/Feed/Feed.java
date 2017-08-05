package com.example.rohan.miniproject.Feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rohan.miniproject.R;

/**
 * Created by Rohan on 31/03/17.
 */
public class Feed extends Fragment {

    RecyclerView rvonline;
    FeedAdapter feedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("test", "tab 1 oncreateview");
        View view = inflater.inflate(R.layout.feed, null);
        rvonline = (RecyclerView)view.findViewById(R.id.rvonline);
        rvonline.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        feedAdapter = new FeedAdapter(getActivity(),this);
        rvonline.setAdapter(feedAdapter);


        return view;
    }

    @Override
    public void onPause() {
        feedAdapter.removecallback();
        super.onPause();
    }
}
