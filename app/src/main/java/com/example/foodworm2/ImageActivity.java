package com.example.foodworm2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageActivity extends AppCompatActivity {

    private ImageView img_sub_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image);

        getSupportActionBar().setTitle("식충이");
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_sub_image = findViewById(R.id.img_sub_image);
        img_sub_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent(); // 데이터 수신
        int temp = intent.getIntExtra("korea",0);
        int temp2 = intent.getIntExtra("japan",0);
        int temp3 = intent.getIntExtra("usa",0);
        if(temp == 100){

            img_sub_image.setImageResource(R.drawable.sickbug);
        }else if(temp2 == 200){
            img_sub_image.setImageResource(R.drawable.sickbug);
        }else if(temp3 == 300){
            img_sub_image.setImageResource(R.drawable.sickbug);

        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
