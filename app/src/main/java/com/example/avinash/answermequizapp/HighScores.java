package com.example.avinash.answermequizapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScores extends ListActivity {
    DBHelper dbHelper;
    ArrayList<Player> playerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        dbHelper = new DBHelper(this);

        playerList = new ArrayList<>();
        playerList= getAllPlayers();

        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return Double.compare(o2.getTopScore(), o1.getTopScore());
            }
        });

        ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, playerList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);

                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(playerList.get(position).getName());
                text2.setText("Top Score: "+playerList.get(position).getTopScore());
                return view;
            }
        };

        setListAdapter(adapter);
    }

    public ArrayList<Player> getAllPlayers()
    {
        ArrayList<Player> playerList = new ArrayList<>();
        Cursor data = dbHelper.getAllPlayers();
        String s = String.valueOf(data.getCount());
        Log.d("Count", s);
        if(data.getCount() <= 0)
        {
            return null;
        }
        else
        {
            try {
                Player aPlayer = null;
                while(data.moveToNext())
                {
                    aPlayer = new Player(data.getString(0), data.getInt(1), data.getInt(2));
                    Log.d("Player Details", data.getString(0));
                    playerList.add(aPlayer);
                }
                return playerList;
            }
            catch(Exception e)
            {
                return null;
            }
        }
    }


}
