package com.example.jules.hi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jules on 12/10/2015.
 */

// This class handle all the GET and POST request sent to the BetaSeries API.
//


public class CreateConnection {

    public static Profil profil = null;
    // looger Tag
    String TAG = "CreateConnection";
    //String user = "baudjules";
    // Key permits to id an dev to the betaserie api
    String key = "e60e4c409cca";
    //String mdp_hash = "2d55ca76930bc2102477014e8f503c96";
    //the id user of myaccount
    int id =386608;
    String USER_AGENT = "Mozilla/5.0";
    SimpleDateFormat formater = null;

    // This methode reach the add specified and download the JSON (not async)
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
    // Il faudrait renvoyer un objet de type Episode
    public Episode getNextEpisodes()throws IOException {
        StringBuffer response;
        response = GetJson(id, "https://api.betaseries.com/planning/member");

        JSONObject json = null;

        Episode nextEpisode = null;
        try {
            json = (JSONObject) new JSONTokener(String.valueOf(response)).nextValue();
            JSONArray ArrayShow = json.getJSONArray("episodes");
            // SELECT FIRST ELT FROM ARRAY
            JSONObject episode = ArrayShow.getJSONObject(0);
            int id = (int) episode.getInt("id");
          //  Log.v(TAG,"------------>id next episode="+Integer.toString(id));
            String title = (String) episode.getString("title");
            String description = (String) episode.getString("description");
            String date = (String) episode.getString("date");
            String code = (String) episode.getString("code");

            JSONObject show = episode.getJSONObject("show");
            int id_show = (int) show.getInt("id");
            String showname = (String) show.getString("title");
            JSONObject user = episode.getJSONObject("user");
            boolean seen = (boolean) user.getBoolean("seen");
            boolean downloaded = (boolean) user.getBoolean("downloaded");

            nextEpisode = new Episode(id, title, code, id_show, showname, description, date, seen, downloaded);
            return nextEpisode;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
        //there was a problem

    }
    //Récupère l'image d'un show
    public Bitmap getShowArt(int id_show) throws IOException {

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
        Bitmap bmpimg = BitmapFactory.decodeStream(in);

        return bmpimg;
    }
    //Recupère l'image d'un utilisateur
    public Bitmap GetUserPicture(int user_id) throws IOException {

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
                System.out.println("------>XP value of member: " + xp);
                JSONObject stats = member.getJSONObject("stats");
                int shows = (int) stats.get("shows");
                System.out.println("------>Number of shows: " + shows);
                int seasons = (int) stats.get("seasons");
                System.out.println("------>Number of seasons: " + seasons);
                int episodes = (int) stats.get("episodes");
                System.out.println("------>Number of seen episodes: " + episodes);
                double progress = (double) stats.get("progress");
                System.out.println("------>Overall progress: " + progress + "%");
                // Now we enter inside the shows description
                JSONArray ArrayShow = member.getJSONArray("shows");


                User newUser = new User("baudjules",shows, episodes, xp, progress,GetUserPicture(id));


                System.out.println("ArrayShow size : " + ArrayShow.length());
                Shows newShow = null;
                for (int i = 0; i < ArrayShow.length(); i++) {
                    // we enter sublevel of shows
                    JSONObject show = ArrayShow.getJSONObject(i);
                    int id_show = (int) show.get("id");
                    String title_show = (String) show.getString("title");
                    String description_show = (String) show.getString("description");
                    int seasons_show = (int) show.getInt("seasons");
                    int episodes_show = (int) show.getInt("episodes");
                    // susb-sub level of show
                    JSONObject user = show.getJSONObject("user");
                    int remaining_show = (int) user.getInt("remaining");
                    // We create the object show
                    newShow = new Shows(id_show, title_show.toString(), description_show.toString(), seasons_show, episodes_show, remaining_show,getShowArt(id_show));
                    Log.v(TAG,"New show added!");
                    myShows.add(newShow);
                }
                // Creation of the profil instance
                ListeShow completeShows = new ListeShow(myShows);
                profil = new Profil(newUser, completeShows);

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
