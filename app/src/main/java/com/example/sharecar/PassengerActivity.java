package com.example.sharecar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PassengerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        findViewById(R.id.passenger_date);
        findViewById(R.id.passenger_listview);

    }
}
