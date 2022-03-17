package com.example.foodworm2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;

public class A_ScanActivity extends AppCompatActivity {

    //바코드의 입력된 id를 바탕으로 user정보를 담아올 변수
    private User user;

    private String Userid;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    //qr코드관련변수
    private String qrcode;      //qr코드는 사용자의 "id"+",식권의종류" 이다. 이것을 콤마로 기준으로 두개로 나눈것이 아래 두 변수이다.
    private String userid;
    private String kindsofticket;

    private int DataBase_NumofUsing;
    //각식권별 누적사용횟수를 저장할 변수
    private int DataBase_numofusedkor;
    private int DataBase_numofusedjpa;
    private int DataBase_numofusedusa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__scan);

        new IntentIntegrator(this).initiateScan();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //DB전체의 사용횟수를 받기 위한 함수
        mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataBase_NumofUsing = dataSnapshot.child("NumofUsing").getValue(Integer.class);
                    Log.d("DB전체 사용횟수",""+ DataBase_NumofUsing);
                    DataBase_numofusedkor = dataSnapshot.child("Usedticket").child("kor").getValue(Integer.class);
                    DataBase_numofusedjpa = dataSnapshot.child("Usedticket").child("jpa").getValue(Integer.class);
                    DataBase_numofusedusa = dataSnapshot.child("Usedticket").child("usa").getValue(Integer.class);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                // todo
                Log.d("스캔성공", "스캔이후 부분 돌입");
                 qrcode = result.getContents();
                 int commaindex = qrcode.indexOf(',');
                 userid = qrcode.substring(0, commaindex);
                 kindsofticket = qrcode.substring(commaindex+1);

                 Log.d("스캔성공", "스캔된ID:"+userid);
                 Log.d("스캔성공", "스캔된식권종류:"+kindsofticket);
                Toast.makeText(this, "userid:" + userid, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "kindsofticket:" + kindsofticket, Toast.LENGTH_LONG).show();

                //식권정보는 "userid , 식권의종류" 가 된다.
                //1.userid정보를 바탕으로 userQ에 유저정보를 가져온다
                Query userQ = mDatabase.child("user").orderByChild("id").equalTo(userid);
                userQ.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("스캔성공","userQ리스너 for문");
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            user = ds.getValue(User.class);
                            Log.d("ID", user.id);
                        }
                        //2.user의정보를 바탕으로 이 바코드의 사용을 승인 미승인을 결정 만약 승인시 2.1, 2.2진행
                        if(kindsofticket.equals("kor") && user.kor <= 0){
                            //식권없음 미승인
                        }else if(kindsofticket.equals("usa") && user.usa <= 0){
                            //식권없음 미승인
                        }else if(kindsofticket.equals("jpa") && user.jpa <=0){
                            //식권없을 미승인
                        }else{
                            //해당 바코드에서 사용하길원하는 식권을 유저가 가지고있음!!
                            //바코드 사용 승인. 2.1 , 2.2 실행
                            //2.1해당 user의 보유식권개수 1차감, 식권 사용내역 +1 (유저정보 업데이트)
                            UpdateUserinfoAfterUsing(kindsofticket);
                            //2.2식권의 사용내역을 업데이트 (UsingHistory class, 사용 상세내역클래스)
                            AddUsingHistory(kindsofticket);
                            //전체 디비 총 누적 식권 사용횟수 업데이트
                            updateusedticket(kindsofticket);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError){}
                });

            }
            new IntentIntegrator(this).initiateScan();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //id 를 정보로 DB에서 해당 user정보를 꺼내와 userQ에저장하고 리스너부착
    public void getuserinfo(String id){
        Log.d("스캔성공","getuserinfo 함수진입");
        Query userQ = mDatabase.child("user").orderByChild("id").equalTo(id);
        userQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("스캔성공","userQ리스너 for문");
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    user = ds.getValue(User.class);
                    Log.d("ID", user.id);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        });
    }
    public void UpdateUserinfoAfterUsing(String T_kind){
        //1.현재 user의 정보수정
        //해당 식권개수 -1 , 해당 식권 누적사용횟수 +1
        if(T_kind.equals("kor")){
            user.kor = user.kor - 1;
            user.numofusing_kor = user.numofusing_kor +1;
        }else if(T_kind.equals("usa")){
            user.usa = user.usa - 1;
            user.numofusing_usa = user.numofusing_usa +1;
        }else{
            user.jpa = user.jpa - 1;
            user.numofusing_jpa = user.numofusing_jpa +1;
        }
        //2. 수정된 유저정보를 디비에 덮어쓴다
        mDatabase.child("user").child(user.pushkey).setValue(user);

    }
    public void AddUsingHistory(String T_kind) {
        String usinghistorykey = mDatabase.child("UsingHistory").push().getKey();
        SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일HH시mm분ss초");
        String format_time1 = format.format(System.currentTimeMillis());

        UsingHistroy data = new UsingHistroy(user.id, format_time1, usinghistorykey, T_kind);
        mDatabase.child("UsingHistory").child(usinghistorykey).setValue(data);
    }

    //DB에있는 총 누적 식권사용횟수, 각 식권별 사용횟수 업데이트
    public void updateusedticket(String ticket_kind){
        mDatabase.child("NumofUsing").setValue(DataBase_NumofUsing+1);
        switch (ticket_kind){
            case "kor":
                mDatabase.child("Usedticket").child("kor").setValue(DataBase_numofusedkor+1);
                break;
            case "jpa":
                mDatabase.child("Usedticket").child("jpa").setValue(DataBase_numofusedjpa+1);
                break;
            case "usa":
                mDatabase.child("Usedticket").child("usa").setValue(DataBase_numofusedusa+1);
                break;
        }
    }
}
