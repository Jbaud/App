package com.example.jules.hi;

/**
 * Created by Jules on 12/12/2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.jules.hi.RoundedImageView.getCroppedBitmap;

public class ShowsAdapterRV extends RecyclerView.Adapter<ShowsAdapterRV.ViewHolder> {
    private Profil profil;

    public ShowsAdapterRV(Profil profil) {
        this.profil = profil;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShowsAdapterRV.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shows, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText("Your progression for this show: "+profil.shows.getShow().get(position).remaining +"/" +profil.shows.getShow().get(position).episodes);
        viewHolder.imgViewIcon.setImageBitmap(getCroppedBitmap(profil.shows.getShow().get(position).imgShow, 80));


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.title);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.content);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.showpicture);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return CreateConnection.profil.getShows().size();
    }
}