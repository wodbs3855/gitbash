package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {


    TextView textViewCare, textViewNews, textViewHome, textViewCom, textViewMy;
    ImageView imageViewCare, imageViewNews, imageViewHome, imageViewCom, imageViewMy;
    ImageView imageadd1, imageadd2, imageadd3, imageadd4, imageadd5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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

        imageadd1 = findViewById(R.id.Add1);
        imageadd2 = findViewById(R.id.Add2);
        imageadd3 = findViewById(R.id.Add3);
        imageadd4 = findViewById(R.id.Add4);
        imageadd5 = findViewById(R.id.Add5);

        imageadd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),WebView_1.class);
                startActivity(intent1);

            }
        });

        imageadd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), WebView_2.class);
                startActivity(intent2);

            }
        });

        imageadd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(), WebView_3.class);
                startActivity(intent3);

            }
        });

        imageadd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), WebView_4.class);
                startActivity(intent4);

            }
        });

        imageadd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(getApplicationContext(), WebView_5.class);
                startActivity(intent5);

            }
        });

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



        imageViewCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
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
}