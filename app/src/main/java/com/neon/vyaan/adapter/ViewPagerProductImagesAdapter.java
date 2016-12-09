package com.neon.vyaan.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.fragments.ProductImage;

import java.util.List;

/**
 * Created by Mayank on 19/03/2016.
 */
public class ViewPagerProductImagesAdapter extends FragmentStatePagerAdapter {

    List<String> picUrlList;
    FragmentManager fm;

    public ViewPagerProductImagesAdapter(FragmentManager fm, List<String> picUrlList) {
        super(fm);
        this.picUrlList = picUrlList;
        this.fm = fm;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ProductImage();
        Bundle bundle = new Bundle();
        if (picUrlList.size() > 0) {
            bundle.putString(AppConstants.KEY_PIC_URL, picUrlList.get(position));
        } else {
            bundle.putString(AppConstants.KEY_PIC_URL, "");
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (picUrlList.size() == 0) {
            return 1;
        } else if (picUrlList.size() > 3) {
            return 3;
        }


        return picUrlList.size();
    }
}
