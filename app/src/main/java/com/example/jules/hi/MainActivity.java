package com.example.jules.hi;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String TAG ="MainActivity";
    private BroadcastReceiver broadcastReceiverOk;
    private BroadcastReceiver broadcastReceiverKo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // if not the first time we open the app
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        //ThreadPolicy instance creation
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Creation of the page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //Filtres pour le broadcast receiver
        IntentFilter intentFilterOk = new IntentFilter("com.example.PROFILE_CREATED");
        IntentFilter intentFilterKo = new IntentFilter("com.example.CONNECTION_ERROR");

        //Action à effectuer quand le profil a bien été créé
        broadcastReceiverOk = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intentOk) {
                Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Connected! Starting the intent...");

                //HomePage activity
                Intent intent = new Intent(getApplicationContext(), HomePage.class);

                //disable the back button to this activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        };

        //Actions à effectuer si erreur de connection
        broadcastReceiverKo = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intentKo) {
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
            }
        };

        registerReceiver(broadcastReceiverOk, intentFilterOk);
        registerReceiver(broadcastReceiverKo, intentFilterKo);

        Log.v(TAG, "Connection in progress...");

        //AsyncTask pour la creation de profil
        new ProfilCreationTask((TextView) findViewById(R.id.loading_status),getApplicationContext()).execute();
        //CreateConnection connection = new CreateConnection();
        //connection.getProfile();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jules.hi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jules.hi/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);
        //client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    // Create an notification view with logo + title + text
    public void sendNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.beta_icone);
        builder.setContentTitle("Database updated");
        builder.setContentText("your profile and show informations have been updated.");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}
