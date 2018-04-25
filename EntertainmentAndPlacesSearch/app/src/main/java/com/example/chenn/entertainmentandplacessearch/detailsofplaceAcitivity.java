package com.example.chenn.entertainmentandplacessearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
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

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class detailsofplaceAcitivity extends AppCompatActivity {

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
    public boolean clicked = false;
    public String name = "";
    public String address = "";
    public String id = "";
    SharedPreferences sharedPref = null;
    public String jsonobjstr = "";
    public String placeId= "";
    public String lat;
    public String lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsofplace_acitivity);
        String s = getIntent().getStringExtra("PLACE_NAME");

        sharedPref =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;

        name = s.substring(1,s.length()-1);
        s = getIntent().getStringExtra("PLACE_ADDRESS");
        address = s.substring(1,s.length()-1);
        id = getIntent().getStringExtra("PLACE_ID");
        jsonobjstr = getIntent().getStringExtra("PLACE_OBJ");
        JsonElement je = new JsonParser().parse(jsonobjstr);
        JsonObject job = je.getAsJsonObject();
        //System.out.println("String s is "+s);
        placeId = job.get("place_id").getAsString();
        System.out.println("Place id is "+placeId);
        lat = job.get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsString();
        lng = job.get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsString();

        System.out.println("Lat and long are "+lat + " "+lng);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        String fstr = sharedPref.getString("favIDarr",null);

        Gson gson = new Gson();


        System.out.println("id is "+id);
        if(fstr!=null) {
            List<String> list;
            list =  gson.fromJson(fstr, List.class);

            System.out.println(Arrays.toString(list.toArray()));

          //  System.out.println("value is " + Arrays.toString(set.toArray()));
            //System.out.println(" id " + id);

            if (list.contains(id)) {
              //  System.out.println("Inside this beacuse it contained Id!! "+id);
                ImageView favview = (ImageView) findViewById(R.id.placeFavicon);
                clicked = true;
                favview.setImageResource(R.drawable.favourites);
            }
        }

        addFavActionListener();
        addTwitterActionListener();
    }

    public void addTwitterActionListener(){

        final ImageView twitter = (ImageView) findViewById(R.id.Favtwittericon);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked Fav");
                String str = "https://twitter.com/intent/tweet?text=Check out "+name+" at "+address+" \n Website: ";
                Uri uri = Uri.parse(str); // missing 'http://' will cause crashed

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }


        });

    }


    public void addFavActionListener(){

        final ImageView favview = (ImageView) findViewById(R.id.placeFavicon);
        favview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked Fav");
                String text = "";
                if(clicked)
                {
                    favview.setImageResource(R.drawable.favoutlinewhite);
                    text = name+ " was removed from favourites";
                    removeFromFavourties(name,address);
                }
                else {
                    favview.setImageResource(R.drawable.favourites);
                    text = name + " was added to the favourites";
                    addtoFavourites(name,address);
                }

                clicked = !clicked;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }


        });

    }
    public void addtoFavourites(String name, String add)
    {
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
       // Set<String> set = sharedPref.getStringSet("favIDs",null);
       // Set<String> currest;
        String fstr = sharedPref.getString("favIDarr",null);
        Gson gson = new Gson();
        List<String> list;

        if(fstr==null) {
            list = new ArrayList<>();
        }
        else
            list =  gson.fromJson(fstr, List.class);

        //   System.out.println("Printing Array");
        list.add(id);
        String json = gson.toJson(list);
        editor.putString("favIDarr", json);
       editor.putString(id,jsonobjstr);

        // editor.apply();
        editor.commit();
        System.out.println(Arrays.toString(list.toArray()));

    }

    public void removeFromFavourties(String name, String add)
    {
       // int i;
        String key = id;
        SharedPreferences.Editor editor = sharedPref.edit();

        String fstr = sharedPref.getString("favIDarr",null);
        List<String> list;
        Gson gson = new Gson();

        list =  gson.fromJson(fstr, List.class);

        list.remove(key);
        // set.add(obj.obj.getAsJsonObject().get("id").toString());
        String json = gson.toJson(list);
        editor.putString("favIDarr", json);
        editor.remove(id);
       // editor.apply();
         editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_detailsofplace_acitivity, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        System.out.println("hRER");
        super.onBackPressed();
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

        if(id == android.R.id.home) {        //  NavUtils.navigateUpFromSameTask(this);
            // return true;
            onBackPressed();
            this.finish();
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
            View rootView = inflater.inflate(R.layout.fragment_detailsofplace_acitivity, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
            if(position==0){
                Bundle bundle = new Bundle();
                bundle.putString("placeId", placeId);
                InfoTab fragobj = new InfoTab();
                fragobj.setArguments(bundle);
                return fragobj;
            }

            if(position==1) {
                Bundle bundle = new Bundle();
                bundle.putString("placeId", placeId);
                Photos fragobj = new Photos();
                fragobj.setArguments(bundle);
                return fragobj;
            }
            if(position==2)
            {
                Bundle bundle = new Bundle();
                bundle.putString("placeId", placeId);
                bundle.putDouble("lat",Double.parseDouble(lat));
                bundle.putDouble("long",Double.parseDouble(lng));
                bundle.putString("name",name);
                Map fragobj = new Map();
                fragobj.setArguments(bundle);
                return fragobj;

            }
            if(position==3)
            {
                Bundle bundle = new Bundle();
                bundle.putString("placeId", placeId);
                bundle.putString("name",name);
                ReviewsTab fragobj = new ReviewsTab();
                fragobj.setArguments(bundle);
                return fragobj;

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }
}
