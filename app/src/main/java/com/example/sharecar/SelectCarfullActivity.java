package com.example.sharecar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecar.DataSet.Write;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class SelectCarfullActivity extends AppCompatActivity {


    private TextView start;
    private TextView destination;
    private TextView name;
    private TextView time;
    private LinearLayout linearLayoutTmap;
    private Button submit;


    private TMapView tMapView;

    private GPSInfo gps;

    private TMapMarkerItem startPoint;
    private TMapMarkerItem destPoint;
    private TMapMarkerItem nowPoint;

    static private TMapPoint position;
    static private TMapPoint startXY;
    static private TMapPoint destXY;

    private Bitmap bitmap;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_carfull);

        Intent intent = getIntent();
        final String key = intent.getStringExtra("key");
        gps = new GPSInfo(this);

        name = findViewById(R.id.select_carfull_drivername);
        start = findViewById(R.id.select_carfull_start);
        destination = findViewById(R.id.select_carfull_destination);
        time = findViewById(R.id.select_carfull_time);
        linearLayoutTmap = findViewById(R.id.select_mapview);
        submit = findViewById(R.id.select_carfull_button);

        //T맵 초기화
        tMapView = new TMapView(this);
        //tMapView.setCenterPoint(gps.getLongitude(),gps.getLatitude());
        tMapView.setSKTMapApiKey("45ba050a-beba-476a-9c4d-d26365cb895d");


        linearLayoutTmap.addView(tMapView);
        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = rootRef.child("driver").child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Write w = dataSnapshot.getValue(Write.class);
                name.setText(w.getName());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String st = null;
                String ds = null;
                try {
                    st = geocoder.getFromLocation(w.getStartLatitude(), w.getStartLongitude(), 1).get(0).getAddressLine(0);
                    ds = geocoder.getFromLocation(w.getDestLatitude(), w.getDestLongitude(), 1).get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                start.setText(st);
                destination.setText(ds);

                startXY = new TMapPoint(w.getStartLatitude(), w.getStartLongitude());
                destXY = new TMapPoint(w.getDestLatitude(), w.getDestLongitude());


                startPoint = new TMapMarkerItem();
                destPoint = new TMapMarkerItem();
                startPoint.setTMapPoint(startXY); // 마커의 좌표 지정
                destPoint.setTMapPoint(destXY); // 마커의 좌표 지정
                System.out.println("안"+startXY);


                tMapView.addMarkerItem("출발점", startPoint);
                tMapView.addMarkerItem("도착점", destPoint);
                time.setText(w.getDepartTime());

                System.out.println("밖"+startXY);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //tMapView.setCenterPoint(gps.getLatitude(),gps.getLongitude());

        submit.setOnClickListener(v->{
            FirebaseUser user = mAuth.getCurrentUser();
            rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference carfull = rootRef.child("driver").child(key).child("carfull").child(user.getUid());
            carfull.setValue("on");

            DatabaseReference pos = rootRef.child("users").child(user.getUid());
            pos.child("Latitude").setValue(gps.getLatitude());
            pos.child("Longitude").setValue(gps.getLongitude());

            rootRef.child("users").child(user.getUid()).child("carfull").setValue(key);


            Intent intent1 = new Intent(this,ModeActivity.class);
            startActivity(intent1);
            finish();


        });

    }


}
