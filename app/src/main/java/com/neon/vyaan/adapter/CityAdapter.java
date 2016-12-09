package com.neon.vyaan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.AddressModel;
import com.neon.vyaan.model.CityModel;

import java.util.List;

/**
 * Created by Mayank on 31/08/2016.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {


    Activity currentActivity;
    List<CityModel> cityModelList;


    public CityAdapter(Activity currentActivity, List<CityModel> cityModelList) {
        this.currentActivity = currentActivity;
        this.cityModelList = cityModelList;
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(currentActivity).inflate(R.layout.item_cities, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {

        final int pos = position;
        holder.textCityName.setText(cityModelList.get(pos).getName());

        holder.textCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(AppConstants.CITY_MODEL, cityModelList.get(pos));
                ((BaseActivity) currentActivity).setResult(Activity.RESULT_OK,i);
                ((BaseActivity) currentActivity).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textCityName;

        public ViewHolder(View itemView) {
            super(itemView);
            textCityName = (TextView) itemView.findViewById(R.id.textCityName);
        }
    }
}
