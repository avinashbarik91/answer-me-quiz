package com.example.avinash.answermequizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    DBHelper dbHelper;
    MediaPlayer mp;
    MediaPlayer selectAnswer;
    MediaPlayer correctAnswer;
    MediaPlayer wrongAnswer;

    private static final int SCORE_INCREASE = 10 ;
    private static final int MAX_QUESTIONS = 5;
    private static final int MAX_TIME = 16;

    TextView questionTV, playerScoreTV, questionNumTV, timerTV;
    Button button, button2, button3, button4;

    ArrayList<QuestionModel> questionSet;
    ArrayList<String> answerSet;

    int currentScore;
    int currentQuestionNum;
    int currentTime;

    Drawable btnBck;
    Timer t;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = new DBHelper(this);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.musicloop);
        selectAnswer = MediaPlayer.create(getApplicationContext(), R.raw.picked);
        correctAnswer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        wrongAnswer = MediaPlayer.create(getApplicationContext(), R.raw.wrong);

        playGameMusic();

        questionTV = (TextView) findViewById(R.id.questionTV);
        questionNumTV = (TextView) findViewById(R.id.questionNumTV);
        playerScoreTV = (TextView) findViewById(R.id.scoreTV);
        timerTV = (TextView) findViewById(R.id.timerTV);
        timerTV.setText(String.valueOf(MAX_TIME));

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);

        btnBck = new ColorDrawable();
        btnBck = button.getBackground();

        currentScore = 0;
        currentQuestionNum = 0;
        currentTime = MAX_TIME;

        questionSet = new ArrayList<>();
        questionSet = getIntent().getParcelableArrayListExtra("QuestionSet");

        displayQuestion(currentQuestionNum);
    }

    private void startTimer()
    {
        currentTime = MAX_TIME;
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                currentTime -= 1;
                setText();
            }
        }, 0, 1000);
    }

    private void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(currentTime>0)
                {
                    if(currentTime<10)
                    {
                        timerTV.setTextColor(Color.RED);
                    }
                    else
                    {
                        timerTV.setTextColor(Color.WHITE);
                    }
                    timerTV.setText(String.valueOf(currentTime));
                }
                else
                {
                    t.cancel();
                    playWrongAns();
                    gameOver();
                }
            }
        });
    }

    private void playGameMusic()
    {
        mp.setLooping(true);
        mp.start();
    }

    private void stopGameMusic()
    {
        mp.stop();
    }

    private void playAnsPicked()
    {
        selectAnswer.start();
    }

    private void playCorrectAns()
    {
        correctAnswer.start();
    }

    private void playWrongAns()
    {
        wrongAnswer.start();
    }

    private void setGameValues()
    {
        startTimer();
        playerScoreTV.setText(String.valueOf(currentScore));
        questionNumTV.setText(String.valueOf(currentQuestionNum+1));
        button.setBackground(btnBck);
        button2.setBackground(btnBck);
        button3.setBackground(btnBck);
        button4.setBackground(btnBck);
    }


    private void displayQuestion(int currentQuestionNum)
    {
        setGameValues();
        randomizeAnswers(currentQuestionNum);
        questionTV.setText(questionSet.get(currentQuestionNum).getQuestion());
        button.setText(answerSet.get(0));
        button2.setText(answerSet.get(1));
        button3.setText(answerSet.get(2));
        button4.setText(answerSet.get(3));


    }

    private void randomizeAnswers(int currentQuestionNum)
    {
        String correctAnswer = questionSet.get(currentQuestionNum).getCorrectAnswer();
        answerSet = new ArrayList<>();
        answerSet.add(correctAnswer);
        for(int i=0; i<3; i++)
        {
            answerSet.add(questionSet.get(currentQuestionNum).getIncorrectAnswers()[i]);
        }

        Collections.shuffle(answerSet);
    }

    public void btnClicked(View view)
    {
        playAnsPicked();
        t.cancel();
        final Button btn = (Button) view;
        btn.setBackgroundColor(getResources().getColor(R.color.orangeCustom));
        if(checkAnswer(view))
        {
            onCorrect(btn);
        }
        else
        {
            onWrong(btn);
        }
    }

    private Button getCorrectButton(int currentQuestionNum)
    {
        String cAns= questionSet.get(currentQuestionNum).getCorrectAnswer();
        if(button.getText().toString().equals(cAns))
        {
            return button;
        }
        else if (button2.getText().toString().equals(cAns))
        {
            return button2;
        }
        else if (button3.getText().toString().equals(cAns))
        {
            return button3;
        }
        else
        {
            return button4;
        }
    }

    private void onCorrect(final Button btn)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playCorrectAns();
                btn.setBackgroundColor(getResources().getColor(R.color.green_custom));
                currentScore += SCORE_INCREASE*currentTime;
            }
        }, 1500);

        if(currentQuestionNum < MAX_QUESTIONS-1)
        {
            currentQuestionNum += 1;
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayQuestion(currentQuestionNum);
                }
            }, 3000);
        }
        else
        {
            Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameWon();
                }
            }, 3000);
        }
    }

    private void gameWon()
    {
        stopGameMusic();
        Player aPlayer = new Player();
        aPlayer = getIntent().getParcelableExtra("CurrentPlayer");
        aPlayer.setLastScore(currentScore);
        if(currentScore>aPlayer.getTopScore())
        {
            aPlayer.setTopScore(currentScore);
        }

        dbHelper.updateData(aPlayer);

        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("CurrentPlayer", aPlayer);
        intent.putExtra("Status", "Won");
        startActivity(intent);
    }

    private void gameOver()
    {
        try {
            stopGameMusic();
        }catch (Exception e)
        {}
        Player aPlayer = new Player();
        aPlayer = getIntent().getParcelableExtra("CurrentPlayer");
        aPlayer.setLastScore(currentScore);
        if(currentScore>aPlayer.getTopScore())
        {
            aPlayer.setTopScore(currentScore);
        }

        dbHelper.updateData(aPlayer);

        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("CurrentPlayer", aPlayer);
        intent.putExtra("Status", "Lose");
        startActivity(intent);
    }

    private void onWrong(final Button btn)
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playWrongAns();
                stopGameMusic();
                getCorrectButton(currentQuestionNum).setBackgroundColor(getResources().getColor(R.color.green_custom));
                btn.setBackgroundColor(getResources().getColor(R.color.red_custom));
            }
        }, 1500);

        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameOver();
            }
        }, 3000);
    }

    private boolean checkAnswer(View view)
    {
        Button btn = (Button) view;
        String answerSelected= btn.getText().toString();
        if(answerSelected.equals(questionSet.get(currentQuestionNum).getCorrectAnswer() ))
        {
            return true;
        }else
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(QuizActivity.this, "Action not allowed", Toast.LENGTH_SHORT).show();
    }
}
