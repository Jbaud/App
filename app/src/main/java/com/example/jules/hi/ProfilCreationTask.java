package com.example.jules.hi;

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

    public ProfilCreationTask(TextView textView) {
        textViewReference = new WeakReference<>(textView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        textViewReference.get().setText("Loading");
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
        if (step == 0)
            textViewReference.get().setText("");
        else
            textViewReference.get().setText("Connection error!\\nCheck your network connection...");
    }

}

