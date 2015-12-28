package com.example.jules.hi;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jules on 03/12/2015.
 */
public class Profil implements Serializable {

    public ArrayList<Show> shows;
    public User user;
    public Episode next_episode;

    //constructeur
    public Profil(User newUser, ArrayList<Show> shows, Episode next_episode) {
        this.shows = shows;
        this.user = newUser;
        this.next_episode = next_episode;
    }
}
