package com.example.chenn.entertainmentandplacessearch;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class displayresultsAcitivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private populateSearchAdapter mAdapterPage1;
    private populateSearchAdapter mAdapterPage2;
    private populateSearchAdapter mAdapterPage3;
    private String nextPagetoken1 = null;
    private String nextPagetoken2 = null;
    DetailsObject[] dobjpage1 = null;
    DetailsObject[] dobjpage2 = null;
    DetailsObject[] dobjpage3 = null;

    int currentPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_displayresults_acitivity);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        String s = getIntent().getStringExtra("EXTRA_SESSION_ID");
    // dobj = createDetailsObject(s);
        currentPage = 1;
      //  mAdapter = new populateSearchAdapter(dobj,this );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //getDetails();
        //recyclerView.setAdapter(mAdapter);
        getAllData(s);
    }

    public void getAllData(String response)
    {
        Button next = (Button) findViewById(R.id.nextbtn);
        Button prev = (Button) findViewById(R.id.previousbtn);
        next.setEnabled(false);
        prev.setEnabled(false);

        DetailsObject[] currdob = createDetailsObject(response,1);
        dobjpage1 = currdob;
        mAdapterPage1 = new populateSearchAdapter(currdob,this );
        recyclerView.setAdapter(mAdapterPage1);
        if(nextPagetoken1!=null) {
            final String nextPageToken = nextPagetoken1;
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    getNextPageTokenData(nextPageToken);
                }
            }, 3, TimeUnit.SECONDS);

        }
    }


    public void setAdapter()
    {
        Button next = (Button) findViewById(R.id.nextbtn);
        Button prev = (Button) findViewById(R.id.previousbtn);
        next.setEnabled(false);
        prev.setEnabled(false);
        DetailsObject[] currdob;
        populateSearchAdapter myadap = null;
        if(currentPage==1) {
            currdob = dobjpage1;
            myadap = mAdapterPage1;
            if(dobjpage2!=null)
            {
                next.setEnabled(true);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPage =2;
                        setAdapter();
                    }

                });
            }
        }
        if(currentPage==2) {
            currdob = dobjpage2;
            if(mAdapterPage2==null)
            {
                mAdapterPage2 = new populateSearchAdapter(currdob,this );
            }
            myadap = mAdapterPage2;
            if(dobjpage3!=null)
            {
                next.setEnabled(true);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPage = 3;
                        setAdapter();
                    }

                });
            }
            prev.setEnabled(true);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage = 1;
                    setAdapter();
                }

            });
        }
        if(currentPage==3) {
            currdob = dobjpage3;
            if(mAdapterPage3==null)
            {
                mAdapterPage3 = new populateSearchAdapter(currdob,this );
            }
            myadap = mAdapterPage3;
            prev.setEnabled(true);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage = 2;
                    setAdapter();
                }

            });
        }
        recyclerView.setAdapter(myadap);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //  NavUtils.navigateUpFromSameTask(this);
                // return true;
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    DetailsObject[] createDetailsObject(String response,int pg)
    {


        Gson gson = new GsonBuilder().create();
        JsonElement jelement = new JsonParser().parse(response);
        // JsonElement d =  gson.toJsonTree(response);
        JsonObject job = jelement.getAsJsonObject();
        JsonArray jarray = job.getAsJsonArray("results");
        DetailsObject[] myDataset = new DetailsObject[jarray.size()];

        for(int i=0;i<jarray.size();i++)
        {
            JsonObject tmp = jarray.get(i).getAsJsonObject();
            String name = tmp.get("name").toString();
            myDataset[i] = new DetailsObject();
            myDataset[i].name  = name;
            myDataset[i].obj = tmp;
            myDataset[i].add = tmp.get("vicinity").toString();
            myDataset[i].iconurl = tmp.get("icon").toString();
            System.out.println("name is "+name);
            System.out.println("address is " + tmp.get("vicinity").toString());
        }
        if(jelement.getAsJsonObject().get("next_page_token")!=null) {
            if(pg==1)
            nextPagetoken1 = jelement.getAsJsonObject().get("next_page_token").toString();
            else
                nextPagetoken2 = jelement.getAsJsonObject().get("next_page_token").toString();
        }

    return myDataset;
    }

    public void getNextPageTokenData(String nextPageToken)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        nextPageToken = nextPageToken.substring(1,nextPageToken.length()-1);
        String url = "http://chennavamshi-env.us-east-2.elasticbeanstalk.com/nearbySearchNextPage?pagetoken="+nextPageToken;
        System.out.println(url);
        System.out.println("Sending Request for Next Page Token");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        dobjpage2 = createDetailsObject(response,2);
                        final Button btnnxt = (Button) findViewById(R.id.nextbtn);
                        btnnxt.setEnabled(true);
                        btnnxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentPage = 2;
                                setAdapter();
                            }

                        });
                        if(nextPagetoken2!=null)
                        {
                            final String nextPageToken = nextPagetoken2;
                            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                            executorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    getNextPageTokenData2(nextPageToken);
                                }
                            }, 3, TimeUnit.SECONDS);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Eroor " + error);
                // mTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public void getNextPageTokenData2(String nextPageToken)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        nextPageToken = nextPageToken.substring(1,nextPageToken.length()-1);
        String url = "http://chennavamshi-env.us-east-2.elasticbeanstalk.com/nearbySearchNextPage?pagetoken="+nextPageToken;
        System.out.println(url);
        System.out.println("Sending Request for Next Page Token 2");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        dobjpage3 = createDetailsObject(response,3);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Eroor " + error);
                // mTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }


    public void myFunc(int item)
    {
        Intent i = new Intent();

        String name="",address="";
        if(currentPage==1)
        {
           name =  dobjpage1[item].name;
           address = dobjpage1[item].add;
        }
        if(currentPage==2)
        {
                name =  dobjpage2[item].name;
                address = dobjpage2[item].add;

        }

        if(currentPage==3)
        {
            name =  dobjpage3[item].name;
            address = dobjpage3[item].add;

        }
            i.putExtra("PLACE_NAME", name);
        i.putExtra("PLACE_ADDRESS",address);
        i.setClass(this, detailsofplaceAcitivity.class);
      //  i.putExtra("EXTRA_SESSION_ID", response);

        startActivity(i);

    }


}
