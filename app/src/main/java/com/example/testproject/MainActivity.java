package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity{


    private DatabaseReference mDatabase;
    //파이어베이스에 데이터를 추가나 조회하기 위해 사용
    private FirebaseAuth firebaseAuth = null;

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN= 9001;

    ImageView signup, read, login;
    EditText mEmail, name, age, id, password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //데이터를 읽거나 쓰기위해서는 DatabaseReference의 인스턴스가 필요
        firebaseAuth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.buttonSignup); //회원가입 버튼

        mEmail = findViewById(R.id.editEmail); // 이메일
        login = findViewById(R.id.buttonLogin);
        password = findViewById(R.id.editTextPassword);

        //signInButton = findViewById(R.id.SignIn_button);//추가

       /* if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
            finish();
        }*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signup.setOnClickListener(new View.OnClickListener() {// 저장 버튼누르면 값저장
            @Override
            public void onClick(View v) {
                /*String getUserName = name.getText().toString();
                String getUserEmail = email.getText().toString();
                String getUserAge = age.getText().toString();
                writeUser(Integer.toString(i++), getUserName, getUserEmail, getUserAge);//함수*/

                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "아이디나 비번이 틀렸습니다.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

        /*mDatabase.child("boardType").child("photo").setValue("사진 게시판")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });*/

        mDatabase.child("boardType").child("text").setValue("텍스트 게시판")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });



        /*read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readUser(id.getText().toString());

            }
        });
    }

        private void writeUser(String userId, String name, String email, String age){ //함수
            User user = new User(name, email, age);
            //데이터 저장
            mDatabase.child("users").child(userId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() { //데이터베이스에 넘어간 이후 처리
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"저장을 완료했습니다", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"저장에 실패했습니다" , Toast.LENGTH_LONG).show();
                        }
                    });
    }
    private void readUser(String userId) {//함수
        //데이터 읽기
        mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                data.setText("이름: " + user.name + "\n이메일: " + user.email + "\n나이: " + user.age);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/



        //DataSet();

    }




    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }*/



    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Google sign in Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            //Snackbar.make(findViewById(R.id.layout_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    // [END auth_with_google]

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }






    /*public void DataSet(){
        FirebaseUser user = firebaseAuth.getCurrentUser(); // 이는 현재 로그인한 사람의 정보를 가지고 옵니다
        final String uid = user.getUid(); // uid 는 계정 하나당 하나의 값을 가지게 됩니다 그래서 이것으로 분간을 하겠습니다

        mDatabase.addValueEventListener(new ValueEventListener() {
            // 위의 함수는 전부 비동기 처리가 이루어지기 때문에 전부 이벤트가 발생하는 시점에서 이 함수를 써주셔야 합니다 앞으로 지겹게 볼 예정입니다
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String firstLunch = (String)dataSnapshot.child("user").child(uid).child("firstLunch").getValue(); // 이 앱을 처음 사용하면 DB에 값을 넣어주기 값을 세팅
                if(firstLunch == null || firstLunch.equals("")){ // 값의 주소값이 없거나 , 그 값이 비어 있으면 처음 들어오는 사람으로 색인을 해줌
                    mDatabase.child("user").child(uid).child("name").setValue("");
                    mDatabase.child("user").child(uid).child("firstLunch").setValue("Y");
                }else{
                    return;
                }


                User user = dataSnapshot.child("user").child(uid).getValue(User.class); // 이는 해당 데이터를 바로 클래스 형태로 넣는 방법입니다 이때 getter는 필수 입니다
                userClass.setName(user.getName()); // 전역으로 이 값을 사용하기 위해서 세팅을 해줍니다
                userClass.setFirstLunch(user.getFirstLunch());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }*/


}

