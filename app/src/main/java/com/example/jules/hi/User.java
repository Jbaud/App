package com.example.jules.hi;

import android.graphics.Bitmap;

/**
 * Created by Jules on 12/10/2015.
 */
public class User {
        String username;
        int number_shows;
        int episodes_to_watch;
        int xp;
        double progress;
        Bitmap picture;

        public User(String username,int  number_shows, int episodes_to_watch,int xp,double progress,Bitmap picture)
       {
                this.username=username;
                this.number_shows=number_shows;
                this.episodes_to_watch = episodes_to_watch;
                this.xp=xp;
                this.progress = progress;
                this.picture = picture;
        }

    public String getUsername() {
        return username;
    }

    public int getNumber_shows() {
        return number_shows;
    }

    public int getEpisodes_to_watch() {
        return episodes_to_watch;
    }

    public int getXp() {
        return xp;
    }

    public double getProgress() {
        return progress;
    }

    public Bitmap getPicture() {
        return picture;
    }
    // probably the most stupid thing ever.
    public User GetUser(){
        return  this;
    }
}
