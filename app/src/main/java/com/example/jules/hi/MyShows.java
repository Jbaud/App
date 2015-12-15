package com.example.jules.hi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MyShows extends Activity {




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Fonctionne
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_my_shows);
        ListView list;
        MyShowsAdapter adapter = new MyShowsAdapter(MyShows.this);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
         */
        // Adaptation Recycleview

        //tentative avec Recycle View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShowsAdapterRV mAdapter = new ShowsAdapterRV(CreateConnection.profil);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}