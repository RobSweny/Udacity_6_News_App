package com.example.udacity_6_news_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaring
    private TextView welcome_textview;
    private EditText interests_edittext;
    private Button go_button, add_button;

    // Variables
    private int interest_counter = 0;
    private String interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing
        welcome_textview = findViewById(R.id.welcome_textview);
        interests_edittext = findViewById(R.id.interests_edittext);
        go_button = findViewById(R.id.go_button);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take the value from the EditText and give to the string "interest"
                interest = String.valueOf(interests_edittext.getText()).trim();

                // Check if the user entered an empty value
                if (interest.equals("")) {
                    Toast.makeText(MainActivity.this, "Your entered interest was empty! Let's try again.", Toast.LENGTH_SHORT).show();
                } else {

                    // Proceed to next activity
                }
            } // End onClick
        }); // End go_button onClick

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to next activity

            } // End onClick
        }); // End go_button onClick


    } // End onCreate
} // End MainActivity
