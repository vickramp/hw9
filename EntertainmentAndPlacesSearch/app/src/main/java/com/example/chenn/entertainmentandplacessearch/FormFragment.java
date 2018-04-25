
package com.example.chenn.entertainmentandplacessearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText keyword1 = null;
    EditText distance1 = null;
    private ProgressDialog progressBar;

    //EditText customloctext1 = null;
    Spinner category1 = null;
    TextView keywordError = null;
    TextView customLocError = null;
    RadioGroup radioGroup = null;
    RadioButton radiocurrloc = null;
    RadioButton radiocustomloc = null;
    AutoCompleteTextView searchPlace = null;
    public static final String TAG = "AutoCompleteActivity";
    private static final int AUTO_COMP_REQ_CODE = 2;

    private OnFragmentInteractionListener mListener;

    public FormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance(String param1, String param2) {
        FormFragment fragment = new FormFragment();
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

        View rootView =  inflater.inflate(R.layout.fragment_form, container, false);
        keyword1 = (EditText) rootView.findViewById(R.id.keyword);
        distance1 = (EditText) rootView.findViewById(R.id.distance);
        //customloctext1 = (EditText) rootView.findViewById(R.id.customloctext);
        keywordError = (TextView)rootView.findViewById(R.id.keywordError);
        customLocError = (TextView)rootView.findViewById(R.id.customLocError);
        category1 =(Spinner) rootView.findViewById(R.id.category);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup);
        radiocurrloc = (RadioButton) rootView.findViewById(R.id.currlocation);
        radiocustomloc = (RadioButton) rootView.findViewById(R.id.customlocation);
        //keyword = (TextView) rootView.findViewById(R.id.keyword);
        searchPlace = rootView.findViewById(R.id.customloctext);
        CustomAutoCompleteAdapter adapter =  new CustomAutoCompleteAdapter(getActivity().getApplicationContext());

        searchPlace.setAdapter(adapter);
        searchPlace.setOnItemClickListener(onItemClickListener);

        Button buttonOne = (Button) rootView.findViewById(R.id.button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                System.out.println(v.toString());
                String keyword = keyword1.getText().toString();
                String category = category1.getSelectedItem().toString();
                String distance = distance1.getText().toString();
                String customloc = searchPlace.getText().toString();
                int id = radioGroup.getCheckedRadioButtonId();
                //checkForErrors(keyword,customloc,);
               // Log.d("Prinint","pr");
                //Log.d("Val",email);
                int val = 0;
                if(radiocurrloc.getId()==id)
                {
                    val = 1;
                }
                else if(radiocustomloc.getId()==id)
                {
                    val = 2;
                }

                System.out.println("Clicked Search Button" + val  +  "id "+ keyword + " " + category + " "+distance + " " + customloc);
                //getDetails();\
                Double dist = 10*1609.0;
                if(!distance.equals(""))
                {
                    dist = Integer.parseInt(distance)*1609.0;
                }

                if(!checkForErrors(keyword,customloc,val)) {

                    progressBar = new ProgressDialog(getContext());
                    progressBar.setCancelable(true);
                    progressBar.setMessage("Fetching results");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                    if(val==1) {
                        Double lat = ((MainActivity) getActivity()).lat;
                        Double lng = ((MainActivity) getActivity()).lng;
                        getDetails(keyword, lat, lng, category, dist);
                    }
                    if(val==2){
                        getGeoCodeandgoToDetails(keyword,category,dist,customloc);
                    }
                }

            }
        });
        Button clearbtn = (Button) rootView.findViewById(R.id.button2);
        clearbtn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {


            }
        });
        return rootView;
    }

    public void getGeoCodeandgoToDetails(final String keyword, final String category,final Double dist,String customloc)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        try {
            customloc = URLEncoder.encode(customloc, "UTF-8");
            System.out.println(customloc);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Exception");
            e.printStackTrace();
        }
        String str = "address="+customloc;
        String url = "http://chennavamshi-env.us-east-2.elasticbeanstalk.com/geocode?"+str;
        System.out.println("Sending Request "+url );
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response "+response);

                        JsonElement jelement = new JsonParser().parse(response);
                        // JsonElement d =  gson.toJsonTree(response);
                        JsonObject job = jelement.getAsJsonObject();
                        if(job.get("status").getAsString().equals("OK")) {
                            JsonArray jarray = job.getAsJsonArray("results");
                            String lat = jarray.get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsString();
                            //  String lng = jarray.get(0).getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsString();
                            String lng = jarray.get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsString();
                            System.out.println("Got co-ordinates " + lat + " and  " + lng);
                            Double dlng = Double.parseDouble(lng);
                            Double dlat = Double.parseDouble(lat);
                            getDetails(keyword, dlat, dlng, category, dist);

                        }
                        else
                        {
                            System.out.println("Cannot Find the specififed location");
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
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "selected place "
                                    + ((Place)adapterView.
                                    getItemAtPosition(i)).getPlaceText()
                            , Toast.LENGTH_SHORT).show();
                    //do something with the selection
                  //  searchScreen();
                }
            };
  /*  public void searchScreen(){
        Intent i = new Intent();
        i.setClass(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
*/
    public boolean checkForErrors(String kword,String lcldis,int val)
    {
        String nstring = kword.trim();
        String nlcldis = lcldis.trim();
        boolean error = false;
        System.out.println("Values of nstring are " + nstring + " and ncl is " + nlcldis);
        if(nstring==null||nstring.equals(""))
        {
            System.out.println("Inside this");
            keywordError.setVisibility(View.VISIBLE);
            error = true;
        }
        if(val==2 && (nlcldis==null ||nlcldis.equals("")))
        {
            customLocError.setVisibility(View.VISIBLE);
            error = true;
        }
        if(error)
        {
            Context context = getActivity().getApplicationContext();
            CharSequence text = "Please Fix All Fields With Errors";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        return error;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }
    /*  @Override
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
    */

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

    public void getDetails(String keyword,Double lat,Double lng,String cat,Double distance)
    {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
            System.out.println(keyword);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Exception");
            e.printStackTrace();
        }
        //String url ="http://chennavamshi-env.us-east-2.elasticbeanstalk.com/nearbySearch?keyword=usc&lat=34.0266&long=-118.2831&category=default&distance=16094.300000000001";
        String str = "keyword="+keyword+"&lat="+lat+"&long="+lng+"&category="+cat+"&distance="+distance;

        String url = "http://chennavamshi-env.us-east-2.elasticbeanstalk.com/nearbySearch?"+str;
        System.out.println("Sending Request "+url );
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.dismiss();

                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));
                        //System.out.println(response);
                        Intent i = new Intent();
                        i.setClass(getActivity().getApplicationContext(), displayresultsAcitivity.class);
                        i.putExtra("EXTRA_SESSION_ID", response);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Eroor " + error);
                // mTextView.setText("That didn't work!");
                progressBar.dismiss();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}
