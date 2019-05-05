package com.example.popularmovies.Utils;
import android.net.Uri;

import com.example.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    public static final String POPULAR_URL = "movie/popular";
    public static final String TOP_RATED_URL = "movie/top_rated";
    public static final String POSTER_URL = "https://image.tmdb.org/t/p/w185";

    private static String API_KEY = BuildConfig.ApiKey;
    private static String API_PARAM = "api_key";
    private static String BASE_URL = "https://api.themoviedb.org/3";


    /**
     * Create a URL object using the BASE_URL, api key and the passed path
     * @param path the query path that should be added to the base URL
     * @return a URL object built on the path string
     */
    public static URL buildURL(String path){
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendEncodedPath(path).appendQueryParameter(API_PARAM, API_KEY).build();
        URL result = null;

        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Create a HTTP connection to the passed url to get a json response back
     * @param url to which the connection should be established
     * @return the response from the server as a string
     */
    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        Scanner scanner = null;
        String response = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST ?
                    urlConnection.getInputStream() : urlConnection.getErrorStream();

            scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                response = scanner.next();
            }
        } catch (MalformedURLException e) {
            // Replace this with your exception handling
            e.printStackTrace();
        } catch (IOException e) {
            // Replace this with your exception handling
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(scanner != null)
                scanner.close();
        }

        return response;
    }

}
