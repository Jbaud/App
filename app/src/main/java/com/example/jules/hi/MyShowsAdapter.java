package com.example.jules.hi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.jules.hi.RoundedImageView.getCroppedBitmap;

/**
 * Created by Jules on 07/12/2015.
 */
public class MyShowsAdapter extends ArrayAdapter<String> {

    private final Activity context;


    public MyShowsAdapter(Activity context) {
        super(context, R.layout.list);
        this.context = context;
    }

    @Override
    public int getCount() {
        return CreateConnection.profil.getShows().size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list, null, true);
        TextView content = (TextView) rowView.findViewById(R.id.text);
        TextView pseudo = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.photo);
        pseudo.setText(CreateConnection.profil.shows.getShow().get(position).title);
        content.setText("Your progression for this show: "+CreateConnection.profil.shows.getShow().get(position).remaining +"/" +CreateConnection.profil.shows.getShow().get(position).episodes);
        imageView.setImageBitmap(getCroppedBitmap(CreateConnection.profil.shows.getShow().get(position).imgShow, 80));
        Log.v("MyShowAdapter", "sublist has been created");
        return rowView;
    }
}