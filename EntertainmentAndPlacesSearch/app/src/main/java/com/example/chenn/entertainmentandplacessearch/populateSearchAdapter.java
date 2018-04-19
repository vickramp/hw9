package com.example.chenn.entertainmentandplacessearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class populateSearchAdapter extends RecyclerView.Adapter<populateSearchAdapter.MyViewHolder> {


    public populateSearchAdapter() {
        //this.moviesList = moviesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.populatesearchrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Movie movie = moviesList.get(position);
        holder.name.setText("University of Southern California");
       holder.location.setText("Los Angeles");
      // holder.year.setText("Hurrah");
    }

    @Override
    public int getItemCount() {
        return 10;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, location;

        public MyViewHolder(View view) {
            super(view);

           name = (TextView) view.findViewById(R.id.srchName);
            location = (TextView) view.findViewById(R.id.srchlocation);
           // year = (TextView) view.findViewById(R.id.sr2);
        }
    }
}







