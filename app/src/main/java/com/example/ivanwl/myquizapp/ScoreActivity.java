package com.example.ivanwl.myquizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreActivity extends AppCompatActivity {

    ListView scoreBoard;
    TextView textView_localname;
    TextView textView_localscore;
    Button button_play;
    Button button_delete;
    String name;
    int score;
    String uid;
    DatabaseReference fbRef;
    ArrayList<Player> scores;
    PlayerAdapter pa;

    public void displayList()
    {
        pa = new PlayerAdapter(this,R.layout.listview_adapter,scores);
        scoreBoard.setAdapter(pa);
    }

    public void addPlayer()
    {
        HashMap<String, Object> pair = new HashMap<>();
        pair.put("Name",name);
        pair.put("Score", this.score);
        DatabaseReference newRef = fbRef.push();
        uid = newRef.getKey();
        newRef.setValue(pair);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        name = getIntent().getExtras().getString("com.example.ivanwl.myquizapp.name");
        score = getIntent().getExtras().getInt("com.example.ivanwl.myquizapp.score");
        scoreBoard = (ListView) findViewById(R.id.listView_scoreboard);
        button_play = (Button) findViewById(R.id.button_play);
        button_delete = (Button) findViewById(R.id.button_delete);

        scores = new ArrayList<>();
        fbRef = FirebaseDatabase.getInstance().getReference("Players");
        Query q = fbRef.orderByChild("Score");

        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGame = new Intent(getApplicationContext(), GameActivity.class);
                goToGame.putExtra("com.example.ivanwl.myquizapp.name", name);
                startActivity(goToGame);
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player p1 = scores.get(scores.size()-1);
                if(p1.getName().equals(name + " (You)") && p1.getScore().equals("" + score)) {
                    fbRef.child(uid).removeValue();
                    scores.remove(scores.size() - 1);
                    pa.notifyDataSetChanged();
                }
            }
        });
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot uid : dataSnapshot.getChildren()) {
                    scores.add(new Player((String) uid.child("Name").getValue(),uid.child("Score").getValue().toString()));
                }
                scores.add(new Player(name + " (You)","" + score));
                displayList();
                addPlayer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
