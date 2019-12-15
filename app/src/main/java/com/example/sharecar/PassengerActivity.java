package com.example.sharecar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sharecar.DataSet.Write;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PassengerActivity extends Activity {
    private TextView date;
    private      ListView listView;

    private ListItemAdapter listItemAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        listItemAdapter = new ListItemAdapter();

        Calendar mCalendar = Calendar.getInstance();
         listView = findViewById(R.id.passenger_listview);






        date = findViewById(R.id.passenger_date);
        findViewById(R.id.passenger_listview);

        date.setText(Integer.toString(mCalendar.get(Calendar.YEAR))
                + Integer.toString(mCalendar.get(Calendar.MONTH))
                + Integer.toString(mCalendar.get(Calendar.DAY_OF_MONTH)));

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference databaseReference = rootRef.child("driver");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot board : dataSnapshot.getChildren()){
                    System.out.println(board.getKey());

                    Write w = board.getValue(Write.class);
                    //System.out.println(d.getKey()+""+d.getStart()+""+d.getDest()+""+d.getStart());
                    listItemAdapter.addItem(w);
                    listView.setAdapter(listItemAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(((parent, view, position, id) -> {
            Intent intent = new Intent(this, SelectCarfullActivity.class);
            intent.putExtra("key",listItemAdapter.getItem(position).getKey());
            startActivity(intent);
        }));

        listView.setAdapter(listItemAdapter);

    }
}
