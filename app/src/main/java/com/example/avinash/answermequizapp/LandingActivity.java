package com.example.avinash.answermequizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {

    DBHelper dbHelper;
    boolean validName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        dbHelper = new DBHelper(this);
    }

    /**
     * Method start the game with the entered player
     * @param view
     */
    public void onStartClicked(View view) {
        Intent intent = new Intent(this, CategoryActivity.class);
        EditText playerNameET = (EditText) findViewById(R.id.playerName) ;

        //Check if player name is valid
        if(playerNameET.getText().toString().trim().length()== 0)
        {
            toastMessage("Enter a valid name");
            validName = false;
        }
        else
        {
            validName = true;
        }

        //Proceed if the player name is valid
        if(validName)
        {
            String playerName = playerNameET.getText().toString();
            if(getPlayer(playerName) != null)
            {
                Player aPlayer = getPlayer(playerName);
                toastMessage("Welcome Back, "+playerName);
                intent.putExtra("CurrentPlayer", aPlayer);
                startActivity(intent);
            }
            else
            {
                Player aPlayer = new Player(playerName, 0,0);
                if(addData(aPlayer))
                {
                    toastMessage("Hi, "+playerName);
                    intent.putExtra("CurrentPlayer", aPlayer);
                    startActivity(intent);
                }
            }

        }

    }

    /**
     * Add data to database
     * @param aPlayer
     * @return true/fasle
     */
    public boolean addData(Player aPlayer)
    {
        boolean insertData = dbHelper.addData(aPlayer);
        if(insertData)
        {
            return true;
        }else
        {
            toastMessage("Something went wrong!");
            return false;
        }
    }

    /**
     * Get player details
     * @param playerName
     * @return Player
     */
    public Player getPlayer(String playerName)
    {
        Cursor data = dbHelper.getPlayerData(playerName);
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
                   aPlayer = new Player(playerName, data.getInt(0), data.getInt(1));
                }
                return aPlayer;
            }catch(Exception e)
            {
                return null;
            }
        }
    }

    /**
     * Generate a toast message
     * @param message
     */
    public void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showHighscores(View view)
    {
        Intent intent = new Intent(LandingActivity.this, HighScores.class);
        startActivity(intent);
    }
}
