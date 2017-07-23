package com.example.anubhav.projectmoviesapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.support.v7.widget.helper.ItemTouchHelper;
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
    RecyclerView mrecyclerviewforfaviorate;
    public PopularMoviesAdapter mAdapter;
    public FaviorateAdapter mAdapterFaviorate;
    SQLiteDatabase mdb;
    static String id_clicked;
    static int value_for_saved_instance=0;


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

        int x=2;
        MoviesDatabaseDbHelper db=new MoviesDatabaseDbHelper(this);
        mdb=db.getWritableDatabase();

        mrecyclerview=(RecyclerView)findViewById(R.id.RECYCLER_VIEW_ID);
        mrecyclerviewforfaviorate=(RecyclerView)findViewById(R.id.RECYCLER_VIEW_ID_Faviorate);
         mrecyclerview.setLayoutManager(new GridLayoutManager(this,x));


        mrecyclerview.setHasFixedSize(true);
        mAdapter=new PopularMoviesAdapter(this);

        mrecyclerview.setAdapter(mAdapter);
         LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mrecyclerviewforfaviorate.setLayoutManager(layoutManager);
        mrecyclerviewforfaviorate.setHasFixedSize(true);
        mAdapterFaviorate=new FaviorateAdapter(this);
        mrecyclerviewforfaviorate.setAdapter(mAdapterFaviorate);
        mAdapterFaviorate.swapCursor(null);





        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {




                int id = (int) viewHolder.itemView.getTag();


                String stringId = Integer.toString(id);
                Uri uri = MoviesDatabaseContract.moviesEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();


                getContentResolver().delete(uri, null, null);
                mAdapterFaviorate.swapCursor(getAllguests());


            }
        }).attachToRecyclerView(mrecyclerviewforfaviorate);





        if(savedInstanceState==null)
            loadurl();
        else
        {





            int myInt = savedInstanceState.getInt("MyInt");

            if(myInt==0)
                loadurl();
            else
            if(myInt==1)
            {
                doingsortaccordingtodefault();
            }
            else
            if(myInt==2)
            {
                doingsortaccordingtopopularity();
            }
            else
            {
                SHOWINGFAVIORATEMOVIESLISTHERE();
            }






        }






    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("MyInt", value_for_saved_instance);

        // etc.
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        int myInt = savedInstanceState.getInt("MyInt");

        if(myInt==0)
            loadurl();
        else
            if(myInt==1)
            {
                doingsortaccordingtodefault();
            }
            else
                if(myInt==2)
                {
                    doingsortaccordingtopopularity();
                }
                else
                {
                    SHOWINGFAVIORATEMOVIESLISTHERE();
                }

    }




    //load url function is for TaskFetching Through Main Activity
    // check =0 makes def
    // check=1 makes Popularity
    // check=2 makes Top rated
    //check=3 makes data show


     public void loadurl()
    {


        new FetchWeatherTask().execute();


    }
    public Cursor getAllguests()
    {

        return mdb.query(MoviesDatabaseContract.moviesEntry.TABLE_NAME,null,null,null,null,null,MoviesDatabaseContract.moviesEntry.id_of_item);



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
            value_for_saved_instance=1;
            doingsortaccordingtodefault();

        }
        if(id==R.id.sort2)
        {
            value_for_saved_instance=2;
            doingsortaccordingtopopularity();

        }
        if(id==R.id.sort3)
        {
            value_for_saved_instance=3;
                SHOWINGFAVIORATEMOVIESLISTHERE();

        }
        return true;
    }

    public void doingsortaccordingtodefault()
    {
        mrecyclerview.setVisibility(View.VISIBLE);
        mrecyclerviewforfaviorate.setVisibility(View.INVISIBLE);
        mAdapter.setData(null,this);
        check=1;
        loadurl();



    }
    public void doingsortaccordingtopopularity()
    {
        mrecyclerview.setVisibility(View.VISIBLE);
        mrecyclerviewforfaviorate.setVisibility(View.INVISIBLE);
        mAdapter.setData(null,this);
        check=2;

        loadurl();

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

        Toast.makeText(MainActivity.this,id_clicked,Toast.LENGTH_LONG).show();

        Intent i=new Intent(MainActivity.this,Main2Activity.class);
        i.putExtra(Intent.EXTRA_TEXT,b);
        startActivity(i);

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
                Toast.makeText(MainActivity.this,"Network Connection and SWIPE TO DELETE",Toast.LENGTH_LONG).show();
                        SHOWINGFAVIORATEMOVIESLISTHERE();

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

    public void SHOWINGFAVIORATEMOVIESLISTHERE()
    {


        Toast.makeText(MainActivity.this,"SWIPE TO DELETE",Toast.LENGTH_SHORT).show();

        mrecyclerview.setVisibility(View.GONE);
        mrecyclerviewforfaviorate.setVisibility(View.VISIBLE);


        Cursor cursor= getContentResolver().query(MoviesDatabaseContract.moviesEntry.CONTENT_URI,
                null,
                null,
                null,
                MoviesDatabaseContract.moviesEntry.id_of_item);


        mAdapterFaviorate.swapCursor(cursor);




    }





}