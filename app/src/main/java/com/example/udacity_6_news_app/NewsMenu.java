package com.example.udacity_6_news_app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class NewsMenu extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView list_view;
    private Context context;
    private NetworkInfo activeNetwork;

    ArrayList<HashMap<String, String>> pokemonList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_menu);

        pokemonList = new ArrayList<>();
        list_view = findViewById(R.id.list);


        if (isInternetWorking()) {
            new GetPokemon().execute();

        } else {
            // No active internet, send user to NoInternet Activity
            Intent i = new Intent(NewsMenu.this, NoInternet.class);
            startActivity(i);
        }


    }

    private class GetPokemon extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json";
            String jsonString = "";
            try {
                jsonString = sh.makeHttpRequest(createUrl(url));
            } catch (IOException e) {
                return null;
            }

            Log.e(TAG, "Response from url: " + jsonString);
            if (jsonString != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    JSONArray pokemons = jsonObj.getJSONArray("pokemon");

                    // looping through all Contacts
                    for (int i = 0; i < pokemons.length(); i++) {
                        JSONObject c = pokemons.getJSONObject(i);
                        String name = c.getString("name");
                        String id = c.getString("id");
                        String candy = c.getString("candy");


                        // tmp hash map for a single pokemon
                        HashMap<String, String> pokemon = new HashMap<>();

                        // add each child node to HashMap key => value
                        pokemon.put("name", name);
                        pokemon.put("id", id);
                        pokemon.put("candy", candy);

                        // adding a pokemon to our pokemon list
                        pokemonList.add(pokemon);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }
            return url;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(NewsMenu.this, pokemonList,
                    R.layout.custom_news_item, new String[]{"name", "id", "candy"},
                    new int[]{R.id.name, R.id.id, R.id.candy});
            list_view.setAdapter(adapter);
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
