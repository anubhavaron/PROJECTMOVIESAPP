package com.anubhav.anubhav.projectmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ANUBHAV on 7/20/2017.
 */

public class TrailerMoviesAdapter extends RecyclerView.Adapter<TrailerMoviesAdapter.TrailerMoviesViewHolder> {



    private String[] data;
    private final TrailerMoviesAdapterOnClickHandler mClickHandler;

    public interface TrailerMoviesAdapterOnClickHandler{

        void onClick(int x);
    }



    private Context context;
    public TrailerMoviesAdapter(TrailerMoviesAdapterOnClickHandler clickHandler)

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
    public TrailerMoviesAdapter.TrailerMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_screen,parent,false);
        return new TrailerMoviesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TrailerMoviesAdapter.TrailerMoviesViewHolder holder, int position) {

        int adapterposition=position;

        holder.textfield.setText(data[adapterposition]);





    }
    @Override
    public int getItemCount() {

        if(data!=null)
            return data.length;
        else
            return 0;



    }
    public class TrailerMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textfield;
        public TrailerMoviesViewHolder(View view)
        {

            super(view);
            textfield=(TextView) view.findViewById(R.id.Trailer_id);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            int adapterPosition=getAdapterPosition();

            mClickHandler.onClick(adapterPosition);

        }
    }























}
