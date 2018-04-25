package com.example.chenn.entertainmentandplacessearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class populateFavouriteAdapter extends RecyclerView.Adapter<populateFavouriteAdapter.MyViewHolder> {
    List<DetailsObject> dobj;

    public Context myContext;
    SharedPreferences sharedPref;
    displayFavourites ref;

    public populateFavouriteAdapter(DetailsObject[] obj,Context context,SharedPreferences sharedPref, displayFavourites ref) {
        this.ref = ref;
        List<DetailsObject> p = new ArrayList<DetailsObject>();
        for(int i=0;i<obj.length;i++)
        {
            p.add(obj[i]);
        }
        this.dobj = p;
        this.myContext = context;
        this.sharedPref = sharedPref;
        //this.moviesList = moviesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.populatesearchrow, parent, false);
        //itemView.setOnClickListener();

        return new MyViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Movie movie = moviesList.get(position);
        // holder.imgview.setI
       Picasso.get().load(dobj.get(position).iconurl.substring(1,dobj.get(position).iconurl.length()-1)).into(holder.imgview);
        holder.name.setText(dobj.get(position).name.substring(1,dobj.get(position).name.length()-1));
        holder.location.setText(dobj.get(position).add.substring(1,dobj.get(position).add.length()-1));
        holder.favview.setImageResource(R.drawable.favouritesoutlineblack);
        holder.clicked = false;
        holder.bind(position);

        SharedPreferences.Editor editor = sharedPref.edit();
        System.out.println("Here for "+position + " and value is "+holder.clicked);
        holder.favview.setImageResource(R.drawable.favredheart);

        /*Set<String> set = sharedPref.getStringSet("favID3",null);
        if(set!=null) {
            System.out.println("value is " + Arrays.toString(set.toArray()));
            String id = dobj.get(position).obj.getAsJsonObject().get("id").getAsString();
            System.out.println(" id " + id);

            if (set.contains(id)) {
                System.out.println("Inside this beacuse it contained Id!! "+id);
                holder.clicked = true;
                holder.favview.setImageResource(R.drawable.favredheart);
            }
        }
        */
        final MyViewHolder h1 = holder;
        // holder.year.setText("Hurrah");
    }

    @Override
    public int getItemCount() {
        return dobj.size();
    }
    public void removeItem(int position)
    {
        System.out.println("Position is " +position +" Size is "+dobj.size());

        SharedPreferences.Editor editor = sharedPref.edit();

        String fstr = sharedPref.getString("favIDarr",null);

        Gson gson = new Gson();

        List<String> list;
        list =  gson.fromJson(fstr, List.class);
        String key = dobj.get(position).obj.getAsJsonObject().get("id").getAsString();
        System.out.println("Key is "+key);
        System.out.println("values are "+Arrays.toString(list.toArray()));
        list.remove(key);
        System.out.println("values after are "+Arrays.toString(list.toArray()));

        // set.add(obj.obj.getAsJsonObject().get("id").toString());
        String json = gson.toJson(list);
        editor.putString("favIDarr", json);
        editor.remove(key);

        editor.commit();
        dobj.remove(position);
        if(dobj.size()==0)
        {
            ((displayFavourites)ref).onResume();
        }
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, location;
        public ImageView imgview,favview;
        public boolean clicked = false;
        public populateFavouriteAdapter myAdapter;
        public MyViewHolder(View view, populateFavouriteAdapter adapter) {
            super(view);
            imgview = (ImageView) view.findViewById(R.id.imgsrcicon);
            name = (TextView) view.findViewById(R.id.srchName);
            location = (TextView) view.findViewById(R.id.srchlocation);
            favview = (ImageView) view.findViewById(R.id.imgfavicon);
            myAdapter = adapter;
            System.out.println("Inside the contructor of myview holder");
            favview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("Clicked Fav");
                    myAdapter.removeItem(getAdapterPosition());
                    String text = name.getText().toString() + " was removed from favourites";
                    Toast.makeText(myContext, text, Toast.LENGTH_SHORT).show();


                    /*String text = "";
                    String loc = "";
                    if(clicked)
                    {
                        favview.setImageResource(R.drawable.favouritesoutlineblack);
                        text = name.getText().toString() + " was removed from favourites";
                        loc = location.getText().toString();

                        if(myContext instanceof displayresultsAcitivity){
                            ((displayresultsAcitivity)myContext).removeFromFavourties( name.getText().toString(),loc);
                        }
                    }
                    else {
                        favview.setImageResource(R.drawable.favredheart);
                        text = name.getText().toString() + " was added to the favourites";
                        loc = location.getText().toString();

                        if(myContext instanceof displayresultsAcitivity){
                            ((displayresultsAcitivity)myContext).addtoFavourites( name.getText().toString(),loc);
                        }
                    }

                    clicked = !clicked;
                    Toast.makeText(myContext, text, Toast.LENGTH_SHORT).show();
                    */
                }


            });

            // year = (TextView) view.findViewById(R.id.sr2);
        }
        public void bind(final int item) {
            // name.setText(item.name);
            // Picasso.with(itemView.getContext()).load(item.imageUrl).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                    System.out.println("Clicked V");
                    System.out.println("Obj "+dobj.get(item).name);
                    System.out.println("Context is "+myContext);
                    if(myContext instanceof MainActivity){
                        String name = dobj.get(item).name;
                        String add = dobj.get(item).add;
                        String id = dobj.get(item).obj.getAsJsonObject().get("id").getAsString();
                        String data = dobj.get(item).obj.toString();
                        ((MainActivity)myContext).goTodetailsPlaceActivity(name,add,id,data);
                    }

                }
            });
        }

    }

}








