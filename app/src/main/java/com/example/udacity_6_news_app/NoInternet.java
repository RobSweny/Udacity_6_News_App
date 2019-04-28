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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);

        // Declarations
        Button internet_button = findViewById(R.id.internet_button);
        Button interests_button = findViewById(R.id.interests_button);

        interests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Bring user back
                Intent i = new Intent(NoInternet.this, NewsMenu.class);
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
            Toast.makeText(NoInternet.this, "Successfully connected to internet", Toast.LENGTH_SHORT).show();

            // Bring user back
            Intent i = new Intent(NoInternet.this, NewsMenu.class);
            startActivity(i);
        }
    } // End onResume

    public boolean isInternetWorking() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    } // End isInternetWorking
}
