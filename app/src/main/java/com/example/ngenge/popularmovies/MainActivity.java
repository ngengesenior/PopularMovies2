package com.example.ngenge.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.ngenge.popularmovies.adapters.MoviesAdapter;
import com.example.ngenge.popularmovies.models.Movie;
import com.example.ngenge.popularmovies.room.MovieViewModel;
import com.example.ngenge.popularmovies.utils.GridDividerItemDecoration;
import com.example.ngenge.popularmovies.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MoviesAdapter.OnItemClickListener{
    private GridDividerItemDecoration portratDecoration;
    private GridDividerItemDecoration landscapeDecoration;


    private static MoviesAdapter adapter = null;
    private CoordinatorLayout coordinatorLayout;
    public static final String LIST_STATE_KEY= "List";
    Parcelable listState;


private static RecyclerView movieList;
private static List<Movie> movies;
private static Context context;
private static MoviesAdapter.OnItemClickListener listener;

private String url;
private String URL_KEY = "urlKey";
private String sortOrderKey = "SORT_KEY";
private MovieViewModel movieViewModel;
private RecyclerView.LayoutManager layoutManager;
private Observer<List<Movie>> favouriteMoviesObserver;
private int sort = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutManager = new GridLayoutManager(this,2);
        movieList = findViewById(R.id.movieList);
        movieList.setHasFixedSize(true);
        movieList.setLayoutManager(layoutManager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        setSupportActionBar(toolbar);
        coordinatorLayout = findViewById(R.id.constraintLayout);
       adapter = new MoviesAdapter(new ArrayList<Movie>(),this,this);
       movieList.setAdapter(adapter);
        if(savedInstanceState != null)
        {
            if(savedInstanceState.getInt(sortOrderKey) == 1)
            {
                loadPopularMovies();
            }

            else if(savedInstanceState.getInt(sortOrderKey) == 2)
            {
                loadTopRatedMovies();
            }

            else if(savedInstanceState.getInt(sortOrderKey)== 3)
            {
                loadFavoriteMovies();
            }

        }

        else {
            loadFavoriteMovies();
            getSupportActionBar().setTitle("Favourite Movies");
        }
        context = MainActivity.this;
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        landscapeDecoration = new GridDividerItemDecoration(getResources().getDrawable(R.drawable.divider_horizontal),getResources().getDrawable(R.drawable.divider),3);
        portratDecoration = new GridDividerItemDecoration(getResources().getDrawable(R.drawable.divider_horizontal),getResources().getDrawable(R.drawable.divider),2);


        if(!isNetworkConnected())
        {
            showNoInternet();
            return;
        }








    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {


            url = Utils.basePopularUrl;
            sort = 1;
            loadPopularMovies();

            return true;
        }

       else if(id == R.id.action_top_rated)
        {
            sort = 2;
            url = Utils.baseTopRatedUrl;

            loadTopRatedMovies();


            return true;

        }

        else if(id == R.id.action_favorites)
        {


            sort = 3;
            loadFavoriteMovies();


            getSupportActionBar().setTitle(R.string.favorite);

        }

        return super.onOptionsItemSelected(item);
    }

    private void loadPopularMovies() {
        if(isNetworkConnected())
        {
            getSupportActionBar().setTitle(R.string.popular_movies);
            new MyTask().execute(Utils.basePopularUrl);
            adapter.notifyDataSetChanged();

        }

        else {



            showNoInternet();

        }
    }

    private void loadTopRatedMovies() {
        getSupportActionBar().setTitle(R.string.top_rated);
        if(isNetworkConnected())
        {
            new MyTask().execute(Utils.baseTopRatedUrl);


        }
        else {
            showNoInternet();
        }
    }

    private void loadFavoriteMovies() {
        favouriteMoviesObserver = new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {

                adapter.setMovies(movies);
            }
        };

        movieViewModel.getAllMovies().observe(this,favouriteMoviesObserver);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(URL_KEY,url);
        outState.putInt(sortOrderKey,sort);
        listState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!= null)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
//        if(isNetworkConnected()){
//            new MyTask().execute(url);
//
//        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
           if(movieList != null)
           {
               movieList.addItemDecoration(landscapeDecoration);
           }
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            if(movieList!=null) {
                movieList.addItemDecoration(portratDecoration);
            }
        }
    }




    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
        if(!isNetworkConnected())
        {

            showNoInternet();
            return;
        }
    }

    @Override
    public void onItemClicked(Movie movie) {


        Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

        intent.putExtra(Utils.movieKey,movie);
        Log.d("MOVIES--",movie.getPoster_path());
        startActivity(intent);
    }


    class MyTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder results = new StringBuilder();
            HttpURLConnection con = null;
            

            try {
                URL url = new URL(params[0]);
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


        @Override
        protected void onPostExecute(String s) {

            movies = new ArrayList<>();
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray array = jsonObject.getJSONArray("results");

                for(int i=0;i<array.length(); i++)
                {
                    JSONObject movieItem = array.getJSONObject(i);

                    Movie movie = Utils.getMovieFromJsonObject(movieItem);
                    //Log.d("MOVIE",movie.toString());
                    movies.add(movie);


                }

                adapter.setMovies(movies);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void showNoInternet()
    {
        Snackbar.make(coordinatorLayout,getResources().getString(R.string.no_internet),Snackbar.LENGTH_LONG)
                .show();
    }





}