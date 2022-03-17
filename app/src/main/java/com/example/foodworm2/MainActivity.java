package com.example.foodworm2;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;

        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;

        import java.io.ByteArrayOutputStream;

        import static android.graphics.BitmapFactory.*;


public class MainActivity extends AppCompatActivity {

    private Button btn_using;
    private Button btn_buying;
    private Button btn_mypage;
    private ImageView img_korea;
    private ImageView img_japan;
    private ImageView img_usa;
    final int KOREA=100;
    final int JAPAN=200;
    final int USA=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("식충이");

        btn_using = findViewById(R.id.btn_main_using);
        btn_using.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , UsingActivity.class);
                startActivity(intent);
            }
        });

        btn_mypage=findViewById(R.id.btn_mypage);
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, mypageUserActivity.class);
                startActivity(intent);
            }
        });

        btn_buying = findViewById(R.id.btn_main_buy);
        btn_buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });

        img_korea = findViewById(R.id.img_main_korea);
        img_korea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                intent.putExtra("korea",KOREA);
                startActivity(intent);
            }
        });

        img_japan = findViewById(R.id.img_main_japan);
        img_japan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                intent.putExtra("japan",JAPAN);
                startActivity(intent);
            }
        });

        img_usa = findViewById(R.id.img_main_usa);
        img_usa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                intent.putExtra("usa",USA);
                startActivity(intent);
            }
        }); }


}
