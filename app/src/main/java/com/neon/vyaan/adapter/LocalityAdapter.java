package com.neon.vyaan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.CityModel;
import com.neon.vyaan.model.LocalityModel;

import java.util.List;

/**
 * Created by Mayank on 31/08/2016.
 */
public class LocalityAdapter extends RecyclerView.Adapter<LocalityAdapter.ViewHolder> {


    Activity currentActivity;
    List<LocalityModel> localityModelList;


    public LocalityAdapter(Activity currentActivity, List<LocalityModel> localityModelList) {
        this.currentActivity = currentActivity;
        this.localityModelList = localityModelList;
    }


    @Override
    public LocalityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_localities, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LocalityAdapter.ViewHolder holder, int position) {
        final int pos = position;
        holder.textLocalityName.setText(localityModelList.get(pos).getName());

        holder.textLocalityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(AppConstants.LOCALITY_MODEL, localityModelList.get(pos));
                ((BaseActivity) currentActivity).setResult(Activity.RESULT_OK,i);
                ((BaseActivity) currentActivity).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return localityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textLocalityName;

        public ViewHolder(View itemView) {
            super(itemView);
            textLocalityName = (TextView) itemView.findViewById(R.id.textLocalityName);
        }
    }
}
