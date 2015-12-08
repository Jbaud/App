package com.example.jules.hi;

/**
 * Created by Jules on 12/10/2015.
 */
public class Episode {

        int id;
        String title;
        String code;
        int id_show;
        String show_name;
        String description ;
        String date;
        boolean seen;
        boolean downloaded;


    public Episode(int id, String title,String code,int id_show, String show_name, String description, String date, boolean seen, boolean downloaded) {
        this.id = id;
        this.title = title;
        this.code= code;
        this.id_show = id_show;
        this.show_name = show_name;
        this.description = description;
        this.date = date;
        this.seen = seen;
        this.downloaded =downloaded;

    }
}
