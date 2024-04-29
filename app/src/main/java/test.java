//package com.example.attendeasev2;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class add_Student extends AppCompatActivity {
//    Button btn;
//    FirebaseAuth mAuth;
//    public EditText editTextName,editTextType;
//    public TextInputEditText editTextEmail, editTextRollno;
//    TextView textView;
//
//    ProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_student);
//        btn=findViewById(R.id.btn_done);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String fullName = editTextName.getText().toString();
//                String email = String.valueOf(editTextEmail.getText());
//                int rno = Integer.parseInt(editTextRollno.getText().toString());
//                progressBar.setVisibility(View.VISIBLE);
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(add_Student.this, "Enter Email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (rno == 0) {
//                    Toast.makeText(add_Student.this, "Enter Roll Number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(fullName)) {
//                    Toast.makeText(add_Student.this, "Enter Name", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                FirebaseUser firebaseUser= mAuth.getCurrentUser();
//                FirebaseFirestore db;
//                db=FirebaseFirestore.getInstance();
//                FirebaseUser ud= FirebaseAuth.getInstance().getCurrentUser();
//                String userstring=ud.getEmail();
//                Map<String, Object> udetails = new HashMap<>();
//                udetails.put("email:",ud.getEmail());
//                udetails.put("rno", rno);
//                udetails.put("name", fullName);
//                udetails.put("attendance"," ");
//                db.collection("users").document(userstring).collection("students").document("list")
//                        .set(udetails)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(add_Student.this, "Account details saved...",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(add_Student.this, "Error in writing details.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//
//        });
//
//
//    }
//}