package com.example.travelapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.travelapp.Adapter.CategoryAdapter;
import com.example.travelapp.Adapter.PopularAdapter;
import com.example.travelapp.Adapter.RecommendedAdapter;
import com.example.travelapp.Adapter.SliderAdapter;
import com.example.travelapp.Domain.Category;
import com.example.travelapp.Domain.Item;
import com.example.travelapp.Domain.Location;
import com.example.travelapp.Domain.SliderItems;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private PopularAdapter popularAdapter;
    private RecommendedAdapter recommendedAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.chipNavigationBar.setOnItemSelectedListener(id -> {
            if (id == R.id.bookmark) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                ArrayList<Item> bookmarkedItems = new ArrayList<>();
                if (popularAdapter != null) {
                    bookmarkedItems.addAll(popularAdapter.getBookmarkedItems());
                }
                if (recommendedAdapter != null) {
                    bookmarkedItems.addAll(recommendedAdapter.getBookmarkedItems());
                }
                intent.putExtra("bookmarkedItems", bookmarkedItems);
                startActivity(intent);
            } else if (id == R.id.cart) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        database = FirebaseDatabase.getInstance(
                "https://travelapp-accfc-default-rtdb.firebaseio.com/"
        );

        initLocations();
        initBanners();
        initCategory();
        initPopular();
        initRecommended();
    }

    private void initRecommended() {
        DatabaseReference myRef = database.getReference("Item");
        final ArrayList<Item> list = new ArrayList<>();

        binding.progressBarRecommended.setVisibility(android.view.View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Item item = child.getValue(Item.class);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                }

                if (!list.isEmpty()) {
                    binding.recyclerViewRecommended.setLayoutManager(
                            new LinearLayoutManager(MainActivity.this,
                                    LinearLayoutManager.HORIZONTAL,
                                    false)
                    );
                    recommendedAdapter = new RecommendedAdapter(list);
                    binding.recyclerViewRecommended.setAdapter(recommendedAdapter);
                } else {
                    binding.recyclerViewRecommended.setAdapter(null);
                }

                binding.progressBarRecommended.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(android.view.View.GONE);
            }
        });
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Popular");
        final ArrayList<Item> list = new ArrayList<>();

        binding.progressBarPopular.setVisibility(android.view.View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Item item = child.getValue(Item.class);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                }

                if (!list.isEmpty()) {
                    binding.recyclerViewPopular.setLayoutManager(
                            new LinearLayoutManager(MainActivity.this,
                                    LinearLayoutManager.HORIZONTAL,
                                    false)
                    );
                    popularAdapter = new PopularAdapter(list);
                    binding.recyclerViewPopular.setAdapter(popularAdapter);
                } else {
                    binding.recyclerViewPopular.setAdapter(null);
                }

                binding.progressBarPopular.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarPopular.setVisibility(android.view.View.GONE);
            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        final ArrayList<Category> list = new ArrayList<>();

        binding.progressBarCategory.setVisibility(android.view.View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Category c = child.getValue(Category.class);
                        if (c != null) list.add(c);
                    }
                }

                if (!list.isEmpty()) {
                    binding.recyclerViewCategory.setLayoutManager(
                            new LinearLayoutManager(MainActivity.this,
                                    LinearLayoutManager.HORIZONTAL,
                                    false)
                    );
                    CategoryAdapter adapter = new CategoryAdapter(list);
                    binding.recyclerViewCategory.setAdapter(adapter);
                } else {
                    binding.recyclerViewCategory.setAdapter(null);
                }

                binding.progressBarCategory.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarCategory.setVisibility(android.view.View.GONE);
            }
        });
    }

    private void initLocations() {
        DatabaseReference myRef = database.getReference("Location");
        final ArrayList<Location> locations = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locations.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Location loc = child.getValue(Location.class);
                        if (loc != null) locations.add(loc);
                    }

                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_spinner_item,
                            locations
                    );
                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );
                    binding.locationSp.setAdapter(adapter);
                } else {
                    binding.locationSp.setAdapter(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.locationSp.setAdapter(null);
            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewPager2.setAdapter(new SliderAdapter(items, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        if (binding.viewPager2.getChildCount() > 0) {
            binding.viewPager2.getChildAt(0).setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
        }

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewPager2.setPageTransformer(compositePageTransformer);
    }

    private void initBanners() {
        DatabaseReference myRef = database.getReference("Banner");
        final ArrayList<SliderItems> items = new ArrayList<>();

        binding.progBarBanner.setVisibility(android.view.View.VISIBLE);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        SliderItems si = child.getValue(SliderItems.class);
                        if (si != null) items.add(si);
                    }
                }

                if (!items.isEmpty()) {
                    banners(items);
                } else {
                    binding.viewPager2.setAdapter(null);
                }

                binding.progBarBanner.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progBarBanner.setVisibility(android.view.View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}