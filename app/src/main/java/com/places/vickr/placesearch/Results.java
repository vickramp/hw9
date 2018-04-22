package com.places.vickr.placesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("com.places.vickr.placesearch.Keyword");
        String category = intent.getStringExtra("com.places.vickr.placesearch.Category");
        String distance = intent.getStringExtra("com.places.vickr.placesearch.distance");
        String latitude = intent.getStringExtra("com.places.vickr.placesearch.latitude");
        String longitude = intent.getStringExtra("com.places.vickr.placesearch.longitude");



    }
}
