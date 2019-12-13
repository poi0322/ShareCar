package com.example.sharecar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ConformPassengerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform_passenger);

        findViewById(R.id.conform_passegername);
        findViewById(R.id.conform_passeger_place);
        findViewById(R.id.conform_passeger_mapView);
        findViewById(R.id.conform_passeger_button);


    }
}
