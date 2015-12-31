package com.example.jules.hi;

import android.graphics.Bitmap;

/**
 * Created by Jules on 12/10/2015.
 */
public class User {
        String username;
        int shows_nb;
        int episodes_vus;
        int minutes;
        Bitmap picture;

        public User(String username,int  number_shows, int episodes_to_watch, int minutes, Bitmap picture)
       {
                this.username=username;
                this.shows_nb=number_shows;
                this.episodes_vus = episodes_to_watch;
                this.minutes = minutes;
                this.picture = picture;
        }

    public String getUsername() {
        return username;
    }

    public int getNumber_shows() {
        return shows_nb;
    }

    public int getEpisodes_vus() {
        return episodes_vus;
    }

    public int getHeures() {
        return minutes;
    }

    public Bitmap getPicture() {
        return picture;
    }
}
