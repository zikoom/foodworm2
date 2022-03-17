package com.example.foodworm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;


public class BuyActivity extends AppCompatActivity {
    //유저의 User객체값을 저장할 변수
    private User myself;
    //유저의 아이디를 저장할 변수
    private String UserId;
    private FirebaseAuth mAuth;

    //DataBase리퍼런스
    private DatabaseReference mDatabase;

    //데이터베이스의 NumofPurchase 저장용변수, 구매횟수를 저장
    private int DataBase_NumofPurchase;
    //데이터베이스의 식권별 누적 판매량 변수
    private int DataBase_soldkor;
    private int DataBase_soldjpa;
    private int DataBase_soldusa;
    // 구매수량 변수
    private int k_count = 0;
    private int j_count = 0;
    private int u_count = 0;
    // 최종 결제 금액
    private int final_cash = 0;
    //최종 결제 버튼
    private Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("식충이");
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserId = currentUser.getEmail();
        Log.d("유저아이디 받기",UserId);
        //DB 레퍼런스 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Datadbase root-NumofPurchase 리스너 장착
        //이 레퍼런스는 오로지 데이터베이스의 NumofPurchase값의 변화만 추적하며 읽는다
        //리스너 부착
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataBase_NumofPurchase = dataSnapshot.child("NumofPurchase").getValue(Integer.class);
                Log.d("데이터 베이스의 모든 구매 횟수 값",Integer.toString(DataBase_NumofPurchase));
                //각 식권의 누적 식권판매량을 가져온다
                DataBase_soldkor =dataSnapshot.child("Soldticket").child("kor").getValue(Integer.class);
                DataBase_soldjpa =dataSnapshot.child("Soldticket").child("jpa").getValue(Integer.class);
                DataBase_soldusa =dataSnapshot.child("Soldticket").child("usa").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //사용자 ID 정보를 이용해 객체값 가져오기
        Query myQ = mDatabase.child("user").orderByChild("id").equalTo(UserId);
        myQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("쿼리리스너 실행","In DataChange");
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    myself = ds.getValue(User.class);
                    //유저 객체 받아오기 완료
                    Log.d("ID값", myself.id);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //값 가져오기 완료


        setContentView(R.layout.activity_buy);
        // 한식 구매 변수
        final TextView korea_count = (TextView)findViewById(R.id.tv_Kset);
        Button korea_minus = (Button)findViewById(R.id.btn_Kminus);
        Button korea_plus = (Button)findViewById(R.id.btn_Kplus);

        // 일품 구매 변수
        final TextView japan_count = (TextView)findViewById(R.id.tv_Jset);
        Button japan_minus = (Button)findViewById(R.id.btn_Jminus);
        Button japan_plus = (Button)findViewById(R.id.btn_Jplus);

        // 양식 구매 변수
        final TextView usa_count = (TextView)findViewById(R.id.tv_Uset);
        Button usa_minus = (Button)findViewById(R.id.btn_Uminus);
        Button usa_plus = (Button)findViewById(R.id.btn_Uplus);

        // 텍스트 변수
        final TextView order = (TextView)findViewById(R.id.tv_order);
        final TextView cash = (TextView)findViewById(R.id.tv_cash);

        // 최종구매버튼 변수
        buy = (Button)findViewById((R.id.btn_buy));

