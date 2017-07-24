package com.example.anubhav.projectmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.os.Build.VERSION_CODES.M;
import static com.example.anubhav.projectmoviesapp.MainActivity.Data;
import static com.example.anubhav.projectmoviesapp.R.id.overview;
import static com.example.anubhav.projectmoviesapp.R.id.userrating;

public class Main2Activity extends AppCompatActivity implements TrailerMoviesAdapter.TrailerMoviesAdapterOnClickHandler{
    private FloatingActionButton fab;

    RecyclerView mrecyclerviewtrailers;

    SQLiteDatabase mydb;
    TextView OVERVIEW;
    public TrailerMoviesAdapter mAdapter;
    TextView TITLE;
    TextView RELEASE;
    TextView USERNAME;
    TextView Testing;
    String id_of_that_item;
    String overview_of_that_item;
    String title_of_that_item;
     String[] key;
     String[] Name;


    String[] Reviews=null;
    public static String TrailersUrl="https://api.themoviedb.org/3/movie";
    String id;
    String Fetched_Reviews=null;
    ImageView Image;




    public static Bundle BundleRecyclerViewState=null;
    static String[] Data_Trailers=null;
    static String Data_Reviews=null;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        MoviesDatabaseDbHelper db=new MoviesDatabaseDbHelper(this);
        mydb=db.getWritableDatabase();





        Testing=(TextView) findViewById(R.id.testing);


        id_of_that_item=MainActivity.id_clicked;

        Image=(ImageView)findViewById(R.id.I);
        TITLE=(TextView)findViewById(R.id.title);
        OVERVIEW=(TextView)findViewById(overview);
        RELEASE=(TextView)findViewById(R.id.releasedate);
        USERNAME=(TextView) findViewById(userrating);



        Intent i=getIntent();
        String text = null;
        id=MainActivity.id_clicked;

        if(i.hasExtra(Intent.EXTRA_TEXT))
        {
            text=i.getStringExtra(i.EXTRA_TEXT);
        }

        int j;
        StringBuilder title=new StringBuilder();
        StringBuilder overview=new StringBuilder();
        StringBuilder realeasedate=new StringBuilder();
        StringBuilder userrating=new StringBuilder();
        StringBuilder thumbnail=new StringBuilder();
        int flag=0;
        for(j=0;j<text.length();j++)
        {
            if((flag==0)&&(text.charAt(j)!='<'))
            {
                title.append(text.charAt(j));


            }
            if(text.charAt(j)=='<')
            {
                flag++;
            }
            if((flag==1)&&(text.charAt(j)!='<'))
            {
                overview.append(text.charAt(j));


            }
            if((flag==2)&&(text.charAt(j)!='<'))
            {
                realeasedate.append(text.charAt(j));


            }
            if((flag==3)&&(text.charAt(j)!='<'))
            {
                thumbnail.append(text.charAt(j));


            }
            if((flag==4)&&(text.charAt(j)!='<'))
            {
                userrating.append(text.charAt(j));


            }





        }
        char ch;
        String y=userrating.toString();
        ch=y.charAt(0);
        int a=ch-'0';

        StringBuilder b=new StringBuilder();
        RELEASE.setText(realeasedate.toString());
        OVERVIEW.setText(overview.toString());
        TITLE.setText(title.toString());
        title_of_that_item=title.toString();
        overview_of_that_item=overview.toString();

        USERNAME.setText(userrating.toString()+"/"+"10 STARS");

        String x="http://image.tmdb.org/t/p/w185/";
        b.append(x);
        b.append(thumbnail.toString());

        Picasso
                .with(Main2Activity.this)
                .load(b.toString())
                .fit()
                .into(Image);




        mrecyclerviewtrailers=(RecyclerView)findViewById(R.id.RECYCLER_VIEW_ID_TRAILERS);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mrecyclerviewtrailers.setLayoutManager(layoutManager);
        mrecyclerviewtrailers.setHasFixedSize(true);
        mAdapter=new TrailerMoviesAdapter(this);
        mrecyclerviewtrailers.setAdapter(mAdapter);



