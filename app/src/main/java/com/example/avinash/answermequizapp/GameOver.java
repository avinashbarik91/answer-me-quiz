package com.example.avinash.answermequizapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameOver extends AppCompatActivity {

    TextView mainText, scoreText;
    ImageView bck, caption;
    Player aPlayer;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.gamewin);
        aPlayer = new Player();
        aPlayer = getIntent().getParcelableExtra("CurrentPlayer");

        mainText = (TextView) findViewById(R.id.mainText);
        scoreText = (TextView) findViewById(R.id.finalScoreTextTV);
        bck = (ImageView) findViewById(R.id.bckImageView);
        caption = (ImageView) findViewById(R.id.imageView5);

        if(getIntent().getStringExtra("Status").equals("Won"))
        {
            mp.start();
            mainText.setText("Congratulations \n You have won the game!");
            scoreText.setText("You Scored\n"+aPlayer.getLastScore()+"\n points");
            bck.setImageDrawable(getResources().getDrawable(R.drawable.bckwin));
            caption.setImageDrawable(getResources().getDrawable(R.drawable.youwin));
        }
        else
        {
            mainText.setText("Incorrect Answer! Game Over.");
            scoreText.setText("You Scored\n"+aPlayer.getLastScore()+"\n points");
        }
    }

    public void playAgainClicked(View view)
    {
        mp.stop();
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(GameOver.this, "Action not allowed", Toast.LENGTH_SHORT).show();
    }
}
