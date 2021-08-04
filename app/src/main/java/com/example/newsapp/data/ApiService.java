package com.example.newsapp.data;

import com.example.newsapp.data.utils.CountriesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    static final String base_url = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<CountriesResponse>> getCountryData();

}
