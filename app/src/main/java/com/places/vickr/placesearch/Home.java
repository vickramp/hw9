package com.places.vickr.placesearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void search(View view){
        Intent intent = new Intent(this, search.class);
               String message = ((EditText) findViewById(R.id.editText)).getText().toString();
                intent.putExtra("com.places.vickr.placesearch.key", message);
                message = ((EditText) findViewById(R.id.editText2)).getText().toString();
                intent.putExtra("com.places.viqckr.placesearch.distance", message);
                message = ((EditText) findViewById(R.id.autoCompleteTextView2)).getText().toString();
                intent.putExtra("com.places.vickr.placesearch.location", message);
                message = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
                intent.putExtra("com.places.vickr.placesearch.type", message);
                RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup2);
                int idx = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
                RadioButton r = (RadioButton)  radioGroup.getChildAt(idx);
                message = r.getText().toString();
                intent.putExtra("com.places.vickr.placesearch.loc", message);


        startActivity(intent);
    }
}
