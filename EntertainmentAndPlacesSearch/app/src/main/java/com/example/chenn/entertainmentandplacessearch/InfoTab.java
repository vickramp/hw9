package com.example.chenn.entertainmentandplacessearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_PARAM1 = "param1";
    private String place_id = "";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public View view;
    private OnFragmentInteractionListener mListener;

    public InfoTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoTab.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoTab newInstance(String param1, String param2) {
        InfoTab fragment = new InfoTab();
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
        view=inflater.inflate(R.layout.fragment_info_tab, container, false);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringBuffer url =new StringBuffer("http://hw9-env.6bqvq4tt8g.us-west-2.elasticbeanstalk.com/getdetails?placeid=");
        url.append(place_id);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Map jsonJavaRootObject = new Gson().fromJson(response, Map.class);
                        displayInfo(jsonJavaRootObject);

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

    public void displayInfo(Map root)
    {

        if(root.containsKey("formatted_address"))
        {
            TextView address_val = view.findViewById(R.id.address_val);
            String address_value=root.get("formatted_address").toString();
            address_val.setText(address_value);
        }
        else
        {
            view.findViewById(R.id.ADD).setVisibility(View.GONE);
        }


        if(root.containsKey("international_phone_number"))
        {
            TextView address_val = view.findViewById(R.id.number_val);
            String address_value=root.get("international_phone_number").toString();
            address_val.setText(address_value);
            Linkify.addLinks(address_val, Linkify.PHONE_NUMBERS);
            address_val.setLinksClickable(true);
        }
        else
        {
            view.findViewById(R.id.NUM).setVisibility(View.GONE);
        }


        if(root.containsKey("price_level"))
        {
            TextView address_val = view.findViewById(R.id.price_val);
            double num=Double.parseDouble(root.get("price_level").toString());
            String address_value="";
            for(int j=0;j<(int)num;j++)
            {
                address_value=address_value.concat("$");
            }

            address_val.setText(address_value);

        }
        else
        {
            view.findViewById(R.id.PRI).setVisibility(View.GONE);
        }

        if(root.containsKey("rating"))
        {
            double num=Double.parseDouble(root.get("rating").toString());
            RatingBar rb=view.findViewById(R.id.ratingBar);
            rb.setNumStars(5);
            rb.setRating((float)num);
        }
        else
        {
            view.findViewById(R.id.RAT).setVisibility(View.GONE);
        }

        if(root.containsKey("url"))
        {
            TextView address_val = view.findViewById(R.id.google_val);
            String address_value=root.get("url").toString();
            address_val.setText(address_value);
            Linkify.addLinks(address_val, Linkify.WEB_URLS);
            address_val.setLinksClickable(true);
        }
        else
        {
            view.findViewById(R.id.GOO).setVisibility(View.GONE);
        }

        if(root.containsKey("website"))
        {
            TextView address_val = view.findViewById(R.id.web_val);
            String address_value=root.get("website").toString();
            address_val.setText(address_value);
            Linkify.addLinks(address_val, Linkify.WEB_URLS);
            address_val.setLinksClickable(true);
        }
        else
        {
            view.findViewById(R.id.WEB).setVisibility(View.GONE);
        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
