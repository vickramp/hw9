package com.example.chenn.entertainmentandplacessearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link displayFavourites.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link displayFavourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class displayFavourites extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DetailsObject[] obj;
    SharedPreferences sp;
    RecyclerView recyclerView;
    TextView emptyView;
    private OnFragmentInteractionListener mListener;
    public displayFavourites() {
    }


    DetailsObject[] createDetailsObject()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        String fstr = sharedPref.getString("favIDarr",null);
        if(fstr==null)
        {
         fstr = "[]";
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("favIDarr", fstr);
            editor.commit();


        }
        Gson gson = new Gson();

        List<String> list;
        list =  gson.fromJson(fstr, List.class);

        DetailsObject[] myDataset = new DetailsObject[list.size()];

        for(int i=0;i<myDataset.length;i++)
        {
            myDataset[i] = new DetailsObject();
            String s = sharedPref.getString(list.get(i),null);
            JsonElement je = new JsonParser().parse(s);
            JsonObject job = je.getAsJsonObject();
           System.out.println("String s is "+s);
            System.out.println("id is "+job.get("id").getAsString());

            myDataset[i].name  = job.get("name").toString();
            myDataset[i].obj = job;
            myDataset[i].add = job.get("vicinity").toString();
            myDataset[i].iconurl = job.get("icon").toString();

            // myDataset[i].iconurl = tmp.get("icon").toString();
            //System.out.println("name is "+name);
            //System.out.println("address is " + tmp.get("vicinity").toString());
        }

        return myDataset;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Inside On Resume!!!!");
        obj = createDetailsObject();
        populateFavouriteAdapter mAdapter = new populateFavouriteAdapter(obj,getContext(),sp,this);
        recyclerView.setAdapter(mAdapter);
        System.out.println("Obj length is" + obj.length);
        if (obj.length==0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }


    }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment displayFavourites.
         */
    // TODO: Rename and change types and number of parameters
    public static displayFavourites newInstance(String param1, String param2) {
        displayFavourites fragment = new displayFavourites();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display_favourites, container, false);
        String strtext = getArguments().getString("data");
        obj = createDetailsObject();
       recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_fav);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       sp= PreferenceManager.getDefaultSharedPreferences(getContext());
        populateFavouriteAdapter mAdapter = new populateFavouriteAdapter(obj,getContext(),sp,this);
        recyclerView.setAdapter(mAdapter);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        System.out.println("Size is aa " + obj.length);
        if (obj.length==0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

 /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
**/
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
