package com.example.jules.hi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jules on 13/10/2015.
 */
// Cette classe stocke les épisodes d'un utilisateur

public class ListEpisode {


    List<Episode> myepisodes = new ArrayList<Episode>();

    public ListEpisode(List myepisodes){

            this.myepisodes=myepisodes;
    }

    public void addEpisode(Episode episode){

            this.addEpisode(episode);
    }

    public void removeEpisode(Episode episode){

            this.removeEpisode(episode);
    }
    public List<Episode> getMyepisodes(){

        return this.myepisodes;
    }
}
