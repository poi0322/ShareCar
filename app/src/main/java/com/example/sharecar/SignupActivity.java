package com.example.sharecar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharecar.DataSet.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText id;
    private EditText birth;
    private EditText age;
    private EditText password;
    private Button submit;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.signup_name);
        id = findViewById(R.id.signup_id);
        birth = findViewById(R.id.signup_birth);
        password = findViewById(R.id.signup_password);
        submit = findViewById(R.id.signup_submit_button);

        rootRef = FirebaseDatabase.getInstance().getReference();

        submit.setOnClickListener(v -> {
            String email = id.getText().toString();
            String pw = password.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    DatabaseReference userRef = rootRef.child("users");
                    userRef.child(uid).setValue(new User(
                            uid, "", name.getText().toString(),
                            birth.getText().toString(),
                            0, 0));
                    rootRef.child("users").child(uid).child("carfull").setValue(null);

                    Toast.makeText(SignupActivity.this, "회원가입이 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입이 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            });


        });


    }
}
