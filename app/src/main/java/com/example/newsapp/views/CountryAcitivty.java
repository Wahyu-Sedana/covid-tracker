package com.example.newsapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import com.example.newsapp.R;
import com.example.newsapp.data.ApiRetfofit;
import com.example.newsapp.data.utils.CountriesResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryAcitivty extends AppCompatActivity {

    private EditText searchView;
    private RecyclerView rvView;
    private List<CountriesResponse> list;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_acitivty);


        getSupportActionBar().setTitle("List Negara");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        searchView = findViewById(R.id.searchCountry);
        rvView = findViewById(R.id.tvList);
        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        rvView.setHasFixedSize(true);
        listAdapter = new ListAdapter(this, list);
        rvView.setAdapter(listAdapter);

        ApiRetfofit.getApiInterface().getCountryData().enqueue(new Callback<List<CountriesResponse>>() {
            @Override
            public void onResponse(Call<List<CountriesResponse>> call, Response<List<CountriesResponse>> response) {
                list.addAll(response.body());
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {
                Toast.makeText(CountryAcitivty.this, "error" + t.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        List<CountriesResponse> filterList = new ArrayList<>();
        for (CountriesResponse items : list){
            if(items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }

        listAdapter.filterList(filterList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}