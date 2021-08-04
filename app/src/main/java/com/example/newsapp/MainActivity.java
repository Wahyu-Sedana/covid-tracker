package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.data.ApiRetfofit;
import com.example.newsapp.data.utils.CountriesResponse;
import com.example.newsapp.views.CountryAcitivty;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalCases, totalDeaths, totalRecorverd, totalActives, countryTracker, info;
    private TextView time;
    public List<CountriesResponse> list;
    String country ="Afghanistan";
    private BarChart barchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if(getIntent().getStringExtra("country") != null)
            country = getIntent().getStringExtra("country");

        init();
        ApiRetfofit.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountriesResponse>>() {
                    @Override
                    public void onResponse(Call<List<CountriesResponse>> call, Response<List<CountriesResponse>> response) {
                        list.addAll(response.body());

                        for(int i = 0; i < list.size(); i++){
                            if(list.get(i).getCountry().equals(country)){
                                int cases = Integer.parseInt(String.valueOf(list.get(i).getCases()));
                                int deaths = Integer.parseInt(String.valueOf(list.get(i).getDeaths()));
                                int recorverds = Integer.parseInt(String.valueOf(list.get(i).getRecovered()));
                                int actives = Integer.parseInt(String.valueOf(list.get(i).getActive()));

                                totalCases.setText(NumberFormat.getInstance().format(cases));
                                totalDeaths.setText(NumberFormat.getInstance().format(deaths));
                                totalRecorverd.setText(NumberFormat.getInstance().format(recorverds));
                                totalActives.setText(NumberFormat.getInstance().format(actives));

                                setText(list.get(i).getUpdated());

                                barchart.addBar(new BarModel(cases, getResources().getColor(R.color.yelllow)));
                                barchart.addBar(new BarModel(deaths, getResources().getColor(R.color.red)));
                                barchart.addBar(new BarModel(recorverds, getResources().getColor(R.color.green)));
                                barchart.addBar(new BarModel(actives, getResources().getColor(R.color.blue)));

                                barchart.startAnimation();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountriesResponse>> call, Throwable t) {

                    }
                });
    }

    private void setText(String updated){
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        long millSecconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millSecconds);
        time.setText("Updated at " + format.format(calendar.getTime()));

    }

    private void init(){
        if(getSupportActionBar()!=null) {
            Drawable drawable = getResources().getDrawable(R.drawable.app_logo);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 35, 35, true));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newdrawable);
        }
        countryTracker = findViewById(R.id.countryTracker);
        countryTracker.setText(country);
        countryTracker.setOnClickListener( v->{
            startActivity(new Intent(MainActivity.this, CountryAcitivty.class));
        });
        barchart = findViewById(R.id.barchart);
        totalCases = findViewById(R.id.cases);
        totalDeaths = findViewById(R.id.deaths);
        totalRecorverd = findViewById(R.id.recorverd);
        totalActives = findViewById(R.id.actives);
        time = findViewById(R.id.updated);
        info = findViewById(R.id.masihUpdate);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Sedang melakukan pembaharuan", Toast.LENGTH_LONG).show();
            }
        });
    }
}