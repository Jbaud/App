package com.example.jules.hi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by laure on 12/31/15.
 */

public class ShowsCreationTask extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private String TAG="ShowsCreation";

    public ShowsCreationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        CreateConnection connection = new CreateConnection();
        Log.v(TAG, "Récupération des images...");
        int result = connection.updateShowsPicture();
        publishProgress(result);
        return (null);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Integer step = values[0];
        if (step == 0) {
            Log.v(TAG, "Toutes les images ont été récupérées");
            context.sendBroadcast(new Intent("com.example.SHOWS_CREATED"));
        } else {
            Log.v(TAG, "Erreur dans la récupération des images");
            context.sendBroadcast(new Intent("com.example.SHOWS_ERROR"));
        }
    }

}
