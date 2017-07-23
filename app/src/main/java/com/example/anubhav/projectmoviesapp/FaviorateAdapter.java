package com.example.anubhav.projectmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static android.R.attr.data;

/**
 * Created by ANUBHAV on 7/22/2017.
 */


public class FaviorateAdapter extends RecyclerView.Adapter<FaviorateAdapter.NUMBERVIEWHOLDER>{


    private Cursor mcursor;
    private Context mcontext;
    public FaviorateAdapter(Context context)
    {

        this.mcontext=context;



    }




    @Override
    public FaviorateAdapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.faviorate_movies_screen,parent,false);
        return new NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(FaviorateAdapter.NUMBERVIEWHOLDER holder, int position) {

        if(!mcursor.moveToPosition(position))
            return ;

        String title=mcursor.getString(mcursor.getColumnIndex(MoviesDatabaseContract.moviesEntry.title_of_item));

        String overview=mcursor.getString(mcursor.getColumnIndex(MoviesDatabaseContract.moviesEntry.overview_of_item));
        holder.tit.setText(title);
        holder.over.setText(overview);

        int id=mcursor.getInt(mcursor.getColumnIndex(MoviesDatabaseContract.moviesEntry.id_of_item));
        holder.itemView.setTag(id);





    }

    @Override
    public int getItemCount() {

            if(mcursor==null)
                return 0;
        else
            return mcursor.getCount();



    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder
    {
        TextView tit;
        TextView over;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            tit=(TextView)view.findViewById(R.id.textViewforfavoraiteMoviesTitle);
            over=(TextView)view.findViewById(R.id.textviewforfavoraieMoviesOverview);


        }



    }
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mcursor != null) mcursor.close();
        mcursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }



}
