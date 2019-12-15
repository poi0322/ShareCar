package com.example.sharecar;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecar.DataSet.Write;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.util.Locale;

public class ConformPassengerActivity extends AppCompatActivity {

    private TextView driver;
    private TextView time;
    private LinearLayout map;
    private Button cancel;

    private String driverKey;

    private TMapView tMapView;
    static private TMapPoint startXY;
    static private TMapPoint destXY;
    private TMapMarkerItem startPoint;
    private TMapMarkerItem destPoint;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform_passenger);

        driver = findViewById(R.id.conform_drivername);
        time = findViewById(R.id.dep_time);
        cancel = findViewById(R.id.conform_passeger_button);
        map = findViewById(R.id.select_mapview);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("45ba050a-beba-476a-9c4d-d26365cb895d");
        map.addView(tMapView);



        DatabaseReference u = rootRef.child("users").child(mAuth.getUid()).child("carfull");
        u.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverKey = dataSnapshot.getValue(String.class);
                rootRef.child("driver").child(driverKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Write w = dataSnapshot.getValue(Write.class);
                        driver.setText(w.getName());
                        time.setText(w.getDepartTime());

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        String st = null;
                        String ds = null;
                        try {
                            st = geocoder.getFromLocation(w.getStartLatitude(), w.getStartLongitude(), 1).get(0).getAddressLine(0);
                            ds = geocoder.getFromLocation(w.getDestLatitude(), w.getDestLongitude(), 1).get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startXY = new TMapPoint(w.getStartLatitude(), w.getStartLongitude());
                        destXY = new TMapPoint(w.getDestLatitude(), w.getDestLongitude());


                        startPoint = new TMapMarkerItem();
                        destPoint = new TMapMarkerItem();
                        startPoint.setTMapPoint(startXY); // 마커의 좌표 지정
                        destPoint.setTMapPoint(destXY); // 마커의 좌표 지정


                        tMapView.addMarkerItem("출발점", startPoint);
                        tMapView.addMarkerItem("도착점", destPoint);

                        double latitude = (startXY.getLatitude() + destXY.getLatitude()) / 2;
                        double longitude = (startXY.getLongitude() + destXY.getLongitude()) / 2;

                        //tMapView.setCenterPoint(latitude, longitude);
                        try {
                            searchRoute(startXY, destXY);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            tMapView.setCenterPoint(longitude,latitude);
                            tMapView.setZoom(10);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cancel.setOnClickListener(v -> {
            rootRef.child("driver").child(driverKey).child("carfull").child(mAuth.getUid()).setValue(null);
            rootRef.child("users").child(mAuth.getUid()).child("carfull").setValue(null);

            Toast.makeText(getApplicationContext(),"카풀이 취소되었습니다.",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, ModeActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void searchRoute(TMapPoint start, TMapPoint end) {
        TMapData data = new TMapData();
        data.findPathData(start, end, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tMapView.addTMapPath(path);

                    }
                });
            }
        });
    }
}
