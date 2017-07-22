package com.example.anubhav.projectmoviesapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.data;
import static android.R.attr.rating;
import static android.R.attr.x;

public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.PopularMoviesAdapterOnClickHandler {

    RecyclerView mrecyclerview;
    public PopularMoviesAdapter mAdapter;
    SQLiteDatabase mdb;
    static String id_clicked;

    String[] id;
    String[] title;
    String[] overview;
    String[] userrating;

    String[] release_date;
    String[] simple;
    int check=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int x=3;
        MoviesDatabaseDbHelper db=new MoviesDatabaseDbHelper(this);
        mdb=db.getWritableDatabase();

        mrecyclerview=(RecyclerView)findViewById(R.id.RECYCLER_VIEW_ID);


        mrecyclerview.setLayoutManager(new GridLayoutManager(this,x));

        mrecyclerview.setHasFixedSize(true);
        mAdapter=new PopularMoviesAdapter(this);

        mrecyclerview.setAdapter(mAdapter);




        loadurl();












    }
    public void loadurl()
    {


        new FetchWeatherTask().execute();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.sort)
        {
            mAdapter.setData(null,this);
            check=1;

            loadurl();
        }
        if(id==R.id.sort2)
        {
            mAdapter.setData(null,this);
            check=2;

            loadurl();



        }

        return true;
    }

    public void onClick(int weatherForDay) {
        Context context = this;

        id_clicked=id[weatherForDay];

        StringBuilder built=new StringBuilder();
        built.append(title[weatherForDay]);
        built.append("<");
        built.append(overview[weatherForDay]);
        built.append("<");
        built.append(release_date[weatherForDay]);
        built.append("<");
        built.append(simple[weatherForDay]);
        built.append("<");
        built.append(userrating[weatherForDay]);
        built.append("<");
        String b=built.toString();

        Toast.makeText(MainActivity.this,id_clicked,Toast.LENGTH_SHORT).show();

        Intent i=new Intent(MainActivity.this,Main2Activity.class);
        i.putExtra(Intent.EXTRA_TEXT,b);
        startActivity(i);

    }

    public void press(View view) {


        loadurl();


    }

    public class FetchWeatherTask extends AsyncTask<Object, Object, String[]> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(Object... params) {

            /* If there's no zip code, there's nothing to look up. */



            URL MoviesURL = buildurl();

            try {
                String movieResponse = NetworkUtils
                        .getResponseFromHttpUrl(MoviesURL);










                JSONObject popularJson=new JSONObject(movieResponse);
                JSONArray Array=popularJson.getJSONArray("results");
                simple=new String[Array.length()];
                int i;int j=0;
                title=new String[Array.length()];
                overview=new String[Array.length()];
                userrating=new String[Array.length()];
                release_date=new String[Array.length()];
                id=new String[Array.length()];
                for(i=0; i<Array.length() ; i++)
                {
                    String posterpath;
                    JSONObject ob=Array.getJSONObject(i);
                    id[i]=ob.getString("id");
                    posterpath=ob.getString("poster_path");
                    simple[i]=posterpath;
                    title[i]=ob.getString("title");
                    overview[i]=ob.getString("overview");
                    String x=ob.getString("vote_average");
                    userrating[i]=x;
                    release_date[i]=ob.getString("release_date");










                }





                return simple;





            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {

            if (weatherData != null) {



                mAdapter.setData(weatherData,getApplicationContext());


            } else {
                        mAdapter.setData(null,getApplicationContext());

            }
        }
    }

    private static final String STATIC_WEATHER_URL =
            "https://api.themoviedb.org/3/discover/movie?api_key=14ba823de3e05b5696f262efbdfe38ad";
    private static final String STATIC_WEATHER_URL2 =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=14ba823de3e05b5696f262efbdfe38ad";
    private static final String STATIC_WEATHER_URL3 =
            "https://api.themoviedb.org/3/movie/popular?api_key=14ba823de3e05b5696f262efbdfe38ad";

    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;
    private static final String FORECAST_BASE_URL2 = STATIC_WEATHER_URL2;
    private static final String FORECAST_BASE_URL3 = STATIC_WEATHER_URL3;


    public URL buildurl()
    {
        Uri builtUri = null;
        if(check==0)
        {


            builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("sort_by","popularity.desc")

                    .build();
        }
        else
        if(check==1)
        {

            builtUri = Uri.parse(FORECAST_BASE_URL2).buildUpon()
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("sort_by","popularity.desc")

                    .build();









        }
        else
        if(check==2)
        {

            builtUri = Uri.parse(FORECAST_BASE_URL3).buildUpon()
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("sort_by","popularity.desc")

                    .build();


        }





        URL url=null;
        try
        {
            url=new URL(builtUri.toString());



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }











}