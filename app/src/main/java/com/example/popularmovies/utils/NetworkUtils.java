package com.example.popularmovies.utils;

import com.example.popularmovies.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    private final static String API_KEY = BuildConfig.ApiKey;
    private final static String API_PARAM = "api_key";
    private final static String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POSTER_URL = "https://image.tmdb.org/t/p/w342";
    public static final String BACKDROP_URL = "https://image.tmdb.org/t/p/w500";
    public static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/mqdefault.jpg";
    public static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        //Use custom date serializer for occasional empty date fields in JSON
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        @Override
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            try {
                                return dateFormat.parse(json.getAsString());
                            } catch (ParseException e) {
                                return null;
                            }
                        }
                    })
                    .create();

            OkHttpClient.Builder httpClient =  new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(API_PARAM, API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }

}
