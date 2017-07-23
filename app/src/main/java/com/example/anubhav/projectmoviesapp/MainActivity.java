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

        int x=2;        // Means my Grid willshow 2 posters
        MoviesDatabaseDbHelper db=new MoviesDatabaseDbHelper(this);
        mdb=db.getWritableDatabase();
        mrecyclerview=(RecyclerView)findViewById(R.id.RECYCLER_VIEW_ID);


        mrecyclerviewforfaviorate=(RecyclerView)findViewById(R.id.RECYCLER_VIEW_ID_Faviorate);


        //It is recyclerview to display posters of movie in grid layout
        mrecyclerview.setLayoutManager(new GridLayoutManager(this,x));
        mrecyclerview.setHasFixedSize(true);
        mAdapter=new PopularMoviesAdapter(this);
        mrecyclerview.setAdapter(mAdapter);



        //It is recyclerview if user chooses Faviourate to display or NEtwork problem in App then faviorate will display
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mrecyclerviewforfaviorate.setLayoutManager(layoutManager);
        mrecyclerviewforfaviorate.setHasFixedSize(true);
        mAdapterFaviorate=new FaviorateAdapter(this);
        mrecyclerviewforfaviorate.setAdapter(mAdapterFaviorate);
        mAdapterFaviorate.swapCursor(null);




        //It is cursor if user swipe out data from Faviorate movies Database
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



        //Save state will be saved in myInt variable
        //Using Save STate if myInt is 0 then show default posters if save state if 1 then show
        //sorting according to top rated and if save state is 2 then sort it according to popularity

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

    //Here using savestate using value_for_saved_instance_variable which will be change according to function that will implement sorting
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        savedInstanceState.putInt("MyInt", value_for_saved_instance);


    }
    //Restoring from savestate and make changes in app aacording to savestate so that app can return back to previous state
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


    //This function will Fetch data of posters from internet and if internet connection is not present then it will show Faviorate Movies Database
     public void loadurl()
    {
        new FetchWeatherTask().execute();
    }
    //Give cursor for All data
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
        {   //IF THIS IS CLICKED IT MEANS i have to sort according to top rated and show it
            //saving state in next line
            value_for_saved_instance=1;

            doingsortaccordingtodefault();

        }
        if(id==R.id.sort2)
        {   //It means if item clicked i have to sort according to popularity
            value_for_saved_instance=2;
            doingsortaccordingtopopularity();

        }
        if(id==R.id.sort3)
        {   //It means i have to show data of Favioarte movies database
            value_for_saved_instance=3;

            SHOWINGFAVIORATEMOVIESLISTHERE();

        }
        return true;
    }
    //This function is doing sort according to top rated first it invisible the Favoraite movie recyclerview and show posters Recycler View
    //Then set recyclerview  of Posters to null so that it can be upgraded then make check =1 ,it will use /top_rated/movies endpoint to
    //download movies according to top _rated and load url
    public void doingsortaccordingtodefault()
    {
        mrecyclerview.setVisibility(View.VISIBLE);
        mrecyclerviewforfaviorate.setVisibility(View.INVISIBLE);
        mAdapter.setData(null,this);
        check=1;
        loadurl();



    }
    //This function is doing sort according to popularity first it invisible the Favoraite movie recyclerview and show posters Recycler View visible
    //Then set recyclerview  of Posters to null so that it can be upgraded then make check =2 ,it will use /popular/movies endpoint to
    //download movies according to top _rated and load url



    public void doingsortaccordingtopopularity()
    {
        mrecyclerview.setVisibility(View.VISIBLE);
        mrecyclerviewforfaviorate.setVisibility(View.INVISIBLE);
        mAdapter.setData(null,this);
        check=2;

        loadurl();

   }




    //this is implemented just to starting Detail Activity which in my case named as Main2Activity and start intent
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


    //After load url function fetching will start and send posterpath that is named as simple in my code to that adapter so that it can show it.
    public class FetchWeatherTask extends AsyncTask<Object, Object, String[]> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(Object... params) {




            //Url will call build function that will use check for for confirming that we want to fetch TOP RATED Posters or POPULARITY posters
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
        //if fetching is not possible due to bad internet connection then it will show Faviorate mOvies
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
    //this is for default show of posters
    private static final String STATIC_WEATHER_URL =
            "https://api.themoviedb.org/3/discover/movie?api_key=14ba823de3e05b5696f262efbdfe38ad";
    //this will for Top_Rated
    private static final String STATIC_WEATHER_URL2 =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=14ba823de3e05b5696f262efbdfe38ad";
    //This will be for Popular Fetching
    private static final String STATIC_WEATHER_URL3 =
            "https://api.themoviedb.org/3/movie/popular?api_key=14ba823de3e05b5696f262efbdfe38ad";

    private static final String FORECAST_BASE_URL = STATIC_WEATHER_URL;
    private static final String FORECAST_BASE_URL2 = STATIC_WEATHER_URL2;
    private static final String FORECAST_BASE_URL3 = STATIC_WEATHER_URL3;


    public URL buildurl()
    {
        Uri builtUri = null;

        //Means we want to fetch normal default data
        if(check==0)
        {


            builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("sort_by","popularity.desc")

                    .build();
        }
        else        //We want to Fetch Data ACcording to Top_Rated as we are using FORECAST_BASE_URL2
        if(check==1)
        {

            builtUri = Uri.parse(FORECAST_BASE_URL2).buildUpon()
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("sort_by","popularity.desc")

                    .build();

            }
        else         //We want to Fetch Data ACcording to Top_Rated as we are using FORECAST_BASE_URL3
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


    //This function firstly get Cursor and then upgraded the Faviorate movie list with new cursor
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