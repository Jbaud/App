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
        // One can add other elements as the xp or the profile picture
        public User(String username,int  number_shows, int episodes_to_watch,int xp,double progress,Bitmap picture)
       {
                this.username=username;
                this.number_shows=number_shows;
                this.episodes_to_watch = episodes_to_watch;
                this.xp=xp;
                this.progress = progress;
                this.picture = picture;
        }
}
