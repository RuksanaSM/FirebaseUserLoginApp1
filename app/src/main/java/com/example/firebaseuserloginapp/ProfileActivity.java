package com.example.firebaseuserloginapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {
  private FirebaseAuth mAuth;
  private FirebaseFirestore db;
  TextView firstname,lastname,email;
  String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstname=findViewById(R.id.displayfname);
        lastname=findViewById(R.id.displaylname);
        email=findViewById(R.id.displaymail);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        userId=mAuth.getCurrentUser().getUid();

        DocumentReference documentReference=db.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                firstname.setText(value.getString("fname"));
                lastname.setText(value.getString("lname"));
                email.setText(value.getString("email"));
            }
        });

    }

    public void signout(View view) {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
        Toast.makeText(this, "Logout Successfully...", Toast.LENGTH_SHORT).show();
    }
}