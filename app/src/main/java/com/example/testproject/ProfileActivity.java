package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    //파이어베이스에 데이터를 추가나 조회하기 위해 사용
    private FirebaseAuth firebaseAuth;

    EditText editTextID, editTextName;

    ImageView imageViewBack;

    TextView textViewChange, textViewChangeID, textViewLogout, textViewDel, textViewNameChange;

    Button buttonNameChange, buttonPassWordChange, buttonChangeId;

    String uid, email, name;
    String newEmail, newName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextID = findViewById(R.id.editTextTextEmailAddressId);
        editTextName = findViewById(R.id.editTextTextPersonName);

        imageViewBack = findViewById(R.id.imageViewBack);

        buttonPassWordChange = findViewById(R.id.pw_change_btn);


        textViewLogout = findViewById(R.id.textViewLogout);
        textViewDel = findViewById(R.id.textViewDel);

        buttonNameChange = findViewById(R.id.name_change_btn);


        uid = user.getUid();

        newEmail = editTextID.getText().toString();



        mDatabase.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                editTextID.setText(user.email);
                editTextName.setText(user.name);

                email = user.email;
                name = user.name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        buttonNameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              newName = editTextName.getText().toString();

              if (name.equals(editTextName.getText().toString())){

                  //같으면 변경없음
                }
              else {
                  mDatabase.child("Users").child(uid).child("name").setValue(newName);

              }


            }
        });


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MyActivity.class);
                startActivity(intent);
                finish();

            }
        });


        /*buttonPassWordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               updateEmail();

            }
        });아이디 변경불가*/


        buttonPassWordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();

            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ProfileActivity.this);
                alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        user.delete().addOnCompleteListener(task ->  {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }
                        });



                    }
                });
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProfileActivity.this, "취소", Toast.LENGTH_LONG).show();
                    }
                });
                alert_confirm.show();
            }

        });






    }
    //이메일을 보내서 비밀번호 재설정
    private void send(){
        if(email.length() !=0){
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(this,"이메일을 보냈습니다.",Toast.LENGTH_SHORT).show();
                }


            });
        }
        else {
            Toast.makeText(this,"입력을 완료해 주세요.",Toast.LENGTH_SHORT).show();
        }

    }


    private void updateEmail(){

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user.updateEmail(newEmail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "변경됨", Toast.LENGTH_LONG).show();


                }
                else{
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();

                }
            }

        });






    }



}