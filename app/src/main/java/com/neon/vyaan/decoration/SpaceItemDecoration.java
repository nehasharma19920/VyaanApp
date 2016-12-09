package com.neon.vyaan.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Mayank on 18/03/2016.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.top = space;
        outRect.left = space / 2;
        outRect.right = space / 2;

        // Add top margin only for the first item to avoid double space between items
    /*    if (parent.getChildLayoutPosition(view) % 2 != 0) {
            outRect.right = space;

        } else {
            outRect.right = 0;
        }*/
    }

}