package com.example.ngenge.popularmovies.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ngenge.popularmovies.BuildConfig;
import com.example.ngenge.popularmovies.models.Movie;
import com.example.ngenge.popularmovies.models.Review;
import com.example.ngenge.popularmovies.models.Trailer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static String  API_KEY = BuildConfig.API_KEY;
    public static String movieKey = "movie";
    public static String movieUrl = "https://api.themoviedb.org/3/movie/";
    public static String basePopularUrl = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
    public static String baseTopRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
    public static String baseImageUrl = "https://image.tmdb.org/t/p/w185";
    public static String vote_count_key = "vote_count";
    public static String id_key = "id";
    public static String vote_avg_key = "vote_average";
    public static String title_key = "title";

    public static String overview_key ="overview";
    public static String poster_path_key = "poster_path";

    public static String release_date_key ="release_date";
    public static String author_key = "author";
    public static String content_key = "content";



    public static Movie getMovieFromJsonObject(JSONObject jsonObject)
    {
        try {


            int id = jsonObject.getInt(id_key);
            String poster_path = jsonObject.getString(poster_path_key);
            String release_date = jsonObject.getString(release_date_key);
            double vote_average = jsonObject.getDouble(vote_avg_key);
            int vote_count = jsonObject.getInt(vote_count_key);
            String title = jsonObject.getString(title_key);

            String overview = jsonObject.getString(overview_key);
            return new Movie(id,title,release_date,vote_average,vote_count,baseImageUrl+poster_path,overview);

        }
        catch (JSONException ex)
        {
            System.out.println(ex.getMessage());

        }


        return null;
        }


        public static Review getReview(JSONObject object) throws JSONException {
            String author = object.getString(author_key);
            String content = object.getString(content_key);
            return new Review(author,content);

        }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }




        public static Trailer getTrailer(JSONObject object) throws JSONException{

        return new Trailer(object.getString("key"));
        }


    /**
     *
     * @param endPoint The endpoint of request
     * @return
     */


    public static String makeNetworkRequest(String endPoint)
        {

            StringBuilder results = new StringBuilder();
            HttpURLConnection con = null;


            try {
                URL url = new URL(endPoint);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                //Get output with buffered reader
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;

                while ((line=reader.readLine()) != null)
                {
                    results.append(line);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                if(con!=null)
                {
                    con.disconnect();
                }
            }

            return results.toString();
        }




}
