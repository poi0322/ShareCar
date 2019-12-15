package com.example.sharecar;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecar.DataSet.User;
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

public class DriverMypageActivity extends AppCompatActivity {

    private LinearLayout linearLayoutTmap;
    private Button carfullEnd;

    static private TMapPoint startXY;
    static private TMapPoint destXY;
    private TMapMarkerItem startPoint;
    private TMapMarkerItem destPoint;


    private TMapView tMapView;
    private ListView listView;
    private CarfullAdapter listItemAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private Write driverInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_mypage);

        linearLayoutTmap = findViewById(R.id.driver_mypage_mapView);


        listView = findViewById(R.id.driver_mypage_listview);
        carfullEnd = findViewById(R.id.driver_mypage_button);


        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("45ba050a-beba-476a-9c4d-d26365cb895d");
        linearLayoutTmap.addView(tMapView);

        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();

        listSet();
        //카풀완료
        carfullEnd.setOnClickListener(v -> {
            rootRef.child("driver").child(mAuth.getUid()).setValue(null);
            rootRef.child("users").child(mAuth.getUid()).child("carfull").setValue(null);
            Intent intent = new Intent(this, ModeActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener(((parent, view, position, id) -> {
            show(listItemAdapter.getItem(position));
        }));

        listView.setAdapter(listItemAdapter);
    }

    private void listSet(){
        listItemAdapter = new CarfullAdapter();

        DatabaseReference write = rootRef.child("driver").child(mAuth.getUid());
        write.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Write w = dataSnapshot.getValue(Write.class);
                driverInfo = w;
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



        DatabaseReference databaseReference = rootRef.child("driver").child(mAuth.getUid()).child("carfull");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot board : dataSnapshot.getChildren()) {


                    DatabaseReference carfull = rootRef.child("users").child(board.getKey());
                    carfull.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User u = dataSnapshot.getValue(User.class);
                            double latitude = dataSnapshot.child("Latitude").getValue(double.class);
                            double longitude = dataSnapshot.child("Longitude").getValue(double.class);
                            String n = dataSnapshot.child("userName").getValue(String.class);

                            TMapPoint pass = new TMapPoint(latitude, longitude);
                            TMapMarkerItem passinger = new TMapMarkerItem();
                            passinger.setTMapPoint(pass);
                            tMapView.addMarkerItem(n,passinger);

                            listItemAdapter.addItem(u);
                            listView.setAdapter(listItemAdapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setAdapter(listItemAdapter);

    }

    private void show(User u){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카풀신청");
        builder.setMessage("카풀신청을 취소하겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = rootRef.child("driver").child(mAuth.getUid()).child("carfull").child(u.getUid());
                        databaseReference.setValue(null);

                        DatabaseReference user = rootRef.child("users").child(mAuth.getUid()).child("carfull");
                        user.setValue(null);

                        Toast.makeText(getApplicationContext(),"카풀이 취소되었습니다.",Toast.LENGTH_LONG).show();
                        listSet();
                        listItemAdapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
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
