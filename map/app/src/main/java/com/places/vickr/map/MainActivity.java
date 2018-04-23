package com.places.vickr.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback   {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static GoogleMap gmap;
    private static Object ob;
    private static String did;
    private static double lat,lng;
    private static AutoCompleteTextView searchPlace=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ob=this;
        //get lat lng from object
        lat=-33.852;
        lng=151.211;
        did="ChIJ7aVxnOTHwoARxKIntFtakKo";

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        gmap=googleMap;
        LatLng loc = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(loc));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static Spinner sp=null;
        private static String pid=null;
        private static Context context;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()  .findFragmentById(R.id.map);
            mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback)ob);


            searchPlace = rootView.findViewById(R.id.autoCompleteTextView2);
            context=getContext();
            CustomAutoCompleteAdapter adapter =  new CustomAutoCompleteAdapter(getContext() );
            sp=rootView.findViewById(R.id.spinner);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(pid==null||pid.length()==0)
                        return;
                    getDir(pid, sp.getSelectedItem().toString());
                }
            });
            searchPlace.setAdapter(adapter);
            searchPlace.setOnItemClickListener(onItemClickListener);
            return rootView;
        }



        private AdapterView.OnItemClickListener onItemClickListener =
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    pid=((Place)adapterView.
                            getItemAtPosition(i)).getPlaceId();

                    getDir(pid, sp.getSelectedItem().toString());

                    }
                };
        private static void getDir(String pid,String type){
            MainActivity.gmap.clear();
            LatLng loc = new LatLng(MainActivity.lat, MainActivity.lng);
            MainActivity.gmap.addMarker(new MarkerOptions().position(loc));
            MainActivity.gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

            RequestQueue queue = Volley.newRequestQueue(context);
            StringBuffer url =new StringBuffer("http://hw9-env.6bqvq4tt8g.us-west-2.elasticbeanstalk.com/waypoints?");
            url.append("&start=");
            url.append(pid);
            url.append("&end=");
            url.append(did);
            url.append("&type=");
            url.append(type);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("[]")) {
                                Toast.makeText(context, "No Directions Found From Given Location",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            Gson gson = new Gson();
                            data []dt = gson.fromJson(response, data[].class);

                            LatLng loc = new LatLng(dt[dt.length-1].end_location.lat,dt[dt.length-1].end_location.lng);
                            MainActivity.gmap.addMarker(new MarkerOptions().position(loc));
                            MainActivity.gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));

                            for(int i=0;i<dt.length;i++) {
                                MainActivity.gmap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(dt[i].start_location.lat, dt[i].start_location.lng), new LatLng(dt[i].end_location.lat, dt[i].end_location.lng))
                                        .color(Color.BLUE));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    VolleyError err=error;

                    //error
                }
            });
            queue.add(stringRequest);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}

 class data {
        end end_location ;
        start  start_location ;
         class end {
             double lat, lng;
         }
         class start {
             double lat, lng;
         }
     
 }