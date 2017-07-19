package com.example.anubhav.projectmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {
    TextView OVERVIEW;
    TextView TITLE;
    TextView RELEASE;
    TextView USERNAME;
    EditText Testing;
    String[] key;
    String[] Name;
    public static String TrailersUrl="https://api.themoviedb.org/3/movie";
    String id;

    ImageView Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);







        Testing=(EditText)findViewById(R.id.testing);




        Image=(ImageView)findViewById(R.id.I);
        TITLE=(TextView)findViewById(R.id.title);
        OVERVIEW=(TextView)findViewById(R.id.overview);
        RELEASE=(TextView)findViewById(R.id.releasedate);
        USERNAME=(TextView) findViewById(R.id.userrating);



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

        USERNAME.setText(userrating.toString()+"/"+"10 STARS");

        String x="http://image.tmdb.org/t/p/w185/";
        b.append(x);
        b.append(thumbnail.toString());

        Picasso
                .with(Main2Activity.this)
                .load(b.toString())
                .fit()
                .into(Image);








        FetchingStartoftrailer();

    }

    private void FetchingStartoftrailer() {


        new FetchTrailerTask().execute();

    }

    public class FetchTrailerTask extends AsyncTask<Object,Object,String[]>
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



                Testing.setText(NameData[0]);


            } else {
                Testing.setText("kuch nh liya");


            }
        }




    }



    public URL buildUrlForTarilers()
    {
        Uri trailerBuiltUri=null;

        StringBuilder BaseUrlString=new StringBuilder();
        BaseUrlString.append(TrailersUrl);
        BaseUrlString.append("/"+id+"/videos"+"?api_key=14ba823de3e05b5696f262efbdfe38ad");
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




}
