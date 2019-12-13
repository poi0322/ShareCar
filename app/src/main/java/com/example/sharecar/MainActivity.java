package com.example.sharecar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sharecar.DataSet.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private Button login;
    private Button singup;


    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_button);
        singup = findViewById(R.id.signup_button);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);


        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        //로그인버튼
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO : 로그인 구현
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();

                                    DatabaseReference userRef = rootRef.child("users").child(uid);
                                    userRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();

                                            User u = dataSnapshot.getValue(User.class);
                                            editor.putString("name", u.getUserName());
                                            editor.putString("birth", u.getUserBirth());
                                            Date date = new Date();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                            int iage = Integer.parseInt(sdf.format(date)) / 10000 -
                                                    Integer.parseInt(u.getUserBirth()) / 10000;
                                            editor.putString("age", String.valueOf(iage));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });

                                    Intent intent = new Intent(MainActivity.this, ModeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //회원가입 버튼
        singup.setOnClickListener(v ->

        {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });


    }
}
