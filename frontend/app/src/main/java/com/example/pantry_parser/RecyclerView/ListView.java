package com.example.pantry_parser.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.pantry_parser.R;

import java.util.ArrayList;

public class ListView extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> dataset = new ArrayList<>();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        recyclerView = findViewById(R.id.recyclerView);
        populateData();
        setupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(!isLoading){
                    if (linearLayoutManager!=null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == dataset.size()-1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });
    }

    private void getMoreData() {
        dataset.add(null);
        recyclerViewAdapter.notifyItemInserted(dataset.size()-1);
        dataset.remove(dataset.size()-1);
        int currentSize = dataset.size();
        int nextSize = currentSize + 10;
        while (currentSize < nextSize){
            dataset.add("Recipe " + currentSize);
            currentSize++;
        }
        recyclerViewAdapter.notifyDataSetChanged();
        isLoading = false;
    }

    private void populateData(){
        int i = 0;
        while (i<=10) {
            dataset.add("Recipe" + i);
            i++;
        }
    }



    private void setupAdapter(){
        recyclerViewAdapter = new RecyclerViewAdapter(dataset);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}