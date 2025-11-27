package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelapp.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private static final long SPLASH_DELAY_MS = 800; // délai avant d'ouvrir MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si tu utilises une utilité EdgeToEdge, décommente la ligne suivante et assure-toi
        // d'avoir la dépendance correspondante. Sinon, laisse commentée pour éviter les crashes.
        // EdgeToEdge.enable(this);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Si le layout a un bouton Start (id StartBtn), on lie son clic (sécurisé)
        if (binding != null) {
            try {
                if (binding.StartBtn != null) {
                    binding.StartBtn.setOnClickListener(v -> {
                        openMainAndFinish();
                    });
                }
            } catch (Exception ignored) {
                // En cas de proguard/obfuscation ou nom d'id différent, on ignore et on continue
            }
        }

        // Lancement automatique après un petit délai pour debug / expérience utilisateur
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            openMainAndFinish();
        }, SPLASH_DELAY_MS);
    }

    private void openMainAndFinish() {
        try {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } catch (Exception e) {
            e.printStackTrace(); // pour aider en cas d'erreur : regarde Logcat
        } finally {
            // Termine la Splash pour ne pas revenir dessus via back
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}