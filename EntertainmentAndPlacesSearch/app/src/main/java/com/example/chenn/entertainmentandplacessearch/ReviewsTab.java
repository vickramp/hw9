package com.example.chenn.entertainmentandplacessearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ReviewsTab extends Fragment {

    private String place_id = "";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public View view;
    public RecyclerView recyclerView;
    private PopulateReviewsAdapter mAdapter;

    public boolean isGoogleRevEmpty=false;
    public boolean isYelpRevEmpty=false;

    public Spinner type;
    public Spinner order;

    public String type_string="Google Reviews";
    public String order_string="Default Order";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ReviewsDetails [] google_rev_obj;
    ReviewsDetails[] yelp_rev_obj;

    ReviewsDetails [] google_rev_obj_sorted;
    ReviewsDetails[] yelp_rev_obj_sorted;



    private OnFragmentInteractionListener mListener;

    public ReviewsTab() {
    }


    public static ReviewsTab newInstance(String param1, String param2) {
        ReviewsTab fragment = new ReviewsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        place_id = getArguments().getString("placeId");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_reviews_tab, container, false);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringBuffer url =new StringBuffer("http://hw9-env.6bqvq4tt8g.us-west-2.elasticbeanstalk.com/getdetails?placeid=");
        url.append(place_id);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Map jsonJavaRootObject = new Gson().fromJson(response, Map.class);
                        fetchYelpReviews(jsonJavaRootObject);
                        if(jsonJavaRootObject.containsKey("reviews"))
                        {
                            parseGoogledata(jsonJavaRootObject);
                            mAdapter = new PopulateReviewsAdapter(google_rev_obj);
                            recyclerView.setAdapter(mAdapter);

                        }
                        else
                        {
                            isGoogleRevEmpty=true;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        queue.add(stringRequest);

        return view;
    }


    public void fetchYelpReviews(Map place_details)
    {
        String name=place_details.get("name").toString();
        String fa=place_details.get("formatted_address").toString();
        String address="";
        String [] res=fa.split(",");
        int res_len=res.length;

        if(res_len==5)
        {
            address=res[0]+res[1];
        }
        else {
            address=res[0];
        }

        String city=res[res_len-3].substring(0,res[res_len-3].length());
        String state=res[res_len-2].substring(1,3);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringBuffer url =new StringBuffer("http://hw9-env.6bqvq4tt8g.us-west-2.elasticbeanstalk.com/yelp?");

        try {
            name = URLEncoder.encode(name, "UTF-8");
            address = URLEncoder.encode(address, "UTF-8");
            city = URLEncoder.encode(city,"UTF-8");
            state = URLEncoder.encode(state,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("URL encode error");
            e.printStackTrace();
        }
        url.append("name="+ name);
        url.append("&address="+address);
        url.append("&city="+city);
        url.append("&state="+state);
        url.append("&country=US");
        url.append("&lat=0");
        url.append("&lon=0");
        System.out.println(url);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map jsonJavaRootObject = new Gson().fromJson(response, Map.class);
                        if(jsonJavaRootObject.containsKey("reviews"))
                        {
                            parseYelpdata(jsonJavaRootObject);
                            setListners();
                        }
                        else
                        {
                            isYelpRevEmpty=true;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        queue.add(stringRequest);


    }


    public void parseGoogledata(Map root)
    {

        ArrayList data= (ArrayList) root.get("reviews");

        google_rev_obj=new ReviewsDetails[data.size()];
        google_rev_obj_sorted=new ReviewsDetails[data.size()];

        for(int i=0;i<data.size();i++) {
            Map demo = (Map) data.get(i);

            String name = demo.get("author_name").toString();

            long time_val = Double.valueOf(demo.get("time").toString()).longValue();
            time_val = time_val * 1000;
            Timestamp stamp = new Timestamp(time_val);
            Date date = new Date(stamp.getTime());
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

            float rating=0;
            if(demo.containsKey("rating"))
            { rating = (float) Double.valueOf(demo.get("rating").toString()).longValue();}

            String text="";
            if(demo.containsKey("text"))
            { text = demo.get("text").toString();}

            String img_url="";
            if(demo.containsKey("profile_photo_url"))
            {img_url = demo.get("profile_photo_url").toString();}

            String author_url="";
            if(demo.containsKey("author_url"))
            {author_url = demo.get("author_url").toString();}

            ReviewsDetails RD=new ReviewsDetails(name,time,author_url,img_url,text,time_val,rating);
            google_rev_obj[i]=RD;
            google_rev_obj_sorted[i]=RD;
        }

    }



    public void parseYelpdata(Map root)
    {

        ArrayList data= (ArrayList) root.get("reviews");

        yelp_rev_obj=new ReviewsDetails[data.size()];
        yelp_rev_obj_sorted=new ReviewsDetails[data.size()];

        for(int i=0;i<data.size();i++) {
            Map demo = (Map) data.get(i);

            Map user = (Map) demo.get("user");
            String name=user.get("name").toString();

            String time=demo.get("time_created").toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = null;
            try {
                parsedDate = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            long time_val=timestamp.getTime();


            float rating=0;
            if(demo.containsKey("rating"))
            {
                if(demo.get("rating")!=null)
                {rating = (float) Double.valueOf(demo.get("rating").toString()).longValue();}
            }

            String text="";
            if(demo.containsKey("text"))
            {
                if(demo.get("text")!=null)
                {text = demo.get("text").toString();}
            }

            String img_url="";
            if(user.containsKey("image_url"))
            {
                if(user.get("image_url")!=null)
                {img_url = user.get("image_url").toString();}
            }

            String author_url="";
            if(demo.containsKey("url"))
            {
                author_url = demo.get("url").toString();
            }

            author_url = demo.get("url").toString();
            ReviewsDetails RD=new ReviewsDetails(name,time,author_url,img_url,text,time_val,rating);
            yelp_rev_obj[i]=RD;
            yelp_rev_obj_sorted[i]=RD;

        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rcv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    public void setListners()
    {
        type=view.findViewById(R.id.spinner);
        order=view.findViewById(R.id.spinner2);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                type_string=type.getSelectedItem().toString();
                displayReviews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                order_string=order.getSelectedItem().toString();
                displayReviews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }


    public void displayReviews()
    {
        String key=type_string+" "+order_string;
        if(isGoogleRevEmpty||isYelpRevEmpty)
        {
            //TODO HANDLE EDGE CASE
            return;
        }

        switch(key)
        {
            case "Google Reviews Default Order":
            {
                System.out.println("1");
                mAdapter = new PopulateReviewsAdapter(google_rev_obj);
                recyclerView.setAdapter(mAdapter);

            }break;


            case "Google Reviews Highest Rating":
            {
                System.out.println("2");
                float [][] order=new float[google_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=google_rev_obj[i].rating;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<float[]>() {
                    public int compare(float[] a, float [] b) {
                        return Double.compare(a[1], b[1]);
                    }
                });

                for(int i=order.length-1;i>=0;i--) {
                    google_rev_obj_sorted[order.length-1-i] = google_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(google_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);

            }break;


            case "Google Reviews Lowest Rating":
            {
                System.out.println("3");

                float [][] order=new float[google_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=google_rev_obj[i].rating;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<float[]>() {
                    public int compare(float[] a, float [] b) {
                        return Double.compare(a[1], b[1]);
                    }
                });

                for(int i=0;i<order.length;i++) {
                    google_rev_obj_sorted[i] = google_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(google_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);

            }break;


            case "Google Reviews Most Recent":
            {
                System.out.println("4");
                long [][] order=new long[google_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=google_rev_obj[i].time_val;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<long[]>() {
                    public int compare(long[] a, long [] b) {
                        return Long.compare(a[1], b[1]);
                    }
                });

                for(int i=order.length-1;i>=0;i--) {
                    google_rev_obj_sorted[order.length-1-i] = google_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(google_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);



            }break;


            case "Google Reviews Least Recent":
            {
                System.out.println("5");
                long [][] order=new long[google_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=google_rev_obj[i].time_val;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<long[]>() {
                    public int compare(long[] a, long [] b) {
                        return Long.compare(a[1], b[1]);
                    }
                });

                for(int i=0;i<order.length;i++) {
                    google_rev_obj_sorted[i] = google_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(google_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);

            }break;


            case "Yelp Reviews Default Order":
            {
                System.out.println("6");
                mAdapter = new PopulateReviewsAdapter(yelp_rev_obj);
                recyclerView.setAdapter(mAdapter);

            }break;


            case "Yelp Reviews Highest Rating":
            {
                System.out.println("7");
                float [][] order=new float[yelp_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=yelp_rev_obj[i].rating;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<float[]>() {
                    public int compare(float[] a, float [] b) {
                        return Double.compare(a[1], b[1]);
                    }
                });

                for(int i=order.length-1;i>=0;i--) {
                    yelp_rev_obj_sorted[order.length-1-i] = yelp_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(yelp_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);


            }break;


            case "Yelp Reviews Lowest Rating":
            {
                System.out.println("8");

                float [][] order=new float[yelp_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=yelp_rev_obj[i].rating;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<float[]>() {
                    public int compare(float[] a, float [] b) {
                        return Double.compare(a[1], b[1]);
                    }
                });

                for(int i=0;i<order.length;i++) {
                    yelp_rev_obj_sorted[i] = yelp_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(yelp_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);


            }break;


            case "Yelp Reviews Most Recent":
            {
                System.out.println("9");
                long [][] order=new long[yelp_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=yelp_rev_obj[i].time_val;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<long[]>() {
                    public int compare(long[] a, long [] b) {
                        return Long.compare(a[1], b[1]);
                    }
                });

                for(int i=order.length-1;i>=0;i--) {
                    yelp_rev_obj_sorted[order.length-1-i] = yelp_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(yelp_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);

            }break;


            case "Yelp Reviews Least Recent":
            {
                System.out.println("10");
                long [][] order=new long[yelp_rev_obj.length][2];
                for(int i=0;i<order.length;i++)
                {
                    order[i][1]=yelp_rev_obj[i].time_val;
                    order[i][0]=i;
                }

                java.util.Arrays.sort(order, new java.util.Comparator<long[]>() {
                    public int compare(long[] a, long [] b) {
                        return Long.compare(a[1], b[1]);
                    }
                });

                for(int i=0;i<order.length;i++) {
                    yelp_rev_obj_sorted[i] = yelp_rev_obj[(int)order[i][0]];
                }

                mAdapter = new PopulateReviewsAdapter(yelp_rev_obj_sorted);
                recyclerView.setAdapter(mAdapter);
            }break;


            default:System.out.println("11");


        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
