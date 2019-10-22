package com.example.ivanwl.myquizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    TextView textView_score;
    TextView textView_quizdisplay;
    TextView textView_description;
    TextView textView_timer;
    TextView textView_emoji;
    Button button_true;
    Button button_false;
    Button button_quit;
    String name;
    int score = 0;
    String question;
    String answer;
    CountDownTimer timer;
    DatabaseReference fbRef;
    //fbRef = FirebaseDatabase.getInstance().getReference();
    //fbRef.child("Name").child(name).setValue(score);

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    void newQuestion()
    {
        button_true.setText("O (TRUE)");
        button_false.setText("X (FALSE)");
        textView_description.setText("");
        textView_score.setText("Score = " + score);
        textView_emoji.setText("");
        timer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView_timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textView_timer.setText("Times up");
                if(score > 0)
                    --score;
                //textView_emoji.setText(String.join("",Collections.nCopies(3,getEmojiByUnicode(0x1F602))));
                results();
            }
        };
        //get db qt
        fbRef = FirebaseDatabase.getInstance().getReference("Problems/" + ((int) (Math.random() * 871) + 0));
        fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                question = (String) dataSnapshot.child("Question").getValue();
                answer = (String) dataSnapshot.child("Answer").getValue();
                textView_quizdisplay.setText(question);
                timer.start();
                button_true.setEnabled(true);
                button_false.setEnabled(true);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }
        });
    }

    void results()
    {
        textView_score.setText("Score = " + score);
        textView_description.setText((answer.charAt(0) + " - " + answer.substring(2)));
        button_true.setEnabled(false);
        button_false.setEnabled(false);
        //Log.i("println","results timeout");
        CountDownTimer waitTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                button_true.setText("" + millisUntilFinished / 1000);
                button_false.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                newQuestion();
            }
        };
        waitTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textView_score = (TextView) findViewById(R.id.textView_score);
        textView_quizdisplay = (TextView) findViewById(R.id.textView_quizdisplay);
        textView_description = (TextView) findViewById(R.id.textView_description);
        textView_timer = (TextView) findViewById(R.id.textView_timer);
        textView_emoji = (TextView) findViewById(R.id.textView_emoji);
        button_true = (Button) findViewById(R.id.button_true);
        button_false = (Button) findViewById(R.id.button_false);
        button_quit = (Button) findViewById(R.id.button_quit);

        name = getIntent().getExtras().getString("com.example.ivanwl.myquizapp.name");
        textView_score.setText("Score = " + score);
        //Log.i("println","question lookup");
        newQuestion();

        button_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                if(answer.charAt(0) == 'O')
                {
                    //textView_emoji.setText(getEmojiByUnicode(0x1F44C));
                    ++score;
                }
                else if(score > 0) {
                    //textView_emoji.setText(String.join("", Collections.nCopies(3, getEmojiByUnicode(0x1F602))));
                    --score;
                }
                else {
                    //textView_emoji.setText(String.join("",Collections.nCopies(3,getEmojiByUnicode(0x1F602))));
                }
                results();
            }
        });

        button_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                if(answer.charAt(0) == 'X') {
                    //textView_emoji.setText(getEmojiByUnicode(0x1F44C));
                    ++score;
                }
                else if(score > 0) {
                    //textView_emoji.setText(String.join("", Collections.nCopies(3, getEmojiByUnicode(0x1F602))));
                    --score;
                }
                else {
                    //textView_emoji.setText(String.join("",Collections.nCopies(3,getEmojiByUnicode(0x1F602))));
                }
                results();
            }
        });

        button_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                Intent goToScoreBoard = new Intent(getApplicationContext(), ScoreActivity.class);
                Bundle extras = new Bundle();
                extras.putString("com.example.ivanwl.myquizapp.name",name);
                extras.putInt("com.example.ivanwl.myquizapp.score",score);
                goToScoreBoard.putExtras(extras);
                startActivity(goToScoreBoard);
            }
        });

    }
}
