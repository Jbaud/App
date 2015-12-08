package com.example.jules.hi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Jules on 12/10/2015.
 */

// This class handle all the GET and POST request sent to the BetaSeries API.
//


public class CreateConnection {

    public static Profil profil = null;
    // looger Tag
    String TAG = "CreateConnection";
    // Key permits to id an dev to the betaserie api
    String key = "e60e4c409cca";
    //the id user of my account
    int id =386608;
    // for json request
    String USER_AGENT = "Mozilla/5.0";
    // This methode reach the add specified and download the JSON (not async yet)
    // TODO: Urgent, Rendre la méthode async
    private StringBuffer GetJson(int id_user,String path) throws IOException{

            String url = path+"?id="+Integer.toString(id_user)+"&v=2.4&key="+key;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }


    // Récupère les prochains épisodes pour un utilisateur
    public Episode getNextEpisodes()throws IOException {
        StringBuffer response;
        response = GetJson(id, "https://api.betaseries.com/planning/member");

        JSONObject json;

        Episode nextEpisode;
        try {
            json = (JSONObject) new JSONTokener(String.valueOf(response)).nextValue();
            JSONArray ArrayShow = json.getJSONArray("episodes");
            // SELECT FIRST ELT FROM ARRAY
            JSONObject episode = ArrayShow.getJSONObject(0);
            int id = episode.getInt("id");
            String title =  episode.getString("title");
            String description = episode.getString("description");
            String date = episode.getString("date");
            String code = episode.getString("code");

            JSONObject show = episode.getJSONObject("show");
            int id_show = show.getInt("id");
            String showname = show.getString("title");
            JSONObject user = episode.getJSONObject("user");
            boolean seen = user.getBoolean("seen");
            boolean downloaded = user.getBoolean("downloaded");

            nextEpisode = new Episode(id, title, code, id_show, showname, description, date, seen, downloaded);
            return nextEpisode;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
        //there was a problem

    }
    //Récupère l'image d'un show avec l'identifiant du show

    private Bitmap getShowArt(int id_show) throws IOException {

        // not the usual method since the url is different for this
        InputStream in = null;
        try
        {
            URL url = new URL("https://api.betaseries.com/pictures/shows?id="+id_show+"&v=2.4&key=e60e4c409cca");
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();
            in = httpConn.getInputStream();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            Log.v(TAG, "error when downloading show art(type1");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.v(TAG,"error when downloading show art(type2");
        }
        return BitmapFactory.decodeStream(in);
    }
    //Recupère l'image d'un utilisateur
    private Bitmap GetUserPicture(int user_id) throws IOException {

        Bitmap image = null;
        try {
            InputStream is = (InputStream) new URL("https://api.betaseries.com/pictures/members?id=" + user_id + "&v=2.4&key=e60e4c409cca").getContent();
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            Log.v(TAG, "Error when downloading image");
        }
        return image;
    }

    // C'est la méthode la plus importante elle cree un objet profil qui sera utilisé tout au long
    // de l'applicaton.
    /*
    Profil:
            ArrayList<Shows>    -> Liste contenant des informations sur les shows
            User        -> Info sur le compte (nb series,photo etc...)
    Pour mettre a jour ces information il est nécessaire de rappeler cette méthode

    Performance: c'est la méthode la plus critique, de plus on appelle la fonction getArt d'ici

     */
    public Profil getUserInformations() {

        ArrayList<Shows> myShows = new ArrayList<Shows>();
        StringBuffer response = null;



        try {
            response = GetJson(id, "https://api.betaseries.com/members/infos");
            JSONObject json = null;
            try {
                json = (JSONObject) new JSONTokener(String.valueOf(response)).nextValue();
                JSONObject member = json.getJSONObject("member");
                int xp = (int) member.get("xp");
                JSONObject stats = member.getJSONObject("stats");
                int shows = (int) stats.get("shows");
                int seasons = (int) stats.get("seasons");
                int episodes = (int) stats.get("episodes");
                double progress = (double) stats.get("progress");
                // Now we enter inside the shows description
                JSONArray ArrayShow = member.getJSONArray("shows");

                //TODO separate the creation of user and the shows info
                User newUser = new User("baudjules",shows, episodes, xp, progress,GetUserPicture(id));

                Shows newShow = null;
                for (int i = 0; i < ArrayShow.length(); i++) {
                    // we enter sublevel of shows
                    JSONObject show = ArrayShow.getJSONObject(i);
                    int id_show =show.getInt("id");
                    String title_show =  show.getString("title");
                    String description_show = show.getString("description");
                    int seasons_show = show.getInt("seasons");
                    int episodes_show =  show.getInt("episodes");
                    // susb-sub level of show
                    JSONObject user = show.getJSONObject("user");
                    int remaining_show = user.getInt("remaining");
                    // We create the object show
                    newShow = new Shows(id_show, title_show, description_show, seasons_show, episodes_show, remaining_show,getShowArt(id_show));
                    Log.v(TAG,"New show added!");
                    myShows.add(newShow);
                }
                // Creation of the profil instance
                ListeShow completeShows = new ListeShow(myShows);
                profil = new Profil(newUser, completeShows);
                Log.i(TAG,"The profil has been created and can now be used in from the CreateConnection class");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(TAG, "Error when parsing JSON");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "Error in the received JSON file");
        }
        return profil;
    }

}
