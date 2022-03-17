package com.example.foodworm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_gologin;
    private Button btn_register;
    private EditText et_regid;
    private EditText et_regpw;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("식충이");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        btn_gologin = findViewById(R.id.btn_gologinact);
        btn_register= findViewById(R.id.btn_register);
        et_regid = findViewById(R.id.et_regid);
        et_regpw = findViewById(R.id.et_regpw);

        //login버튼. 클릭시 로그인 액티비티로 이동하는 리스너 부착
        btn_gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //회원가입 버튼. 텍스트뷰에 입력된 정보를 바탕으로 회원가입 승인 요청을한다
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewUser(et_regid.getText().toString(), et_regpw.getText().toString());
            }
        });

    }
    //새로운 유저의 승인을 결정하고 최종 승인시 파이어베이스 디비를 업데이트한다.
    //총 유저의 수를 +1 하고 유저의 정보를 객체 상태로 업로드함
    private void CreateNewUser(String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //회원가입 성공시 토스트 메세지 출력
                            Toast.makeText(RegisterActivity.this, "회원가입성공!!",
                                    Toast.LENGTH_SHORT).show();
                            //파베 디비 업데이트
                            AddNewUserFireBase(et_regid.getText().toString(), et_regpw.getText().toString());

                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(RegisterActivity.this, "회원가입실패ㅠㅠ.",
                                    Toast.LENGTH_LONG).show();
                        }
                        // ...
                    }
                });
    }

    //유저의 정보를 파이어베이스로 업데이트 하는 함수.
    private void AddNewUserFireBase(String email, String password) {
        //1.SingleListener 부착 후 유저 수를 +1 하여 업데이트 한다
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //NumofUser의 값을 꺼내어  1을더하여  파베에 값저장
                int temp = dataSnapshot.child("NumofUser").getValue(Integer.class);
                temp = temp+1;
                mDatabase.child("NumofUser").setValue(temp);
                Log.d("NumofUser Update", "NumofUser Update");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("NumofUser update faild", "NumofUser update faild");
            }
        });
        //2.User객체를 생성하여 파베에 추가
        String key = mDatabase.child("user").push().getKey();
        User user = new User(email, password, key);
        mDatabase.child("user").child(key).setValue(user);
    }
}

