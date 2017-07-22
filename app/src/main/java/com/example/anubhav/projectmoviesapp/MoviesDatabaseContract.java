package com.example.anubhav.projectmoviesapp;

import android.graphics.Path;
import android.net.Uri;

/**
 * Created by ANUBHAV on 7/21/2017.
 */

public class MoviesDatabaseContract {

public static final String Authority="com.example.anubhav.projectmoviesapp";
    public static final Uri Base_Content_Uri=Uri.parse("content://"+Authority);
    public static final String Path_Tasks="tasks";
    public static final class moviesEntry
    {
        public static final Uri CONTENT_URI=Base_Content_Uri.buildUpon().appendPath(Path_Tasks).build();

        public static final String TABLE_NAME="FAVIORATE_MOVIES";
        public static final String id_of_item="ID_OF_ITEM";
        public static final String title_of_item="TITLE_OF_ITEM";
        public static final String overview_of_item="OVERVIEW_OF_ITEM";






    }



}
