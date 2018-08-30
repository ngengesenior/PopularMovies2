package com.example.ngenge.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngenge.popularmovies.adapters.ReviewsAdapter;
import com.example.ngenge.popularmovies.adapters.TrailersRecyclerAdapter;
import com.example.ngenge.popularmovies.models.Movie;
import com.example.ngenge.popularmovies.models.Review;
import com.example.ngenge.popularmovies.models.Trailer;
import com.example.ngenge.popularmovies.room.MovieViewModel;
import com.example.ngenge.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    private List<Trailer> trailers;
    private List<Review> reviews;

    private TrailersRecyclerAdapter trailersRecyclerAdapter;
    private ReviewsAdapter reviewsAdapter;
    private RecyclerView trailersList;
    private RecyclerView reviewsList;
    private MovieViewModel movieViewModel;

    DividerItemDecoration dividerItemDecoration;

    private int favorited = 0;



    TextView textViewVoteAverage;


    TextView releasedDate;


    ImageView moviePoster;


    TextView textViewOverview;

    TextView movieName;

    private int movieId;

    private ImageButton favoriteButton;
    private List<Movie> moviesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {

                moviesList = movies;
            }
        });
        final Movie movie =  getIntent().getParcelableExtra(Utils.movieKey);
        movieId = movie.getId();

        reviewsList = findViewById(R.id.reviewsList);
        reviewsList.addItemDecoration(dividerItemDecoration);
        trailersList = findViewById(R.id.trailersList);
        trailersList.addItemDecoration(dividerItemDecoration);
        //movieId = getIntent().getIntExtra(Utils.movieKey,-1);
        textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
        releasedDate = findViewById(R.id.textViewReleaseDate);
        movieName = findViewById(R.id.movieName);
        textViewOverview = findViewById(R.id.textViewOverview);
        moviePoster = findViewById(R.id.imageViewPoster);
        favoriteButton = findViewById(R.id.buttonFavorite);

        if(movieViewModel.getMovie(movieId) == movie)
        {

            favorited = 1;
        }

        else {

        }








        //Tried using butterknife but objects are returned as null
        //ButterKnife.bind(this);

        Picasso.get()
                .load(movie.getPoster_path())
        .into(moviePoster);
        textViewOverview.setText(movie.getOverview());
        releasedDate.setText(movie.getRelease_date());
        textViewVoteAverage.setText(String.format("%s%%", String.valueOf(movie.getVote_average() * 10)));
        movieName.setText(movie.getTitle());



        if(Utils.isNetworkConnected(this))
        {
            new TrailerTask().execute(movieId);
            new ReviewsTask().execute(movieId);
        }



        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMovieInFavorites(movie))
                {
                    movieViewModel.delete(movie);
                    Toast.makeText(DetailsActivity.this,"Fav",Toast.LENGTH_LONG)
                            .show();

                }
                else {
                    movieViewModel.insert(movie);
                    favoriteButton.setBackgroundColor(Color.RED);
                    Toast.makeText(DetailsActivity.this,"Non",Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }


    class TrailerTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... integers) {
            String key = String.valueOf(integers[0]);
            String url = Utils.movieUrl+key+"/videos?api_key="+Utils.API_KEY;
            return Utils.makeNetworkRequest(url);
        }

        @Override
        protected void onPostExecute(String s) {
            trailers = new ArrayList<>();

            super.onPostExecute(s);


            try{

                JSONObject object = new JSONObject(s);

                JSONArray array = object.getJSONArray("results");

                for(int i=0; i< array.length(); i++)
                {
                    JSONObject trailerItem = array.getJSONObject(i);
                    Trailer trailer = Utils.getTrailer(trailerItem);
                    trailers.add(trailer);



                }

                trailersRecyclerAdapter = new TrailersRecyclerAdapter(trailers);

                trailersList.setAdapter(trailersRecyclerAdapter);
                trailersRecyclerAdapter.notifyDataSetChanged();


            }

            catch (JSONException ex){
                ex.printStackTrace();
            }



        }
    }


    class ReviewsTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... integers) {
            String key = String.valueOf(integers[0]);
            String url = Utils.movieUrl+key+"/reviews?api_key="+Utils.API_KEY;
            return Utils.makeNetworkRequest(url);
        }


        @Override
        protected void onPostExecute(String str) {
            reviews = new ArrayList<>();
            super.onPostExecute(str);

            try{

                JSONObject object = new JSONObject(str);

                JSONArray array = object.getJSONArray("results");

                for(int i=0; i< array.length(); i++)
                {
                    JSONObject reviewItem = array.getJSONObject(i);
                    Review review = Utils.getReview(reviewItem);
                    reviews.add(review);



                }

                reviewsAdapter = new ReviewsAdapter(reviews);

                reviewsList.setAdapter(reviewsAdapter);
                reviewsAdapter.notifyDataSetChanged();


            }

            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }




    private boolean isMovieInFavorites(Movie movie)
    {
        return moviesList.contains(movie);

    }



}
