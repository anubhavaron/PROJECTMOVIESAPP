package com.example.anubhav.projectmoviesapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static android.R.attr.onClick;
import static android.R.attr.x;

/**
 * Created by ANUBHAV on 6/30/2017.
 */

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder>{

    private String[] data;
    private final PopularMoviesAdapterOnClickHandler mClickHandler;



    public interface PopularMoviesAdapterOnClickHandler{

        void onClick(int x);
    }








    private Context context;
    public PopularMoviesAdapter(PopularMoviesAdapterOnClickHandler clickHandler)

    {
        mClickHandler=clickHandler;

    }

    public void setData(String[] record,Context context)
    {
        data=record;
        this.context=context;
        notifyDataSetChanged();


    }



    @Override
    public PopularMoviesAdapter.PopularMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_screen,parent,false);
        return new PopularMoviesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PopularMoviesAdapter.PopularMoviesViewHolder holder, int position) {

        int adapterposition=position;
        String x=data[adapterposition];
        StringBuilder builder=new StringBuilder();
        builder.append("http://image.tmdb.org/t/p/w185/");
        builder.append(x);
        String build=builder.toString();
        Picasso.with(context).load(build).resize(400, 400).into(holder.img);




    }

    @Override
    public int getItemCount() {

        if(data!=null)
            return data.length;
        else
            return 0;



    }



    public class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView img;
        public PopularMoviesViewHolder(View view)
        {

            super(view);
            img=(ImageView)view.findViewById(R.id.imageviewid);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            int adapterPosition=getAdapterPosition();
            String x=data[adapterPosition];
            mClickHandler.onClick(adapterPosition);

        }
    }



}