        ///////////////////////////////////////////
        ////////////////최종구매버튼 리스너///////
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //식권을 아무도 안사려는 경우는 무반응
                if(k_count + j_count + u_count == 0){}
                //식권을 하나라도 산경우
                else {
                    //1.거래내역을 PurchaseHistory 객체로 DB에 추가한다
                    AddPurchaseHistory(k_count, j_count, u_count);
                    //2.DB 의 현재 User의 정보를 수정한다 (각 식권의 누적 개수, 각 식권의 현재 개수)
                    UpdateUserTicketInfo(k_count, j_count, u_count);
                    //3.DB의 전체 구매 횟수값을 수정한다 (NumofBuying), (Numofsoldticket)
                    UpdatetotalNumofPurchase(k_count, j_count, u_count);

                    Toast.makeText(getApplicationContext(),"구매 완료",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        //////////////////////////////////////////
        ///////////////////////////////////////////

        korea_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(k_count != 0){
                    k_count--;
                    korea_count.setText(""+k_count);
                    order.setText("한식 식권 " + k_count + "매 , " + "일품 식권 " + j_count + "매 , " + "양식 식권 " + u_count + "매");
                    final_cash = ((2500 * k_count) + (3500 * j_count) + (4000 * u_count));
                    cash.setText(""+final_cash);
                }
            }
        });

        korea_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k_count++;
                korea_count.setText(""+k_count);
                order.setText("한식 식권 " + k_count + "매 , " + "일품 식권 " + j_count + "매 , " + "양식 식권 " + u_count + "매");
                final_cash = ((2500 * k_count) + (3500 * j_count) + (4000 * u_count));
                cash.setText(""+final_cash);
            }
        });

        japan_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j_count != 0){
                    j_count--;
                    japan_count.setText(""+j_count);
                    order.setText("한식 식권 " + k_count + "매 , " + "일품 식권 " + j_count + "매 , " + "양식 식권 " + u_count + "매");
                    final_cash = ((2500 * k_count) + (3500 * j_count) + (4000 * u_count));
                    cash.setText(""+final_cash);
                }
            }
        });

        japan_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j_count++;
                japan_count.setText(""+j_count);
                order.setText("한식 식권 " + k_count + "매 , " + "일품 식권 " + j_count + "매 , " + "양식 식권 " + u_count + "매");
                final_cash = ((2500 * k_count) + (3500 * j_count) + (4000 * u_count));
                cash.setText(""+final_cash);
            }
        });

        usa_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u_count != 0){
                    u_count--;
                    usa_count.setText(""+u_count);
                    order.setText("한식 식권 " + k_count + "매 , " + "일품 식권 " + j_count + "매 , " + "양식 식권 " + u_count + "매");
                    final_cash = ((2500 * k_count) + (3500 * j_count) + (4000 * u_count));
                    cash.setText(""+final_cash);
                }
            }
        });

        usa_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_count++;
                usa_count.setText(""+u_count);
                order.setText("한식 식권 " + k_count + "매 , " + "일품 식권 " + j_count + "매 , " + "양식 식권 " + u_count + "매");
                final_cash = ((2500 * k_count) + (3500 * j_count) + (4000 * u_count));
                cash.setText(""+final_cash);
            }
        });

    }

    ///oncreate 밖.


    //거래내역을 PurchaseHistory 객체로 DB에 추가하는 함수 <거래정보>
    //이 함수안에서 시간정보 생성, 데이터베이스 키값을 생성한다
    public void AddPurchaseHistory(int kor, int jpa, int usa){
        //1. 디비에 "PurchaseHistory"에서 push()하고 키값을 받는다
        String purchasehistoryKey = mDatabase.child("PurchaseHistory").push().getKey();
        //2. 날짜정보를 생성해 내가 정의한 포맷으로 바꾼다
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년MM월dd일HH시mm분ss초");
        String format_time1 = format.format(System.currentTimeMillis());

        //3.주어진 정보로 PurchaseHistory 객체를 만든다
        PurchaseHistory data = new PurchaseHistory(myself.id, format_time1,purchasehistoryKey, kor, jpa, usa);

        //4.객체를 데이터베이스에 업로드한다.
        mDatabase.child("PurchaseHistory").child(purchasehistoryKey).setValue(data);
    }

    //거래내역정보를 바탕으로 개인 User정보를 수정하는 함수<현 식권 보유량, 누적식권 구매량 수정>
    public void UpdateUserTicketInfo(int kor, int jpa, int usa){
        //1.현재 어플리케이션에 있는 user정보를 수정한다
        myself.kor += kor;
        myself.numofbuying_kor += kor;

        myself.jpa += jpa;
        myself.numofbuying_jpa += jpa;

        myself.usa += usa;
        myself.numofbuying_usa += usa;
        //2. 이 객체를 기존 객체에 덮어쓴다.
        mDatabase.child("user").child(myself.pushkey).setValue(myself);
    }

    //거래 정보를 바탕으로 데이터베이스 총 누적 구매 횟수, 식권당 팔린 개수를 더하여 기록한다.
    public void UpdatetotalNumofPurchase(int kor, int jpa, int usa){
        mDatabase.child("NumofPurchase").setValue(DataBase_NumofPurchase+1);
        mDatabase.child("Soldticket").child("kor").setValue(DataBase_soldkor+kor);
        mDatabase.child("Soldticket").child("jpa").setValue(DataBase_soldjpa+jpa);
        mDatabase.child("Soldticket").child("usa").setValue(DataBase_soldusa+usa);
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
