package com.example.firebaseuserloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText fname, lname, mail, pass;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String fnamstr, lnamestr, mailStr, passStr;
    String userId;
    private FirebaseFirestore fstore;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = findViewById(R.id.fnameid);
        lname = findViewById(R.id.lnameid);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        View root = getLayoutInflater().inflate(R.layout.custom_dialog, null);
//            builder.setTitle("Error");
//            builder.setMessage("ALready User Logged in...please try again later");
        builder.setView(root);
        builder.setCancelable(false);

        Button cancel = root.findViewById(R.id.cancel);
        Button signout = root.findViewById(R.id.ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(RegisterActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(RegisterActivity.this, "Signout Successfully", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }


    public void signup(View view) {
        progressDialog.show();
        progressDialog.setMessage("Creating User!!");

        fnamstr=fname.getText().toString().trim();
        lnamestr=lname.getText().toString().trim();
        mailStr=mail.getText().toString();
        passStr=pass.getText().toString();

//        firebaseAuth.createUserWithEmailAndPassword(mailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressDialog.dismiss();
//                if(!task.isSuccessful())
//                {
//                    Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//                Toast.makeText(RegisterActivity.this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//
//
//
//            }
//        });

        firebaseAuth.createUserWithEmailAndPassword(mailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    userId=firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference=fstore.collection("Users").document(userId);
                    Map<String,Object> user=new HashMap<>();
                    user.put("fname",fnamstr);
                    user.put("lname",lnamestr);
                    user.put("email",mailStr);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Tag","User profile created for "+userId);
                        }
                    });
                }
            }
        });
    }

    public void gologin(View view) {
//        firebaseAuth.getCurrentUser().getDisplayName();
        //Toast.makeText(this, "already user logged in ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
finish();
    }
}