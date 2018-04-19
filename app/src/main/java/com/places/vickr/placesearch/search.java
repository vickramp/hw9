package com.places.vickr.placesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class search extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent in=getIntent();
        String str;
        StringBuffer url =new StringBuffer("http://hw8-vpentyal.us-west-2.elasticbeanstalk.com/findPlaces?location=34.0093,-118.2580");
        url.append("&key=");
        url.append(in.getStringExtra("com.places.vickr.placesearch.key"));
        url.append("&type=");
        url.append(in.getStringExtra("com.places.vickr.placesearch.type"));
        url.append("&distance=");
        url.append(in.getStringExtra("com.places.vickr.placesearch.distance"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Gson gson = new Gson();
                        data dt = gson.fromJson(response, data.class);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        //error
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
 class data{
    String next;
    details data[];
}
class details{
    String id,name,icon,address;
}