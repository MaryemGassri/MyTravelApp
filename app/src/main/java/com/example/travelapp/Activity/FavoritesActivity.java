package com.example.travelapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelapp.Adapter.FavoritesAdapter;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.databinding.ActivityFavoritesBinding;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    private ActivityFavoritesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Item> bookmarkedItems = (ArrayList<Item>) getIntent().getSerializableExtra("bookmarkedItems");

        if (bookmarkedItems != null) {
            binding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.favoritesRecyclerView.setAdapter(new FavoritesAdapter(bookmarkedItems));
        }
    }
}
