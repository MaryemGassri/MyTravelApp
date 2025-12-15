package com.example.travelapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.travelapp.Domain.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SimpleFavs {
    private static final String PREF = "simple_favs";
    private static final String KEY = "fav_items";
    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public SimpleFavs(Context ctx){
        prefs = ctx.getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public List<Item> getAll(){
        String json = prefs.getString(KEY, null);
        if (json == null) return new ArrayList<>();
        Type t = new TypeToken<List<Item>>(){}.getType();
        try {
            List<Item> list = gson.fromJson(json, t);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean isFav(Item item){
        if (item == null) return false;
        for (Item i : getAll()){
            if (equalsItem(i, item)) return true;
        }
        return false;
    }

    public void add(Item item){
        if (item == null) return;
        List<Item> list = getAll();
        if (!contains(list, item)){
            list.add(item);
            save(list);
        }
    }

    public void remove(Item item){
        if (item == null) return;
        List<Item> list = getAll();
        for (int i = list.size()-1; i >= 0; i--){
            if (equalsItem(list.get(i), item)){
                list.remove(i);
            }
        }
        save(list);
    }

    private void save(List<Item> list){
        prefs.edit().putString(KEY, gson.toJson(list)).apply();
    }

    private boolean contains(List<Item> list, Item item){
        for (Item i : list) if (equalsItem(i, item)) return true;
        return false;
    }

    private boolean equalsItem(Item a, Item b){
        if (a==null || b==null) return false;
        String ta = a.getTitle() == null ? "" : a.getTitle();
        String tb = b.getTitle() == null ? "" : b.getTitle();
        String aa = a.getAddress() == null ? "" : a.getAddress();
        String ab = b.getAddress() == null ? "" : b.getAddress();
        return ta.equals(tb) && aa.equals(ab) && a.getPrice() == b.getPrice();
    }
}