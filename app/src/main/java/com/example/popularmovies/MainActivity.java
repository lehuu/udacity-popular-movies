package com.example.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.popularmovies.Utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //new FetchMovieTask().execute(NetworkUtils.POPULAR_URL);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildURL(strings[0]));
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
        }
    }
}
