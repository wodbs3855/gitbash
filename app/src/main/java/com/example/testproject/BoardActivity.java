package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class BoardActivity extends AppCompatActivity {

    TextView textViewCare, textViewNews, textViewHome, textViewCom, textViewMy;
    ImageView imageViewCare, imageViewNews, imageViewHome, imageViewCom, imageViewMy;

    private DatabaseReference mDatabase;
    //파이어베이스에 데이터를 추가나 조회하기 위해 사용
    private FirebaseAuth firebaseAuth;

    ListView mainListView;
    ListAdapter listAdapter = null;
    ImageView buttonWrtie;

    ArrayList<String> arr_uid = null;
    ArrayList<String> arr_board_key = null;
    ArrayList<String> arr_order_date = null;

    ArrayList<Board> itemList = new ArrayList<Board>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //데이터를 읽거나 쓰기위해서는 DatabaseReference의 인스턴스가 필요
        firebaseAuth = FirebaseAuth.getInstance();

        mainListView = (ListView) findViewById(R.id.list_mainListView);
        listAdapter = new ListAdapter(itemList);

        arr_uid = new ArrayList<>();
        arr_board_key = new ArrayList<>();
        arr_order_date = new ArrayList<>();

        ListListener listListener = new ListListener();
        mainListView.setOnItemClickListener(listListener);

        getBoard();
        getArray();

        buttonWrtie = findViewById(R.id.write_icon);

        buttonWrtie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BoardActivity.this, TestActivity.class);
                startActivity(intent);

            }
        });

        textViewCare = findViewById(R.id.textViewCare);
        textViewNews = findViewById(R.id.textViewNews);
        textViewCom = findViewById(R.id.textViewCom);
        textViewMy = findViewById(R.id.textViewMy);
        textViewHome = findViewById(R.id.textViewHome);

        imageViewCare = findViewById(R.id.imageViewCare);
        imageViewNews = findViewById(R.id.imageViewNews);
        imageViewCom = findViewById(R.id.imageViewCom);
        imageViewMy = findViewById(R.id.imageViewMy);
        imageViewHome = findViewById(R.id.imageViewHome);

        textViewCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CareActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CareActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageViewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(intent);
                finish();
            }
        });



        textViewMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }

    public void getBoard() { // 원래는 텍스트하고 포토 하고 구분을 할려고 했는데 일단 text만 따로 뽑기로 하겠습니다
        mDatabase.child("board").child("text").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                mainListView.setAdapter(listAdapter);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Board board = snapshot.getValue(Board.class);

                    arr_uid.add(board.getUid());
                    arr_board_key.add(snapshot.getKey());
                    arr_order_date.add(board.getOrder_date());

                    listAdapter.addItem(board.getTitle(), board.getDate(), board.getName(), board.getClick(), board.getOrder_date());
                }

                Comparator<Board> textDesc = new Comparator<Board>() {
                    @Override
                    public int compare(Board item1, Board item2) {
                        return item2.getOrder_date().compareTo(item1.getOrder_date());
                    }
                };
                Collections.sort(itemList, textDesc);
                listAdapter.notifyDataSetChanged();

                Comparator<String> dateDesc = new Comparator<String>() {
                    @Override
                    public int compare(String item1, String item2) {
                        return item2.compareTo(item1);
                    }
                };
                Collections.sort(arr_order_date, dateDesc);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getArray() {
        mDatabase.child("board").child("text").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> map = (Map) dataSnapshot.getValue();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Board board = snapshot.getValue(Board.class);

                    for (int i = 0; i < arr_order_date.size(); i++){
                        if(arr_order_date.get(i) == board.getOrder_date()){
                            arr_uid.set(i, board.getUid());
                            arr_board_key.set(i, snapshot.getKey());
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    class ListListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent i = new Intent(BoardActivity.this, ReadBoardActivity.class);
            i.putExtra("arr_uid", arr_uid.get(position));
            i.putExtra("arr_board_key", arr_board_key.get(position));
            startActivity(i);

        }
    }




}