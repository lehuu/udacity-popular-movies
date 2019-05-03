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
    private static String API_KEY = BuildConfig.ApiKey;
    private static String API_PARAM = "api_key";
    private static String BASE_URL = "https://api.themoviedb.org/3";


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
