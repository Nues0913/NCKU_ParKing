package com.example.java_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button btnGoToMap;
//    ParkingCrawler parkingCrawler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGoToMap = findViewById(R.id.btnGoToMap);

//        parkingCrawler = new ParkingCrawler();
//        parkingCrawler.startCrawler();

        btnGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGoToMap.setEnabled(false);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                btnGoToMap.setEnabled(true);
            }
        });
    }

    @Override
    protected void onPause() {
        Log.v("welcome", "start onPause");
//        parkingCrawler.stopCrawler();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.v("welcome", "start onResume");
//        parkingCrawler.startCrawler();
        super.onResume();
    }
}