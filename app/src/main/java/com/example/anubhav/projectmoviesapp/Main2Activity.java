package com.example.anubhav.projectmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity {
    TextView OVERVIEW;
    TextView TITLE;
    TextView RELEASE;
    TextView USERNAME;



    ImageView Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);












        Image=(ImageView)findViewById(R.id.I);
        TITLE=(TextView)findViewById(R.id.title);
        OVERVIEW=(TextView)findViewById(R.id.overview);
        RELEASE=(TextView)findViewById(R.id.releasedate);
        USERNAME=(TextView) findViewById(R.id.userrating);



        Intent i=getIntent();
        String text = null;
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










    }

}
