package com.example.jules.hi;

import java.io.Serializable;

/**
 * Created by Jules on 03/12/2015.
 */
public class Profil implements Serializable {

    public ListeShow   shows;
    public User    user;

    public Profil(User newUser, ListeShow newShow) {
        this.shows = newShow;
        this.user = newUser;
    }
    public ListeShow getShows(){

        return this.shows;
    }
    public User getUser(){
        return this.user;
    }

}
