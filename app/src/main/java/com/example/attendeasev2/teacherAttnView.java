package com.example.attendeasev2;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import java.util.Map;

public class teacherAttnView extends AppCompatActivity {
    Button btn1;
    TextView textView1,textView2;
    TextInputEditText te1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attn_view);
        btn1=findViewById(R.id.btn_getsub);
        textView1=findViewById(R.id.Date);
        textView2=findViewById(R.id.pstuds);
        te1=findViewById(R.id.dte);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser ud= FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(ud.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> udetails = new HashMap<>();
                        udetails = document.getData();
                        String class1 = udetails.get("class").toString();
                        String subj=udetails.get("subject").toString();
                        //textView1.setText(text);
                        DocumentReference dref=db.collection(class1).document(subj).collection(te1.getText().toString()).document("present");
                        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> udetails = new HashMap<>();
                                        udetails = document.getData();
                                        String pres=udetails.get("present").toString();
                                        textView2.setText(String.valueOf(pres));
                                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(ContentValues.TAG, "No such document");
                                    }
                                } else {
                                    Log.d(ContentValues.TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


}