package com.example.attendeasev2;

import static android.content.ContentValues.TAG;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class CurrentDate
{
    static String getToday() {

        return java.time.LocalDate.now().toString();
    }
}

public class takeAttendence extends AppCompatActivity {

    RecyclerView recyclerView;
    QuantityAdapter quantityAdapter;
    String email;
    String name;
    String type;
    List<String> sourceList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Adapter mAdapter;
    int rno;
    private TextInputEditText presentStudents;
    FirebaseAuth mAuth;

    Button back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        sourceList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter(sourceList, this); // Pass sourceList to adapter
        mRecyclerView.setAdapter(mAdapter);
        back1=findViewById(R.id.backbutton);
        fetchUserData();
        Button back1;
        back1=findViewById(R.id.backbutton);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),MainTeacher.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser ud = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(ud.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> udetails = document.getData();
                        rno = Integer.parseInt(udetails.get("lrno").toString());
                        fetchStudentData(); // Fetch student data after getting rno
                    } else {
                        Log.d(TAG, "No user document found");
                        // Handle the case where user document doesn't exist
                    }
                } else {
                    Log.d(TAG, "Failed to get user data", task.getException());
                    // Handle task failure
                }
            }
        });
    }

    private void fetchStudentData() {
        for (int i = 1; i <= rno; i++) {
            fetchStudentData(i);
        }
    }

    private void fetchStudentData(int studentIndex) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser ud = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(ud.getEmail()).collection("students").document(valueOf(studentIndex));
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> udetails2 = document.getData();
                    String name = udetails2.get("name").toString();
                    String rollnumber = udetails2.get("rno").toString();
                    sourceList.add(rollnumber+" "+name); // Add student name to sourceList
                    mAdapter.notifyDataSetChanged(); // Notify adapter about data changes
                } else {
                    Log.d(TAG, "Student document not found");
                    // Handle the case where student document doesn't exist
                }
            } else {
                Log.d(TAG, "Failed to get student data", task.getException());
                // Handle task failure
            }
        });
    }

    public void getData(View view) {
        List<String> selectedList = mAdapter.getSelectedValues();
        Map<String, List<String>> attendanceData=new HashMap<>(); // Convert to Map
        attendanceData.put("present",selectedList);
        FirebaseUser ud = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(ud.getEmail());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String className = documentSnapshot.getString("class");
                        String subj = documentSnapshot.getString("subject");
                        db.collection(className).document(subj).collection(CurrentDate.getToday()).document("present").set(attendanceData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(takeAttendence.this, "Attendance saved successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(takeAttendence.this, "Error saving attendance", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error fetching user data", e);
                });
        for(String name:selectedList)
        {
            db.collection("users")
                    .whereEqualTo("name", name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> udetails = new HashMap<>();
                                    udetails=document.getData();
                                    String email=udetails.get("email").toString();
                                    int i=Integer.parseInt(udetails.get("attendance").toString());
                                    i++;
                                    DocumentReference doc=db.collection("users").document(email);
                                    doc.update("attendance",i);
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private Map<String, Object> convertListToMap(List<String> list) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put("student" + (i + 1), list.get(i));
        }
        return map;
    }
}