package com.places.vickr.placesearch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Home extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_LOCATION = 2;
    double lat;
    double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return;
        } else {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                        lat=location.getLatitude();
                                        lon=location.getLongitude();
                                } else {
                                    inti_loc_updates();


                                }
                            }
                        });
            }


        }




    }


    private void inti_loc_updates() {

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);


    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        // Logic to handle location object
                                    }
                                    else{
                                        LocationRequest mLocationRequest = new LocationRequest();
                                       //Fetch User Location Update


                                    }
                                }
                            });
                    return;
                }


            }

            else {
                // Permission was denied or request was cancelled
            }
        }
    }



    public void sendParameters(View view) {

        Intent intent = new Intent(this, Results.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String keyword = editText.getText().toString();
        intent.putExtra("com.places.vickr.placesearch.Keyword", keyword);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String category=spinner.getSelectedItem().toString();
        intent.putExtra("com.places.vickr.placesearch.Category", category);

        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String distance = editText2.getText().toString();
        intent.putExtra("com.places.vickr.placesearch.distance", distance);

        RadioButton radio=(RadioButton) findViewById(R.id.radioButton5);

        if(radio.isChecked())
        {
            intent.putExtra("com.places.vickr.placesearch.latitude", Double.toString(lat));
            intent.putExtra("com.places.vickr.placesearch.longitude", Double.toString(lon));
        }
        else
        {
            //Get lat and lon from autocplete place search
        }

        startActivity(intent);
    }


}
