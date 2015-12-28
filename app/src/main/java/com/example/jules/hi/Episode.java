package com.example.jules.hi;

/**
 * Created by Jules on 12/10/2015.
 */
public class Episode {

    String show_name;
    String episode_nb;
    String title;
    String date;
    String description ;

    public Episode(String show_name, String episode_nb, String title, String date, String description) {
        this.show_name = show_name;
        this.episode_nb= episode_nb;
        this.title = title;
        this.date = date;
        this.description = description;
    }
}
