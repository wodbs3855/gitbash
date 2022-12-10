package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ReadBoardActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDatabase;
    String uid, name;

    TextView textView_read_board_contentType2, textView_read_board_writer2, textView_read_board_title2, textView_read_board_content, textView_read_board_date2, textView_read_board_click2, textView_read_board, textView_count;
    EditText editText_read_board_title, editText_read_board_content;
    String board_uid;
    String board_key;
    Map board_content_type;
    ImageView imageViewBackBtn;
    Button button_del, button_edit, button_ok, button_cancle;

    EditText editComment;
    Button button_add;

    ListView commentListView;
    CommentAdapter commentAdapter = null;

    ArrayList<String> arr_uid = null;
    ArrayList<String> arr_board_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_board);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        commentListView = (ListView) findViewById(R.id.list_commentListView);
        commentAdapter = new CommentAdapter();


        arr_uid = new ArrayList<>();
        arr_board_key = new ArrayList<>();

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

        imageViewBackBtn = findViewById(R.id.backbtn);

        textView_read_board_contentType2 = (TextView) findViewById(R.id.textView_read_board_content); // 컨텐츠 타입으로 적힐것
        textView_read_board_writer2 = (TextView) findViewById(R.id.textView_read_board_writer); // 작성자
        textView_read_board_title2 = (TextView) findViewById(R.id.textView_read_board_title); // 제목
        textView_read_board_content = (TextView) findViewById(R.id.textView_read_board_content); // 내욜
        textView_read_board_date2 = (TextView) findViewById(R.id.textView_read_board_date); // 날짜
        textView_read_board_click2 = (TextView) findViewById(R.id.textView_read_board_click); // 조회수
        textView_read_board = (TextView) findViewById(R.id.textView_read_board);
        textView_count = (TextView) findViewById(R.id.textView_count);
        editText_read_board_title = (EditText) findViewById(R.id.editText_read_board_title);
        editText_read_board_content = (EditText) findViewById(R.id.editText_read_board_content);
        board_uid = getIntent().getStringExtra("arr_uid"); // intent 로 넘어오는 값을 세팅함
        board_key = getIntent().getStringExtra("arr_board_key");
        button_del = (Button) findViewById(R.id.button_del); // 삭제 버튼
        button_edit = (Button) findViewById(R.id.button_edit); // 수정 버튼
        button_ok = (Button) findViewById(R.id.button_ok); // 완료 버튼
        button_cancle = (Button) findViewById(R.id.button_cancle); // 취소 버튼

        editComment = (EditText) findViewById(R.id.editComment);
        button_add = (Button) findViewById(R.id.button_add);

        board_type(); // 게시판 속성 정의
        board_set(); // 화면 구성 시작
        getBoard();

        imageViewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (uid.equals(board_uid)){
            button_del.setVisibility(View.VISIBLE);
            button_edit.setVisibility(View.VISIBLE);
        }

        button_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener confirm = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("board").child("text").child(board_uid).child(board_key).removeValue();
                        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                DialogInterface.OnClickListener cancle = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                };
                new AlertDialog.Builder(ReadBoardActivity.this).setTitle("게시글을 삭제하시겠습니까?")
                        .setPositiveButton("삭제", confirm)
                        .setNegativeButton("취소", cancle).show();
            }
        });

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_del.setVisibility(View.INVISIBLE);
                button_edit.setVisibility(View.INVISIBLE);
                button_add.setVisibility(View.INVISIBLE);
                editComment.setVisibility(View.INVISIBLE);
                button_ok.setVisibility(View.VISIBLE);
                button_cancle.setVisibility(View.VISIBLE);
                textView_count.setVisibility(View.INVISIBLE);
                commentListView.setVisibility(View.INVISIBLE);
                textView_read_board.setText("게시글 수정");

                editText_read_board_title.setText(textView_read_board_title2.getText().toString());
                textView_read_board_title2.setVisibility(View.INVISIBLE);
                editText_read_board_title.setVisibility(View.VISIBLE);

                editText_read_board_content.setText(textView_read_board_content.getText().toString());
                textView_read_board_content.setVisibility(View.INVISIBLE);
                editText_read_board_content.setVisibility(View.VISIBLE);
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("board").child("text").child(board_uid).child(board_key).child("title").setValue(editText_read_board_title.getText().toString());
                mDatabase.child("board").child("text").child(board_uid).child(board_key).child("content").setValue(editText_read_board_content.getText().toString());

                Intent i = new Intent(ReadBoardActivity.this , BoardActivity.class);
                startActivity(i);
                finish();
            }
        });

        button_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_del.setVisibility(View.VISIBLE);
                button_edit.setVisibility(View.VISIBLE);
                button_ok.setVisibility(View.INVISIBLE);
                button_cancle.setVisibility(View.INVISIBLE);
                button_add.setVisibility(View.VISIBLE);
                editComment.setVisibility(View.VISIBLE);
                textView_count.setVisibility(View.VISIBLE);
                commentListView.setVisibility(View.VISIBLE);
                textView_read_board.setText("게시판");

                textView_read_board_title2.setVisibility(View.VISIBLE);
                editText_read_board_title.setVisibility(View.INVISIBLE);

                textView_read_board_content.setVisibility(View.VISIBLE);
                editText_read_board_content.setVisibility(View.INVISIBLE);
            }
        });

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");

                Date time = new Date();

                String order_today = dateFormat2.format(time);

                Board comment = new Board();
                comment.setComment(editComment.getText().toString());
                comment.setName(name);
                comment.setUid(uid);
                comment.setDate(order_today);

                mDatabase.child("board").child("comment").child(board_uid).child(board_key).child("comm").push().setValue(comment);

                Intent i = new Intent(ReadBoardActivity.this , BoardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.context, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //context.xml파일에 작성한 메뉴항목들의 id를 식별하여 토스트 보여주기
        switch ( item.getItemId() ){
            case R.id.menu_mod:
                button_del.setVisibility(View.INVISIBLE);
                button_edit.setVisibility(View.INVISIBLE);
                button_add.setVisibility(View.INVISIBLE);
                editComment.setVisibility(View.INVISIBLE);
                button_ok.setVisibility(View.VISIBLE);
                button_cancle.setVisibility(View.VISIBLE);
                textView_count.setVisibility(View.INVISIBLE);
                commentListView.setVisibility(View.INVISIBLE);
                textView_read_board.setText("게시글 수정");

                editText_read_board_title.setText(textView_read_board_title2.getText().toString());
                textView_read_board_title2.setVisibility(View.INVISIBLE);
                editText_read_board_title.setVisibility(View.VISIBLE);

                editText_read_board_content.setText(textView_read_board_content.getText().toString());
                textView_read_board_content.setVisibility(View.INVISIBLE);
                editText_read_board_content.setVisibility(View.VISIBLE);



                break;

            case R.id.menu_delete:
                DialogInterface.OnClickListener confirm = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //removeComment();
                        mDatabase.child("board").child("text").child(board_uid).child(board_key).removeValue();
                        Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                DialogInterface.OnClickListener cancle = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                };
                new AlertDialog.Builder(ReadBoardActivity.this).setTitle("게시글을 삭제하시겠습니까?")
                        .setPositiveButton("삭제", confirm)
                        .setNegativeButton("취소", cancle).show();


                break;
        }

        return super.onContextItemSelected(item);
    }




    public void board_type() {
        mDatabase.child("boardType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Log.d("data_snap", "" + dataSnapshot);
                //board_content_type = (Map) dataSnapshot.getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void board_set() {
        mDatabase.child("board").child("text").child(board_uid).child(board_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("data_snap", "" + dataSnapshot);
                Board board = dataSnapshot.getValue(Board.class);

                mDatabase.child("board").child("text").child(board_uid).child(board_key).child("click").setValue(board.getClick()+1);

                //textView_read_board_contentType2.setText((Integer) board_content_type.get(board.getContentType()));
                textView_read_board_writer2.setText(board.getName());
                textView_read_board_title2.setText(board.getTitle());
                textView_read_board_date2.setText(board.getDate());
                textView_read_board_content.setText(board.getContent());
                textView_read_board_click2.setText(String.valueOf(board.getClick()));

                textView_read_board_content.setMovementMethod(new ScrollingMovementMethod()); // TextView가 스크롤이 생김


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getBoard() {
        mDatabase.child("board").child("comment").child(board_uid).child(board_key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                commentListView.setAdapter(commentAdapter);
                int count = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Board board = snapshot.getValue(Board.class);

                    arr_uid.add(board.getUid());
                    arr_board_key.add(snapshot.getKey());

                    commentAdapter.addItem(board.getName(), board.getDate(), board.getComment());

                    count += 1;
                }
                textView_count.setText("댓글 " + Integer.toString(count));
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
    /*public void removeComment(){
        mDatabase.child("board").child("comment").child(board_uid).child(board_key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Board board = snapshot.getValue(Board.class);
                    mDatabase.child("board").child("comment").child(board_uid).child(board_key).child(snapshot.getKey()).removeValue();
                }
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

}

