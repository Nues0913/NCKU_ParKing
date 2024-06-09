package com.example.java_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button btnGoToMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGoToMap = findViewById(R.id.btnGoToMap);
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
}