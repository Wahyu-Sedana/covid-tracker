package com.example.newsapp.views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.MainActivity;
import com.example.newsapp.R;
import com.example.newsapp.data.utils.CountriesResponse;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    private Context context;
    private List<CountriesResponse> list;

    public ListAdapter(Context context,  List<CountriesResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(v);
    }

    public void filterList(List<CountriesResponse> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        CountriesResponse listCountry = list.get(position);
        holder.countryName.setText(listCountry.getCountry());
        holder.countryDeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(String.valueOf(listCountry.getDeaths()))));
        holder.setText(listCountry.getUpdated());

        Map<String, String> img = listCountry.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.countryImage);

        holder.itemView.setOnClickListener( v-> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("country", listCountry.getCountry());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView countryName, countryDeaths, time;
        private ImageView countryImage;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            countryName = itemView.findViewById(R.id.countryName);
            countryDeaths = itemView.findViewById(R.id.deathsCountry);
            countryImage = itemView.findViewById(R.id.flag);
            time = itemView.findViewById(R.id.timeCountry);
        }

        public void setText(String date) {
            DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
            long millSecconds = Long.parseLong(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millSecconds);
            time.setText("Updated at " + format.format(calendar.getTime()));
        }
    }
}
