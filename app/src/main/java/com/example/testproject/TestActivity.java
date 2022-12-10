package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TestActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    String uid,name;

    ImageView imageViewBackBtn;



    Spinner spinner_write_type; // 분류
    EditText write_title , write_content; // 제목 , 내용
    int click = 0;

    ArrayList<String> writeKey = null;
    ArrayList<String> writeValue = null;

    ArrayAdapter<String> adapter = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        //helloText = findViewById(R.id.textView);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageViewBackBtn = findViewById(R.id.backbtn);



        uid = user.getUid();

        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name = user.name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        spinner_write_type = (Spinner) findViewById(R.id.spinner_write_type);
        write_title = (EditText) findViewById(R.id.editText_write_title);
        write_content = (EditText) findViewById(R.id.editText_writeContent);

        writeKey = new ArrayList<>();
        writeValue = new ArrayList<>();

        getBoardType(); // 게시판 분류를 가지고 올것임

        imageViewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*mDatabase.child("Users").child(uid).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                //helloText.setText("이름: " + user.name + "\n이메일: " + user.email + "\n나이: " + user.age);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); 데이터 가져오기*/

    }

    public void getBoardType(){
        mDatabase.child("boardType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> data = (HashMap) dataSnapshot.getValue();

                writeKey.clear();
                writeValue.clear();

                for (Map.Entry<String, String> entry : data.entrySet()) { //key , value 를 구분하기 위해서 만든 분리기
                    writeKey.add(entry.getKey()); // 그 각 값을 각각 분리해서 넣음
                    writeValue.add(entry.getValue());
                }

                adapter = new ArrayAdapter<>(TestActivity.this, android.R.layout.simple_spinner_item, writeValue);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line); // dropDown 스타일 설정

                spinner_write_type.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void button_board_register(View view){



        if( write_title.getText().toString().equals("")){
            Toast.makeText(this , "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        if(write_content.getText().toString().equals("")){
            Toast.makeText(this , "내용을 입력하세요" , Toast.LENGTH_SHORT).show();
            return;
        }

        int index = spinner_write_type.getSelectedItemPosition(); // spinner 에서 선택된 것을 index 로 저장을 함




        //날짜 포맷
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        Date time = new Date();

        String today = dateFormat.format(time);
        String order_today = dateFormat2.format(time);

        Board board= new Board();
        board.setTitle(write_title.getText().toString());
        board.setContent(write_content.getText().toString());
        board.setContentType(writeKey.get(index));
        board.setUid(uid);

        board.setName(name);
        board.setClick(click);


        board.setDate(today);
        board.setOrder_date(order_today);

        mDatabase.child("board").child(writeKey.get(index)).child(uid).push().setValue(board); //push 는 FireBase에서 제공하는 api 로 여러명이 동시에 클라이언트를 이용할때 어떤 값에 대해서 독립을 보장하는 프라이머리 key 입니다

        Intent i = new Intent(TestActivity.this , BoardActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
