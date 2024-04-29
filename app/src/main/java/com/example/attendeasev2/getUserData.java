package com.example.attendeasev2;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class getUserData extends AppCompatActivity {
    public String email;
    public String name;
    public String type;
    public getUserData(FirebaseFirestore db, FirebaseUser ud) {
        DocumentReference docRef = db.collection("users").document(ud.getEmail());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> udetails = new HashMap<>();
                    udetails = document.getData();
                    email= udetails.get("email").toString();
                    name = udetails.get("name").toString();
                    type= udetails.get("type").toString();
                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