        Reviews=null;



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicking();
                fab.setVisibility(View.INVISIBLE);
            }
        });



        if(BundleRecyclerViewState==null) {


            FetchingStartoftrailer();


        }
        else
        {
            onResume();

        }

    }



    @Override
    protected void onPause()
    {   Log.v("K","Pause");
        super.onPause();

        // save RecyclerView state

        BundleRecyclerViewState = new Bundle();
        Parcelable listState = mrecyclerviewtrailers.getLayoutManager().onSaveInstanceState();
        BundleRecyclerViewState.putParcelable("KEY_RECYCLER_STATE_TRAILER", listState);
        BundleRecyclerViewState.putStringArray("SAVED_RECYCLER_VIEW_DATASET_ID_TRAILERS",Data_Trailers);
        BundleRecyclerViewState.putString("Reviews",Data_Reviews);
    }
    @Override
    protected void onResume()
    {
        super.onResume();


        // restore RecyclerView state
        if (BundleRecyclerViewState != null) {
            Parcelable listState = BundleRecyclerViewState.getParcelable("KEY_RECYCLER_STATE_TRAILER");
            Data_Trailers=BundleRecyclerViewState.getStringArray("SAVED_RECYCLER_VIEW_DATASET_ID_TRAILERS");
            mAdapter.setData(Data_Trailers,this);
            Data_Reviews=BundleRecyclerViewState.getString("Reviews",Data_Reviews);
            Testing.setText(Data_Reviews);
            mrecyclerviewtrailers.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

















    private void FetchingStartoftrailer() {


       new FetchTrailerTask().execute();
        new FetchReviewsTask().execute();


    }




    public void clicking()
    {


        ContentValues cv=new ContentValues();
        cv.put(MoviesDatabaseContract.moviesEntry.id_of_item,Integer.parseInt(id_of_that_item));
        cv.put(MoviesDatabaseContract.moviesEntry.title_of_item,title_of_that_item);
        cv.put(MoviesDatabaseContract.moviesEntry.overview_of_item,overview_of_that_item);
        /*
        long result=mydb.insert(MoviesDatabaseContract.moviesEntry.TABLE_NAME,null,cv);
        if(result==-1)
            Toast.makeText(Main2Activity.this,"NOT Inserted",Toast.LENGTH_SHORT).show();
        else
        Toast.makeText(Main2Activity.this,"Inserted",Toast.LENGTH_SHORT).show();
        */
        Uri uri=getContentResolver().insert(MoviesDatabaseContract.moviesEntry.CONTENT_URI,cv);
        Toast.makeText(Main2Activity.this,"ADDED TO favourites",Toast.LENGTH_SHORT).show();










    }

    public class FetchReviewsTask extends AsyncTask<Object,Object,String[]>
    {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(Object... params) {

            URL REVIEWURL=buildUrlForReviews();

            try {
                String movieResponseTrailer = NetworkUtils
                        .getResponseFromHttpUrl(REVIEWURL);

                JSONObject popularJson=new JSONObject(movieResponseTrailer);
                JSONArray Array=popularJson.getJSONArray("results");

                if(Array.length()==0)
                    return null;
                Reviews=new String[Array.length()];
                int i;
                for(i=0;i<Array.length();i++)
                {

                    JSONObject ob=Array.getJSONObject(i);
                    Reviews[i]=ob.getString("content");



                }



            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Reviews;
        }

        @Override
        protected void onPostExecute(String[] NameData) {

            if (NameData != null) {

                Data_Reviews=NameData[0]+"";

                Testing.setText(NameData[0]+"");



            } else {

                Data_Reviews="NO REVIEWS";
                Testing.setText("NO REVIEWS");


            }
        }

    }



    public class FetchTrailerTask extends AsyncTask<Object,Object,String[] >
    {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String[] doInBackground(Object... params) {

            URL TRAILERURL=buildUrlForTarilers();



            try {
                String movieResponseTrailer = NetworkUtils
                        .getResponseFromHttpUrl(TRAILERURL);

                JSONObject popularJson=new JSONObject(movieResponseTrailer);
                JSONArray Array=popularJson.getJSONArray("results");
                key=new String[Array.length()];
                Name=new String[Array.length()];
                int i;
                for(i=0;i<Array.length();i++)
                {
                    JSONObject ob=Array.getJSONObject(i);
                    key[i]=ob.getString("key");
                    Name[i]=ob.getString("name");




                }
                } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }

            return Name;







        }
        @Override
        protected void onPostExecute(String[] NameData) {

            if (NameData != null) {

                Data_Trailers=NameData;

                mAdapter.setData(NameData,getApplicationContext());





            } else {
                Data_Trailers=NameData;
                mAdapter.setData(null,getApplicationContext());


            }





        }




    }



    public URL buildUrlForTarilers()
    {
        Uri trailerBuiltUri=null;

        StringBuilder BaseUrlString=new StringBuilder();
        BaseUrlString.append(TrailersUrl);
        //TODO ADD API key
        BaseUrlString.append("/"+id+"/videos"+"?api_key=<key>");
        String BaseUrlStr=BaseUrlString.toString();

        trailerBuiltUri=Uri.parse(BaseUrlStr).buildUpon()
                .appendQueryParameter("language","en-US").build();

        URL finalURL=null;
        try{
            finalURL=new URL(trailerBuiltUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return finalURL;

    }


    public URL buildUrlForReviews()
    {
        Uri trailerBuiltUri=null;

        StringBuilder BaseUrlString=new StringBuilder();
        BaseUrlString.append(TrailersUrl);
        //TODO Add API KEY
        BaseUrlString.append("/"+id+"/reviews"+"?api_key=<key>");
        String BaseUrlStr=BaseUrlString.toString();

        trailerBuiltUri=Uri.parse(BaseUrlStr).buildUpon()
                .appendQueryParameter("language","en-US")
                .appendQueryParameter("page","1").build();


        URL finalURL=null;
        try{
            finalURL=new URL(trailerBuiltUri.toString());
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }


        return finalURL;

    }
    public void onClick(int weatherForDay) {


        StringBuilder s=new StringBuilder();
        s.append("https://www.youtube.com/watch?v="+key[weatherForDay]);
        String url=s.toString();
        Uri webpage=Uri.parse(url);
        Intent i=new Intent(Intent.ACTION_VIEW,webpage);
        if(i.resolveActivity(getPackageManager())!=null)
        {
            startActivity(i);
        }




    }






}
