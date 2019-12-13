package com.example.sharecar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SelectCarfullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_carfull);

        findViewById(R.id.select_carfull_drivername);
        findViewById(R.id.select_carfull_start);
        findViewById(R.id.select_carfull_destination);
        findViewById(R.id.select_carfull_date);
        findViewById(R.id.select_carfull_time);
        findViewById(R.id.select_carfull_mapView);
        findViewById(R.id.select_carfull_button);
    }
}
