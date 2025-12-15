package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // "GET STARTED" → go to LoginActivity
        binding.btnStart.setOnClickListener(v -> openLoginAndFinish());

        // "Skip" → also go to LoginActivity
        binding.skipTxt.setOnClickListener(v -> openLoginAndFinish());
    }

    private void openLoginAndFinish() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();  // prevents returning to the splash screen
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}