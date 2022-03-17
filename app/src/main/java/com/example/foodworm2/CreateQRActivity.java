package com.example.foodworm2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CreateQRActivity extends AppCompatActivity {

    private String qrstring;
    private ImageView iv_qrimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        getSupportActionBar().setTitle("식충이");
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv_qrimg = findViewById(R.id.iv_qrimg);

        //qr코드 데이터 수신
        Intent intent = getIntent();

        qrstring = intent.getExtras().getString("qrstring");
        Log.d("인텐트수신", qrstring);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        //만든데이터 기반으로 qr코드 이미지 생성
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(qrstring, BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv_qrimg.setImageBitmap(bitmap);
        }catch(Exception e){}
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
