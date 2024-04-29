package com.example.attendeasev2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainTeacher extends AppCompatActivity {
    Button add,tkattn,rmestd,atnlst,back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);
        add=findViewById(R.id.addstud);
        tkattn=findViewById(R.id.takeattn);
        //rmestd=findViewById(R.id.removestd);
        atnlst=findViewById(R.id.attnlist);
        back1=findViewById(R.id.back);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser ud= FirebaseAuth.getInstance().getCurrentUser();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainTeacher.this, "Button works", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainTeacher.this,add_Student.class);
                startActivity(intent);
            }
        });

        tkattn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTeacher.this,takeAttendence.class);
                startActivity(intent);
                finish();
            }
        });

//        rmestd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(),add_Student.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        atnlst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),teacherAttnView.class);
                startActivity(intent);
                finish();
            }
        });

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}