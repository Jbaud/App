package com.example.jules.hi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jules on 13/10/2015
 */

// Cette classe stocke tous les séries suivies par un utilisateur

public class ListeShow {

    ArrayList<Shows> myShows = new ArrayList<Shows>();

    public ListeShow(ArrayList<Shows> myShows) {
        this.myShows = myShows;
    }

    // ajoute un show a la liste actuelle
    public void addShow(Shows myShows){

        this.addShow(myShows);

    }

    public void removeShow(Shows myShows){

        this.removeShow(myShows);

    }
    public ArrayList<Shows> getShow(){

        return  this.myShows;
    }
    public int size(){
        return this.myShows.size();
    }

}
