package com.example.udacity_6_news_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    // Declaring views
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Initializing
        Button dontShowButton = findViewById(R.id.dontShowButton);

        settings = getSharedPreferences("prefs", 0);
        boolean skipSplash = settings.getBoolean("skipSplash", false);

        // Check if user has selected skip splash
        if (!skipSplash) {
            // Timer before next activity
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }, 3000);
        } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra("flag", "Splashscreen");
            startActivity(intent);
        }

        // If the user clicks this button, the splash screen won't be shown again
        dontShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SplashScreen.this, "This screen won't be shown again", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("skipSplash", true);
                editor.apply();
            }
        });
    }
}
