package com.example.foodworm2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class A_wholeinfo extends AppCompatActivity {

    frag_statistics stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_wholeinfo);

        stat = new frag_statistics();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, stat).commit();

        getSupportActionBar().setTitle("식충이~");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mene_adminpage, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnv = super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menu_admin_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, stat);
                break;
        }

        return returnv;
    }
}

