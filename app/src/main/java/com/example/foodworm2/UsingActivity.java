package com.example.foodworm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class UsingActivity extends AppCompatActivity {
    //유저의 User객체값을 저장할 변수
    private User myself;
    //유저의 아이디를 저장할 변수
    private String UserId;
    private FirebaseAuth mAuth;

    //현재 식권수를 보여주는 텍스트뷰
    private TextView tv_kor;
    private TextView tv_jpa;
    private TextView tv_usa;

    //세가지 사용하기 버튼
    private Button use_kor;
    private Button use_usa;
    private Button use_jpa;

    //DataBase리퍼런스
    DatabaseReference mDatabase;
    //유저 레퍼런스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using);

        getSupportActionBar().setTitle("식충이");
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserId = currentUser.getEmail();
        Log.d("유저아이디 받기",UserId);

        //TextView 연결
        tv_kor = findViewById(R.id.tv_showkorticket);
        tv_jpa = findViewById(R.id.tv_showjpaticket);
        tv_usa = findViewById(R.id.tv_showusaticket);

        //사용하기 버튼 연결
        use_kor = findViewById(R.id.btn_usekor);
        use_usa = findViewById(R.id.btn_useusa);
        use_jpa = findViewById(R.id.btn_usejpa);

        //쿼리에 사용자 정보받아오기
        Query myQ = mDatabase.child("user").orderByChild("id").equalTo(UserId);
        myQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("쿼리리스너 실행","In DataChange");
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    myself = ds.getValue(User.class);
                    //유저 객체 받아오기 완료
                    Log.d("ID값", myself.id);
                    tv_kor.setText(Integer.toString(myself.kor));
                    tv_jpa.setText(Integer.toString(myself.jpa));
                    tv_usa.setText(Integer.toString(myself.usa));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //사용하기 버튼 리스너 장착
        use_kor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myself.kor > 0){
                    Intent intent = new Intent(UsingActivity.this, CreateQRActivity.class);
                    String qrstring;
                    qrstring = UserId + ",kor";

                    intent.putExtra("qrstring", qrstring);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(UsingActivity.this, "사용가능한 한식 식권이 없습니다!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        use_usa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myself.usa > 0){
                    Intent intent = new Intent(UsingActivity.this, CreateQRActivity.class);
                    String qrstring;
                    qrstring = UserId + ",usa";

                    intent.putExtra("qrstring", qrstring);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(UsingActivity.this, "사용가능한 양식 식권이 없습니다!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        use_jpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myself.usa > 0){
                    Intent intent = new Intent(UsingActivity.this, CreateQRActivity.class);
                    String qrstring;
                    qrstring = UserId + ",jpa";

                    intent.putExtra("qrstring", qrstring);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(UsingActivity.this, "사용가능한 일식 식권이 없습니다!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
