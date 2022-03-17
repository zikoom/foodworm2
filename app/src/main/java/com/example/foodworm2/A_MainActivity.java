package com.example.foodworm2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class A_MainActivity extends AppCompatActivity {

    private Button btn_gotoscan;
    private Button btn_go_a_statistics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__main);

        btn_gotoscan = findViewById(R.id.btn_gotoscan);
        btn_gotoscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A_MainActivity.this, A_ScanActivity.class);
                startActivity(intent);
            }
        });
        btn_go_a_statistics = findViewById(R.id.btn_go_a_statistics);
        btn_go_a_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_MainActivity.this, A_wholeinfo.class);
                startActivity(intent);
            }
        });

    }
}
