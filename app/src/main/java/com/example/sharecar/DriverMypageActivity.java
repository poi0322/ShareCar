package com.example.sharecar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DriverMypageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_mypage);

        findViewById(R.id.driver_mypage_mapView);
        findViewById(R.id.driver_mypage_listview);
        findViewById(R.id.driver_mypage_button);
    }
}
