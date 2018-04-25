package com.example.chenn.entertainmentandplacessearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PopulateReviewsAdapter extends RecyclerView.Adapter<PopulateReviewsAdapter.MyViewHolder> {

    ReviewsDetails [] obj;
    PopulateReviewsAdapter(ReviewsDetails [] obj)
    {
        this.obj=obj;
    }
    @Override
    public PopulateReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);

        return new PopulateReviewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PopulateReviewsAdapter.MyViewHolder holder, int position) {

        holder.name.setText(obj[position].name);
        holder.time.setText(obj[position].time);
        holder.text.setText(obj[position].text);
        holder.rb.setRating((float)obj[position].rating);

        if(obj[position].img_url!="")
        {Picasso.get().load(obj[position].img_url).into(holder.img);}

    }

    @Override
    public int getItemCount() {
        return obj.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time,text;
        public RatingBar rb;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.rev_name);
            time = (TextView) view.findViewById(R.id.rev_time);
            text = (TextView) view.findViewById(R.id.rev_text);
            rb = (RatingBar) view.findViewById(R.id.ratingBar2);
            img = (ImageView) view.findViewById(R.id.rev_img_icon);

        }
    }
}
