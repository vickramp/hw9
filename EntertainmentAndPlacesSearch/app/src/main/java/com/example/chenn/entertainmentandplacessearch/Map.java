package com.example.chenn.entertainmentandplacessearch;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Map.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  GoogleMap gmap;
    private  Object ob;
    private  String did=null,pid=null,dn,sn;
    private  double lat,lng;
    private Spinner sp;
    private  AutoCompleteTextView searchPlace=null;
    private OnFragmentInteractionListener mListener;

    public Map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Map.
     */
    // TODO: Rename and change types and number of parameters
    public static Map newInstance(String param1, String param2) {
        Map fragment = new Map();
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

        ob=this;
        //get lat lng and placeid from object
        did = getArguments().getString("placeId");
        lat = getArguments().getDouble("lat");
        lng = getArguments().getDouble("long");
        sn = getArguments().getString("name");

        System.out.println("Inside Map "+lat +" and long " + lng);

       // lat=34.0224;
       // lng=-118.2851;

        //did="ChIJ7aVxnOTHwoARxKIntFtakKo";
        //sn="university of southern California";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback)ob);
        searchPlace = rootView.findViewById(R.id.autoCompleteTextView2);
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
                    dn=((Place)adapterView.
                            getItemAtPosition(i)).getPlaceText();
                    getDir(pid, sp.getSelectedItem().toString());

                }
            };
    private  void getDir(String pid,String type){
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        catch (Exception e){

        }
        gmap.clear();
        LatLng loc = new LatLng(lat, lng);
        gmap.addMarker(new MarkerOptions().position(loc).title(sn));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                            Toast.makeText(getContext(), "No Directions Found From Given Location",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        Gson gson = new Gson();
                        data []dt = gson.fromJson(response, data[].class);
                        gmap.clear();
                        LatLng loc1 = new LatLng(dt[dt.length-1].end_location.lat,dt[dt.length-1].end_location.lng);
                        gmap.addMarker(new MarkerOptions().position(loc1).title(sn));

                        LatLng loc = new LatLng(dt[0].start_location.lat,dt[0].start_location.lng);
                        gmap.addMarker(new MarkerOptions().position(loc).title(dn));
                        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));

                        for(int i=0;i<dt.length;i++) {
                            gmap.addPolyline(new PolylineOptions()
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap=googleMap;
        LatLng loc = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(loc).title(sn));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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