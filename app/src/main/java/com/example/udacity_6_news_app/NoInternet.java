package com.example.udacity_6_news_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoInternet extends AppCompatActivity {

    // Declarations
    private Button internet_button, interests_button;
    private SharedPreferences settings;
    private boolean skipInterests;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);

        interests_button = findViewById(R.id.interests_button);
        internet_button = findViewById(R.id.internet_button);

        interests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // We need to edit the preferences to allow the user to go back to interests
                settings = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                skipInterests = settings.getBoolean("skipInterests", false);
                editor.putBoolean("skipInterests", false);
                editor.apply();

                // Bring user back
                Intent i = new Intent(NoInternet.this, MainActivity.class);
                startActivity(i);
            }
        });

        internet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    } // End onCreate

    @Override
    protected void onResume() {
        super.onResume();

        if (isInternetWorking()) {
            Toast.makeText(NoInternet.this,"Successfully connected to internet", Toast.LENGTH_SHORT).show();

            // Bring user back
            Intent i = new Intent(NoInternet.this, NewsMenu.class);
            startActivity(i);
        }
    }

    public boolean isInternetWorking() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
