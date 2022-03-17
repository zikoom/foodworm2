package com.example.foodworm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class mypageUserActivity extends AppCompatActivity {

    myinfo myinfo;
    buyhistory buyhistory;
    usehistory usehistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_user);
        myinfo = new myinfo();
        buyhistory = new buyhistory();
        usehistory = new usehistory();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, myinfo).commit();

        getSupportActionBar().setTitle("식충이");
        //홈버튼 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mypage_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean returnv = super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.mypage_user_myifno:
                Log.d("buyhistory", "myinfo: ");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, myinfo).commit();
                returnv = true;
                break;
            case R.id.mypage_user_buyhistory:
                Log.d("buyhistory", "buyhistory: ");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, buyhistory).commit();
                returnv = true;
                break;
            case R.id.mypage_user_usehistory:
                Log.d("usehistory","usehistory");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, usehistory).commit();

        }
        return returnv;
    }
}
