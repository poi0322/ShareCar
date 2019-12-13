package com.example.sharecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;

public class DriverActivity extends Activity implements OnMapReadyCallback {
    private TextView start;
    private TextView destination;
    private TextView start_date;
    private TextView start_time;
    private MapView mapView;
    private Button submit;
    private DatabaseReference rootRef;

    private GoogleMap map;
    private GPSInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        start =findViewById(R.id.driver_start);
        destination =findViewById(R.id.driver_destination);
        start_date =findViewById(R.id.driver_start_date);
        start_time =findViewById(R.id.driver_start_time);
        submit = findViewById(R.id.driver_broad_register);


        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        mapInit();
    }
    void mapInit(){
        map.setMyLocationEnabled(true);
    }
}
