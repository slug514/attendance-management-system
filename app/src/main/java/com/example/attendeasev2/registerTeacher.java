package com.example.attendeasev2;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerTeacher extends AppCompatActivity {

    private EditText editTextName,editTextType,editTextClass;
    private TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);
        mAuth= FirebaseAuth.getInstance();
        editTextName=findViewById(R.id.Fname);
        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        editTextType=findViewById(R.id.usub);
        editTextClass=findViewById(R.id.classname);
        buttonReg= findViewById(R.id.btn_register);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.loginNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email,password;
                String fullName=editTextName.getText().toString();
                String Subject=editTextType.getText().toString().toLowerCase();
                email=String.valueOf(editTextEmail.getText());
                password=String.valueOf(editTextPassword.getText());
                String classdet=String.valueOf(editTextClass.getText());
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(registerTeacher.this,"Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(registerTeacher.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(fullName))
                {
                    Toast.makeText(registerTeacher.this,"Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Subject))
                {
                    Toast.makeText(registerTeacher.this,"Enter subject", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(classdet))
                {
                    Toast.makeText(registerTeacher.this,"Enter class details", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(registerTeacher.this, "Account Created successfully.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser firebaseUser= mAuth.getCurrentUser();
                                    FirebaseFirestore db;
                                    db=FirebaseFirestore.getInstance();
                                    FirebaseUser ud=FirebaseAuth.getInstance().getCurrentUser();
                                    String userstring=ud.getEmail();
                                    Map<String, Object> udetails = new HashMap<>();
                                    udetails.put("name", fullName);
                                    udetails.put("subject", Subject);
                                    udetails.put("email",ud.getEmail());
                                    udetails.put("class",classdet);
                                    udetails.put("type","teacher");
                                    udetails.put("lrno",0);
                                    //udetails.put("Teacher ID:",cid );
                                    db.collection("users").document(userstring)
                                            .set(udetails)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(registerTeacher.this, "Account details saved...",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(registerTeacher.this, "Error in writing details.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(registerTeacher.this, "Authentication failed/User already exists.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}