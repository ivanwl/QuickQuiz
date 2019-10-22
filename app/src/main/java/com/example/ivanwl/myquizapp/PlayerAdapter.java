package com.example.ivanwl.myquizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {

    private Context mContext;
    int mResource;

    public PlayerAdapter(Context context, int resource, ArrayList<Player> scores)
    {
        super(context,resource,scores);
        mContext = context;
        mResource = resource;
    }

    //@androidx.annotation.NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(getCount() - 1 - position).getName();
        String score = getItem(getCount() - 1 - position).getScore();

        //Player p = new Player(name,score);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView textView_highname = (TextView) convertView.findViewById(R.id.textView_highname);
        TextView textView_highscore = (TextView) convertView.findViewById(R.id.textView_highscore);

        textView_highname.setText(name);
        textView_highscore.setText(score);
        return convertView;
    }
}
