package com.example.attendeasev2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class add_Student extends AppCompatActivity {
    Button btn;
    FirebaseAuth mAuth;
    EditText editTextName, editTextRollno;
    TextInputEditText editTextEmail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize UI elements
        btn = findViewById(R.id.btn_done);
        editTextName = findViewById(R.id.Fname);
        editTextEmail = findViewById(R.id.email);
        editTextRollno = findViewById(R.id.rno);
        progressBar = findViewById(R.id.progressBar);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextName.getText().toString();
                String email = String.valueOf(editTextEmail.getText());
                String rno = String.valueOf(editTextRollno.getText());
                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(add_Student.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(rno)) {
                    Toast.makeText(add_Student.this, "Enter Roll Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(add_Student.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the user is authenticated
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser == null) {
                    // Handle the case when the user is not authenticated.
                    Toast.makeText(add_Student.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Initialize FirebaseFirestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Get the user ID
                String userString = firebaseUser.getEmail();

                // Create a map with user details
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("email", email);
                userDetails.put("rno", rno);
                userDetails.put("name", fullName);
                userDetails.put("attendance", " ");

                // Save details to Firestore
                db.collection("users").document(userString).collection("students").document(rno)
                        .set(userDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(add_Student.this, "Account details saved...", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(add_Student.this, "Error in writing details.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                FirebaseUser ud=FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference docRef = db.collection("users").document(ud.getEmail());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int lastrno=Integer.parseInt(document.get("lrno").toString());
                                lastrno++;
                                docRef.update("lrno",lastrno)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }
}
