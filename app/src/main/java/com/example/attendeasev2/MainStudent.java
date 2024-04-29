package com.example.attendeasev2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class MainStudent extends AppCompatActivity {
    Button button1;
    TextView textView1,textView2,textView3;
    TextInputEditText subj;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        subj=findViewById(R.id.subj);
        button1=findViewById(R.id.btn_getsub);
        textView1=findViewById(R.id.ssub);
        textView2=findViewById(R.id.spres);
        textView3=findViewById(R.id.stot);
        progressBar=findViewById(R.id.progressBar);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser ud = FirebaseAuth.getInstance().getCurrentUser();
                String className = "cseA";
                String subject = String.valueOf(subj.getText());
                String studentName = "Abbhinav";
                //String mail="abbhinav@gmail.com";

                progressBar.setVisibility(View.VISIBLE);

                DocumentReference docRef = db.collection("users").document(ud.getEmail().toString());

                // Retrieve the list of dates from the "dateList" document
                db.collection(className)
                        .document("dateList"+subject)
                        .get()
                        .addOnSuccessListener(dateListSnapshot -> {
                            List<String> dates = (List<String>) dateListSnapshot.get("dateList");
                            int totalDays = dates.size();
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Map<String, Object> udetails = new HashMap<>();
                                            udetails = document.getData();
                                            String pres=udetails.get("attendance").toString();
                                            textView2.setText(pres);
                                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        } else {
                                            Log.d(ContentValues.TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(ContentValues.TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                            // Calculate absent days once all date collections have been processed

                            progressBar.setVisibility(View.GONE);

                            textView1.setText(subject);
                            //presentDays.get()));
                            textView3.setText(String.valueOf(totalDays));
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error getting date list", e);
                            progressBar.setVisibility(View.GONE);
                        });
            }

        });
    }
}
