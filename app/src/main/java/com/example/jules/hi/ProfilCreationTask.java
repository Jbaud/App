package com.example.jules.hi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by laure on 12/29/15.
 */

// Cette méthode permet de créer le profil (user/shows/nextepisode) en asynchrone
// par l'appel à la classe CreateConnection et sa méthode getProfile :)

public class ProfilCreationTask extends AsyncTask<Void, Integer, Void> {

    private final WeakReference<TextView> textViewReference;
    private Context context;

    public ProfilCreationTask(TextView textView, Context context) {
        textViewReference = new WeakReference<>(textView);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        textViewReference.get().setText("Loading...");
    }

    @Override
    protected Void doInBackground(Void... params) {
        CreateConnection connection = new CreateConnection();
        int result = connection.getProfile();
        publishProgress(result);
        return (null);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Integer step = values[0];
        if (step == 0) {
            textViewReference.get().setText("");
            context.sendBroadcast(new Intent("com.example.PROFILE_CREATED"));
        } else {
            textViewReference.get().setText("Connection error!\nCheck your network connection...");
            context.sendBroadcast(new Intent("com.example.CONNECTION_ERROR"));
        }
    }

}

