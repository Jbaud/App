package com.example.jules.hi;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.jules.hi.RoundedImageView.getCroppedBitmap;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

public class DisplayImage extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String TAG="DisplayImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CreateConnection Id = new CreateConnection();
        // To change the input image into a round one
        Bitmap image_carre = CreateConnection.profil.user.picture;
        Bitmap image_ronde;
        image_ronde = getCroppedBitmap(image_carre,50);
        ImageView imgView=(ImageView) findViewById(R.id.profil);
        Drawable drawable = new BitmapDrawable(getResources(), image_ronde);
        imgView.setImageDrawable(drawable);
        //construct next episode
        // we construct next episode on each time the activity is called
        Episode nextEpisode; //TODO: Set an timer to define an refresh rate

        try {
            nextEpisode =Id.getNextEpisodes();
            // now we edit the default textss
            final TextView textscore = (TextView) findViewById(R.id.score);
            final TextView textshows = (TextView) findViewById(R.id.numbershows);
            final TextView textepisodes = (TextView) findViewById(R.id.numberepisodes);
            final TextView textusername = (TextView) findViewById(R.id.username);
            final TextView textprogress = (TextView) findViewById(R.id.progressvalue);
            final TextView textnextshowname = (TextView) findViewById(R.id.idshow);
            final TextView textnextshowinformations = (TextView) findViewById(R.id.informations);
            /*
            textscore.setText(Integer.toString(profil.user.xp));
            textshows.setText(Integer.toString(profil.user.number_shows));
            textepisodes.setText(Integer.toString(profil.user.episodes_to_watch));
            textusername.setText(profil.user.username);
            textprogress.setText(String.valueOf(profil.user.progress));
            textnextshowname.setText(nextEpisode.show_name);
            textnextshowinformations.setText(nextEpisode.code +" "+nextEpisode.title );
            */
            textscore.setText(Integer.toString(CreateConnection.profil.user.xp));
            textshows.setText(Integer.toString(CreateConnection.profil.user.number_shows));
            textepisodes.setText(Integer.toString(CreateConnection.profil.user.episodes_to_watch));
            textusername.setText(CreateConnection.profil.user.username);
            textprogress.setText(String.valueOf(CreateConnection.profil.user.progress));
            textnextshowname.setText(nextEpisode.show_name);
            textnextshowinformations.setText(nextEpisode.code +" "+nextEpisode.title );

        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG,"Error when gettint next episode");
        }

            Log.v(TAG, "Activity updated and displayed");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DisplayImage Page", // TODO: Define a title for the content shown.
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
                "DisplayImage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jules.hi/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
