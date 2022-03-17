package com.example.foodworm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btn_goregister;
    private Button btn_login;
    private EditText et_id;
    private EditText et_pw;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("식충이");

        btn_goregister = findViewById(R.id.btn_goregister);
        btn_login = findViewById(R.id.btn_login);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        mAuth = FirebaseAuth.getInstance();

        btn_goregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(et_id.getText().toString(), et_pw.getText().toString());
            }
        });
    }


    private void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //email이 admin@admin.admin일 경우 admin메인화면으로 들어감
                            if(et_id.getText().toString().equals("admin@admin.admin")){
                                Log.d("어드민로그인 성공","로그인 성공");
                                Intent intent = new Intent(LoginActivity.this, A_MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Log.d("성공","로그인 성공");
                                startActivity(intent);
                            }

                        } else {
                            Log.d("실패","로그인 실패");
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
