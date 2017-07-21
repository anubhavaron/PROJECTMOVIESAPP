package com.example.anubhav.projectmoviesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by ANUBHAV on 7/21/2017.
 */

public class MoviesDatabaseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Movies.db";

    private static final int DATABASE_VERSION=1;



    public MoviesDatabaseDbHelper(Context context)
    {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);


    }



    @Override
    public void onCreate(SQLiteDatabase db) {
     final String SQL_CREATE_TABLE="CREATE TABLE "+MoviesDatabaseContract.moviesEntry.TABLE_NAME+
                " ("
                +MoviesDatabaseContract.moviesEntry.id_of_item+" INTEGER PRIMARY KEY,"
                +MoviesDatabaseContract.moviesEntry.title_of_item+" TEXT NOT NULL, "
                + MoviesDatabaseContract.moviesEntry.overview_of_item+" TEXT NOT NULL);";


                   db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

db.execSQL("DROP TABLE IF EXISTS "+MoviesDatabaseContract.moviesEntry.TABLE_NAME);
        onCreate(db);





    }
}
