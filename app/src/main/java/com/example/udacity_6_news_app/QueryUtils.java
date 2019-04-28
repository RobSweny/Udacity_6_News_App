package com.example.udacity_6_news_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() { }

    // Methods
    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String strUrl) {
        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;

        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;
        try {
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setReadTimeout(10000 /* milliseconds */);
            httpUrlConnection.setConnectTimeout(15000 /* milliseconds */);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            if (httpUrlConnection.getResponseCode() == 200) {
                inputStream = httpUrlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpUrlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (httpUrlConnection != null)
                httpUrlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.length() == 0)
            return null;

        ArrayList<News> newsList = new ArrayList<>();

        try {
            String thumbnail = "";
            String authorName = "";

            JSONObject baseResponse = new JSONObject(jsonResponse);
            JSONObject responseObj = baseResponse.getJSONObject("response");
            JSONArray resultsArray = responseObj.getJSONArray("results");

            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); ++i) {
                    JSONObject obj = resultsArray.getJSONObject(i);

                    //Extract the JSONArray with the key "tag"
                    JSONArray tagsArray = obj.getJSONArray("tags");

                    if (tagsArray.length() == 0) {
                        authorName = null;
                    } else {
                        for (int j = 0; j < tagsArray.length(); j++) {
                            JSONObject contributorTag = tagsArray.getJSONObject(j);
                            authorName = contributorTag.getString("webTitle");
                        }
                    }

                    if (obj.has("fields")) {
                        JSONObject currentFieldsObject = obj.getJSONObject("fields");
                        if (currentFieldsObject.has("thumbnail")) {
                            thumbnail = currentFieldsObject.getString("thumbnail");
                        } else {
                            thumbnail = null;
                        }
                    } else {
                        thumbnail = null;
                    }

                    News newsObject = new News(obj.getString("webTitle"), obj.getString("sectionName"), obj.getString("webPublicationDate"), obj.getString("webUrl"), thumbnail, authorName != null ? authorName : "Not available");
                    newsList.add(newsObject);
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return newsList;
    }

}
