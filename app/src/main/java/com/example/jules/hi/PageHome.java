package com.example.jules.hi;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.jules.hi.RoundedImageView.getCroppedBitmap;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class PageHome extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String TAG="HomePage";
    private BroadcastReceiver showsReceiverOk;
    private BroadcastReceiver showsReceiverKo;
    private boolean showsUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Create de homepage");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showsUpdated = false;

        // Si on a bien créé le profil
        if (CreateConnection.profil != null)
            Log.v(TAG, "Profil existant");

            // On rogne l'image de l'user pour la rendre ronde
            Bitmap image_carre = CreateConnection.profil.user.picture;
            Bitmap user_image;
            user_image = getCroppedBitmap(image_carre,50);

            //on enregistre cette image en tant qu'image de profil
            ImageView imgView = (ImageView) findViewById(R.id.user_picture);
            Drawable drawable = new BitmapDrawable(getResources(), user_image);
            imgView.setImageDrawable(drawable);
        Log.v(TAG, "Image ok");

        // on récupère les textview à éditer
        final TextView text_user_name = (TextView) findViewById(R.id.user_name);
        final TextView text_shows_nb = (TextView) findViewById(R.id.stats_series_number);
        final TextView text_episodes_nb = (TextView) findViewById(R.id.stats_episodes_number);
        final TextView text_heures = (TextView) findViewById(R.id.stats_heures_number);
        final TextView text_next_show = (TextView) findViewById(R.id.next_show);
        final TextView text_next_episode = (TextView) findViewById(R.id.next_episode);
        final TextView text_next_date = (TextView) findViewById(R.id.next_date);
        final TextView text_next_description = (TextView) findViewById(R.id.next_description);

        Log.v(TAG, "Textviews récupérés");

        // on edite les textes par défaut
        text_user_name.setText(CreateConnection.profil.user.username);
        text_shows_nb.setText(Integer.toString(CreateConnection.profil.user.shows_nb));
        text_episodes_nb.setText(Integer.toString(CreateConnection.profil.user.episodes_vus));
        text_heures.setText(String.valueOf(CreateConnection.profil.user.minutes));
        text_next_show.setText(CreateConnection.profil.next_episode.show_name);
        text_next_episode.setText(CreateConnection.profil.next_episode.episode_nb
                +" - "+CreateConnection.profil.next_episode.title);
        text_next_date.setText(CreateConnection.profil.next_episode.date);
        text_next_description.setText(CreateConnection.profil.next_episode.description);

        Log.v(TAG, "Activity updated and displayed");

        //Filtres pour le broadcast receiver
        IntentFilter intentFilterOk = new IntentFilter("com.example.SHOWS_CREATED");
        IntentFilter intentFilterKo = new IntentFilter("com.example.SHOWS_ERROR");

        //Action à effectuer quand le profil a bien été créé
        showsReceiverOk = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intentOk) {
                Toast.makeText(context, "Séries mises à jour", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Séries mises à jour avec les images");
                sendNotification();
                showsUpdated = true;
            }
        };

        //Actions à effectuer si erreur de connection
        showsReceiverKo = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intentKo) {
                Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
            }
        };

        registerReceiver(showsReceiverOk, intentFilterOk);
        registerReceiver(showsReceiverKo, intentFilterKo);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //On lance l'asyntask qui va mettre à jour les shows avec les images
        if (showsUpdated == false)
            Log.v(TAG, "DL des images pour les séries...");
            new ShowsCreationTask(getApplicationContext()).execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "HomePage Page", // TODO: Define a title for the content shown.
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (showsReceiverOk != null) {
            unregisterReceiver(showsReceiverOk);
            showsReceiverOk = null;
        }
        if (showsReceiverKo != null) {
            unregisterReceiver(showsReceiverKo);
            showsReceiverKo = null;
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "HomePage Page", // TODO: Define a title for the content shown.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //declenchement des actions depuis le menu de l'action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menu_shows:
                if (showsUpdated == true) {
                    //ShowsPage activity
                    Intent intentShows = new Intent(getApplicationContext(), PageShows.class);
                    startActivity(intentShows);
                }
                else
                    Toast.makeText(this, "Non disponible", Toast.LENGTH_SHORT).show();
                break;
            // action with ID action_refresh was selected
            case R.id.menu_about:
                Intent intentVote = new Intent(getApplicationContext(), PageAbout.class);
                Log.v(TAG, "Starting the intent!");
                startActivity(intentVote);
                break;
            default:
                break;
        }
        return true;
    }

    // Create an notification view with logo + title + text
    public void sendNotification() {

        Log.v(TAG, "Envoi de notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.beta_icone);
        builder.setContentTitle("BetaSeries - màj");
        builder.setContentText("La liste des séries a été mise à jour");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

}
