package com.neon.vyaan.adapter;

/**
 * Created by Mayank on 28/04/2016.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.Dashboard;
import com.neon.vyaan.utils.LoginUtils;


/**
 * Created by Mayank on 06/03/2016.
 */

public class SideMenuListAdapter extends RecyclerView.Adapter<SideMenuListAdapter.ViewHolder> {

    String[] sideMenuArray;
    int[] sideMenuImages = {R.drawable.icon_product_list, R.drawable.icon_my_plan, R.drawable.icon_view_order, R.drawable.icon_view_order, R.drawable.icon_about_us,  R.drawable.icon_contact_us , R.drawable.icon_rate_us,R.drawable.icon_logout};
    Activity currentActivty;

    public SideMenuListAdapter(Activity currentActivty) {
        this.currentActivty = currentActivty;
        sideMenuArray = currentActivty.getResources().getStringArray(R.array.sideBarItems);

    }

    @Override
    public SideMenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivty).inflate(R.layout.items_side_menu, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SideMenuListAdapter.ViewHolder holder, int position) {
        final int pos = position;
        holder.textSideBarItem.setText(sideMenuArray[pos]);
        if (sideMenuArray[pos].equals("Logout")) {
            if (!LoginUtils.isLogin(currentActivty)) {
                holder.textSideBarItem.setText("Login");
            }
        }
        holder.imageSideBarItem.setImageDrawable(currentActivty.getResources().getDrawable(sideMenuImages[pos]));
        holder.containerSideMenuItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Dashboard) currentActivty).setSelection(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sideMenuImages.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSideBarItem;
        TextView textSideBarItem;
        LinearLayout containerSideMenuItems;

        public ViewHolder(View itemView) {
            super(itemView);
            imageSideBarItem = (ImageView) itemView.findViewById(R.id.imageSideBarItem);
            textSideBarItem = (TextView) itemView.findViewById(R.id.textSideBarItem);
            containerSideMenuItems = (LinearLayout) itemView.findViewById(R.id.containerSideMenuItems);
        }
    }
}