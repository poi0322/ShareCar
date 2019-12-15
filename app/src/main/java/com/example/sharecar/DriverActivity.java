package com.example.sharecar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sharecar.DataSet.Write;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DriverActivity extends Activity {
    private EditText start;
    private EditText destination;
    private Button setStart;
    private Button setDest;
    private Button find;
    private Button setTime;
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
        setContentView(R.layout.activity_driver);

        //bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_marker);

        start = findViewById(R.id.driver_start);
        destination = findViewById(R.id.driver_destination);
        setStart = findViewById(R.id.set_start);
        setDest = findViewById(R.id.set_dest);
        find = findViewById(R.id.find);
        setTime = findViewById(R.id.set_time);
        submit = findViewById(R.id.driver_broad_register);
        linearLayoutTmap = findViewById(R.id.driver_mapView);


        //T맵 초기화
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("45ba050a-beba-476a-9c4d-d26365cb895d");

        destPoint = new TMapMarkerItem();

        linearLayoutTmap.addView(tMapView);


//        tMapView.setOnDisableScrollWithZoomLevelListener((zoom, centerPoint) -> {
//            position = centerPoint;
//            System.out.println(position);
//
//        });

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                nowPoint = new TMapMarkerItem();
                try {
//                    Toast.makeText(DriverActivity.this, tMapPoint.getLatitude() + "", Toast.LENGTH_SHORT).show();
                    position = tMapPoint;
                    nowPoint.setTMapPoint(position);
                    tMapView.addMarkerItem("현재위치", nowPoint);
                } catch (NullPointerException e) {
                    Toast.makeText(DriverActivity.this, "잠시후에 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        setStart.setOnClickListener(v -> {
            startPoint = new TMapMarkerItem();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                startXY = position;

                start.setText(geocoder.getFromLocation(startXY.getLatitude(), startXY.getLongitude(), 1).get(0).getAddressLine(0));
                //startPoint.setIcon(bitmap);
                startPoint.setTMapPoint(startXY); // 마커의 좌표 지정
                tMapView.addMarkerItem("출발점", startPoint);


                //m.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address)));
            } catch (IOException | IndexOutOfBoundsException ioException) {
                //네트워크 문제
                Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            } catch (IllegalArgumentException | NullPointerException e) {
                Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            }
        });


        setDest.setOnClickListener(v -> {
            destPoint = new TMapMarkerItem();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                destXY = position;

                destination.setText(geocoder.getFromLocation(destXY.getLatitude(), destXY.getLongitude(), 1).get(0).getAddressLine(0));
                //destPoint.setIcon(bitmap);
                destPoint.setTMapPoint(destXY); // 마커의 좌표 지정
                tMapView.addMarkerItem("도착점", destPoint);


                //m.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address)));
            } catch (IOException | IndexOutOfBoundsException ioException) {
                //네트워크 문제
                Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            } catch (IllegalArgumentException | NullPointerException e) {
                Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            }
        });

        find.setOnClickListener(v -> {
            try {
                searchRoute(startXY, destXY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        setTime.setOnClickListener(v -> {
            Calendar mCalendar = Calendar.getInstance();
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int min = mCalendar.get(Calendar.MINUTE);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    TextView time = findViewById(R.id.time_view);

                    time.setText(Integer.toString(year) +Integer.toString(month) + Integer.toString(dayOfMonth));
                }
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));


            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    TextView time = findViewById(R.id.time_view);
                    String tmp = (String) time.getText();
                    time.setText(tmp + String.format("%02d%02d",hourOfDay,minute));
                }
            }, hour, min, false);

            timePickerDialog.show();
            datePickerDialog.show();
        });


        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        submitOnClickListener();

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

    private void submitOnClickListener() {
        submit.setOnClickListener((v) -> {
            Date date = new Date(System.currentTimeMillis());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");


            SharedPreferences pref = this.getSharedPreferences("user", MODE_PRIVATE);

            String name =pref.getString("name", "");
            String age =pref.getString("age", "");

            FirebaseUser user = mAuth.getCurrentUser();
            String uid = user.getUid();
            DatabaseReference usersRef = rootRef.child("driver").child(uid);
            TextView time = findViewById(R.id.time_view);
            usersRef.setValue(new Write(
                    uid,
                    name,
                    age,
                    startXY.getLatitude(),
                    destXY.getLatitude(),
                    startXY.getLongitude(),
                    destXY.getLongitude(),
                    sdf.format(date),
                    (String) time.getText()
            ));

            Intent intent = new Intent(this, DriverMypageActivity.class);
            startActivity(intent);

        });
    }

}
