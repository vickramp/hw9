package com.places.vickr.placesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void search(View view){
        Intent intent = new Intent(this, search.class);
               String key = ((EditText) findViewById(R.id.editText)).getText().toString();
//                intent.putExtra("com.places.vickr.placesearch.key", key);
                String distance = ((EditText) findViewById(R.id.editText2)).getText().toString();
//                intent.putExtra("com.places.viqckr.placesearch.distance", distance);
                String location = ((EditText) findViewById(R.id.autoCompleteTextView2)).getText().toString();
//                intent.putExtra("com.places.vickr.placesearch.location", location);
                String type = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
//                intent.putExtra("com.places.vickr.placesearch.type", type);
                RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup2);
                int idx = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                RadioButton r = (RadioButton)  radioGroup.getChildAt(idx);
                String loc = r.getText().toString();
//                intent.putExtra("com.places.vickr.placesearch.loc", loc);


        RequestQueue queue = Volley.newRequestQueue(this);
        Intent in=getIntent();
        String str;
        StringBuffer url =new StringBuffer("http://hw8-vpentyal.us-west-2.elasticbeanstalk.com/findPlaces?location=34.0093,-118.2580");
        url.append("&key=");
        url.append(key);
        url.append("&type=");
        url.append(type);
        url.append("&distance=");
        url.append(distance);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        intent.putExtra("com.places.vickr.placesearch.data", response);
                        intent.putExtra("com.places.vickr.placesearch.page", "1");
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error
            }
        });


        queue.add(stringRequest);



    }
}
