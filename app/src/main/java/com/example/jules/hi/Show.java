package com.example.jules.hi;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Jules on 12/10/2015.
 */
public class Show {

    Bitmap imgShow;
    int id;
    String title;
    String description;
    int seasons;
    int episodes;
    int remaining;

    public Show(int id_show, String title, String description, int seasons_show, int episodes_show, int remaining_show, Bitmap imgShow) {
        this.imgShow=imgShow;
        this.id = id_show;
        this.title = title;
        this.description = description;
        this.seasons = seasons_show;
        this.episodes = episodes_show;
        this.remaining = remaining_show;
    }

    public void setPictureShow(Bitmap picture) {
        this.imgShow = picture;
    }

    public int getShowId() {
        return (this.id);
    }

}
