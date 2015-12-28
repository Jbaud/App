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

// This class handles all the GET requests to the BetaSeries API.

public class CreateConnection {

    //SINGLETON accessible de partout
    public static Profil profil = null;

    String TAG = "CreateConnection"; // looger Tag
    String key = "e60e4c409cca"; // Key permits to id an dev to the betaserie api
    int user_id =386608; //the id user of Jules' account
    String USER_AGENT = "Mozilla/5.0"; // for json request

    public CreateConnection() {
    }

    /*Profil: user = shows
        ArrayList<Show> shows -> Liste contenant des informations sur les shows
        User user             -> Info sur le compte (nb series,photo etc...)
    Pour mettre a jour ces informations : getProfile
    Pour obtenir l'image de chaque série : getShowPicture */

    //getProfile : méthode la + importante, crée un objet profil utilisé pour tout
    public void getProfile() {

        StringBuffer response; //buffer pour le JSON reçu suite au get
        User user;
        ArrayList<Show> shows = new ArrayList<>();
        Episode next_episode;

        // consultation de l'API
        try {
            response = getJson("https://api.betaseries.com/members/infos");
            JSONObject json;

            //parsing de la reponse json
            try {
                //json tokenizer
                json = (JSONObject) new JSONTokener(String.valueOf(response)).nextValue();

                //---- USER -----
                //member
                JSONObject member = json.getJSONObject("member");
                String login = (String) member.get("login");

                //member.stats
                JSONObject stats = member.getJSONObject("stats");
                int shows_nb = (int) stats.get("shows");
                int episodes_nb = (int) stats.get("episodes");
                int progress = (int)((double)stats.get("progress") * 100);

                //creation de l'user
                user = new User(login, shows_nb, episodes_nb, progress, getUserPicture(user_id));

                //member.shows
                JSONArray json_array_show = member.getJSONArray("shows");

                //---- SHOWS -----
                Show newShow;
                for (int i = 0; i < json_array_show.length(); i++) {
                    //member.shows[i]
                    JSONObject show = json_array_show.getJSONObject(i);
                    int show_id =show.getInt("id");
                    String title_show =  show.getString("title");
                    String description_show = show.getString("description");
                    int seasons_show = show.getInt("seasons");
                    int episodes_show =  show.getInt("episodes");

                    //member.shows[i].user
                    JSONObject json_user = show.getJSONObject("user");
                    int remaining_show = json_user.getInt("remaining");

                    //création du show sans l'image et on l'ajoute à la liste
                    newShow = new Show(show_id, title_show, description_show, seasons_show, episodes_show, remaining_show, null);
                    shows.add(newShow);
                    Log.v(TAG,"New show added!");
                }

                //---- NEXT_EPISODE -----
                next_episode = getNextEpisode();

                //---- PROFIL -----
                //creation du profil = user + shows (images des shows non présentes) + next_episode
                profil = new Profil(user, shows, next_episode);
                Log.i(TAG,"The profil has been created and can now be used in from the CreateConnection class");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.v(TAG, "Error when parsing JSON");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "Error in the received JSON file");
        }
    }

    //Ajoute l'image du show à chaque show
    public void updateShowsPicture() {
        int id;
        for (int i = 0; i < profil.shows.size(); i++) {
            id = profil.shows.get(i).getShowId();
            try {
                profil.shows.get(i).setPictureShow(getShowPicture(id));
            } catch (IOException e) {
                e.printStackTrace();
                Log.v(TAG, "Error when adding picture to show with id : "+id);
            }
        }
    }

    // Requete GET vers l'api betaseries, récupération du JSON
    private StringBuffer getJson(String path) throws IOException{

        //forge de la requete
        String url = path+"?id="+Integer.toString(user_id)+"&v=2.4&key="+key;
        URL obj = new URL(url);
        HttpURLConnection http_con = (HttpURLConnection) obj.openConnection();
        http_con.setRequestMethod("GET");
        http_con.setRequestProperty("User-Agent", USER_AGENT);

        //verification du code retour http
        int responseCode = http_con.getResponseCode();
        if(responseCode != 200) {
            Log.v(TAG, "Connection error !");
            //TODO: toast erreur de connexion
        }

        //lecture de la réponse
        BufferedReader in = new BufferedReader(
                new InputStreamReader(http_con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //retour : stringbuffer contentant tout le JSON reçu
        return(response);
    }

    //Recupère l'image d'un utilisateur
    private Bitmap getUserPicture(int user_id) throws IOException {

        Bitmap image = null;
        try {
            URL url = new URL("https://api.betaseries.com/pictures/members?id="+user_id+"&v=2.4&key=e60e4c409cca");
            InputStream is = (InputStream) url.getContent();
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            Log.v(TAG, "Error when downloading image");
        }
        return image;
    }

    //Récupère l'image d'un show grace à l'identifiant du show
    private Bitmap getShowPicture(int show_id) throws IOException {

        // not the usual method since the url is different for this
        InputStream in = null;
        try
        {
            URL url = new URL("https://api.betaseries.com/pictures/shows?id="+show_id+"&v=2.4&key=e60e4c409cca");
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
            Log.v(TAG, "error when downloading show art(type2");
        }
        return BitmapFactory.decodeStream(in);
    }

    // Récupère les prochains épisodes pour un utilisateur
    public Episode getNextEpisode() throws IOException {

        StringBuffer response;
        response = getJson("https://api.betaseries.com/planning/member");
        JSONObject json;

        Episode nextEpisode;
        try {
            json = (JSONObject) new JSONTokener(String.valueOf(response)).nextValue();
            JSONArray ArrayShow = json.getJSONArray("episodes");

            //episode[0] pour le prochain episode
            JSONObject episode = ArrayShow.getJSONObject(0);
            String title = episode.getString("title");
            String description = episode.getString("description");
            String date = episode.getString("date");
            String episode_nb = episode.getString("code");

            //episode.show
            JSONObject show = episode.getJSONObject("show");
            String show_name = show.getString("title");

            nextEpisode = new Episode(show_name, episode_nb, title, date, description);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return nextEpisode;
    }
}
