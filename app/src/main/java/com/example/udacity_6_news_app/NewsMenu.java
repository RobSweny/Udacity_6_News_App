package com.example.udacity_6_news_app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NewsMenu extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    private static final String GUARDIAN_API_KEY = "2734327e-9afb-415e-ae26-e5d61bbda952";
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    private NewsAdapter newsAdapter;
    private String interest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_menu);

        // Initializing
        ListView list_view = findViewById(R.id.list_view);

        newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        list_view.setAdapter(newsAdapter);

        // Check if internet is working
        if (isInternetWorking()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // No active internet, send user to NoInternet Activity
            Intent i = new Intent(NewsMenu.this, NoInternet.class);
            startActivity(i);
        }
    }

    public boolean isInternetWorking() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String no_articles = sharedPrefs.getString("max articles", "40");
        String order_by = sharedPrefs.getString("order-by", "relevance");

        interest = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Interest", "Movies");

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", interest);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", order_by);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("page-size", no_articles);
        uriBuilder.appendQueryParameter("api-key", GUARDIAN_API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        newsAdapter.clear();

        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }
    }

    // Clear newsAdapter on reset
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu, menu);
        return true;
    }

    // If the user selects settings, create intent to open settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if internet is working
        if (!isInternetWorking()) {
            // No active internet, send user to NoInternet Activity
            Intent i = new Intent(NewsMenu.this, NoInternet.class);
            startActivity(i);
        }
    }
}
