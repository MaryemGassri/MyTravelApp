package com.example.travelapp.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.R;
import com.example.travelapp.Util.SimpleFavs;
import com.example.travelapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Item object;
    private SimpleFavs simpleFavs;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        simpleFavs = new SimpleFavs(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        object = (Item) getIntent().getSerializableExtra("object");

        setVariable();

        binding.backBtn.setOnClickListener(v -> finish());

        binding.addToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
            intent.putExtra("object", object);
            startActivity(intent);
        });
    }

    private void setVariable() {
        if (object == null) return;

        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.addressTxt.setText(object.getAddress());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.bedTxt.setText(String.valueOf(object.getBed()));
        binding.ratingBar.setRating((float) object.getScore());
        binding.ratingTxt.setText(object.getScore() + " Rating");

        Glide.with(this)
                .load(object.getPic())
                .into(binding.pic);

        // initialize favorite state and UI
        isFavorite = simpleFavs.isFav(object);
        updateHeartAppearance();

        binding.favBtn.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            if (isFavorite) {
                simpleFavs.add(object);
            } else {
                simpleFavs.remove(object);
            }
            updateHeartAppearance();
        });
    }

    private void updateHeartAppearance() {
        // use fav_filled drawable tinted to red or grey
        binding.favBtn.setImageResource(R.drawable.fav_filled);
        int color = isFavorite
                ? ContextCompat.getColor(this, R.color.red)
                : ContextCompat.getColor(this, R.color.grey);
        binding.favBtn.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}