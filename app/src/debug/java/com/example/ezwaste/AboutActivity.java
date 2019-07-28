package com.example.ezwaste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("About");
        setContentView(R.layout.activity_about);
    }
}
