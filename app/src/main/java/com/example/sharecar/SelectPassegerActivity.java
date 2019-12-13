package com.example.sharecar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SelectPassegerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_passeger);

        findViewById(R.id.select_passeger_listview);
    }
}
