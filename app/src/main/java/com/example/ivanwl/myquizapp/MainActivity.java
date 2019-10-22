package com.example.ivanwl.myquizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_name = (EditText) findViewById(R.id.editText_name);
        Button button_name = (Button) findViewById(R.id.button_name);

        button_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGame = new Intent(getApplicationContext(), GameActivity.class);
                String name = editText_name.getText().toString();
                //Log.i("println",name);
                if(name.equals("")) {
                    goToGame.putExtra("com.example.ivanwl.myquizapp.name", "Guest #" + ((int) (Math.random() * 9999) + 1));
                }
                else {
                    goToGame.putExtra("com.example.ivanwl.myquizapp.name", name);
                }
                startActivity(goToGame);
            }
        });
    }
}
