package com.example.attendeasev2;


import static android.content.ContentValues.TAG;
import com.example.attendeasev2.getTeacherData;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.firestore.Firestore;
//import com.google.firebase.cloud.FirestoreClient;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button lbutton,mbutton;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressBar progressBar;
    TextView textView1,textView2,textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth= FirebaseAuth.getInstance();
        lbutton=findViewById(R.id.logout);
        mbutton=findViewById(R.id.userMenu);
        textView1=findViewById(R.id.uemail);
        textView2=findViewById(R.id.uname);
        textView3=findViewById(R.id.utypes);
        user=auth.getCurrentUser();

        if (user==null)
        {
            Intent intent = new Intent(getApplication(),Login.class);
            startActivity(intent);
            finish();
        }
        else
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser ud=FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference docRef = db.collection("users").document(ud.getEmail().toString());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> udetails = new HashMap<>();
                            udetails = document.getData();
                            String text = "EMAIL ID: " + udetails.get("email").toString();
                            textView1.setText(text);
                            text = "NAME: " + udetails.get("name").toString();
                            textView2.setText(text);
                            text = "Role: " + udetails.get("type").toString();
                            textView3.setText(text);
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            //textView.setText(user.getEmail());


        }

        lbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplication(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                FirebaseUser ud2=FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference docRef = db2.collection("users").document(ud2.getEmail());;
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> udetails = new HashMap<>();
                            udetails = document.getData();
                            String type= udetails.get("type").toString();
                            getUserData d1=new getUserData(db2,ud2);
                        //Toast.makeText(MainActivity.this,d1.type,Toast.LENGTH_SHORT).show();
                            if (type.equals("student"))
                            {
                                Intent intent = new Intent(getApplication(),MainStudent.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (type.equals("teacher"))
                            {
                                Intent intent = new Intent(getApplication(),MainTeacher.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Something wrong",Toast.LENGTH_SHORT).show();
                            }
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });


                //else if()
            }
        });
    }
}




