package com.neon.vyaan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.CartAdapter;
import com.neon.vyaan.adapter.ProductsAdapter;
import com.neon.vyaan.adapter.SideMenuListAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.fragments.AboutUsFragment;
import com.neon.vyaan.fragments.BaseFragment;
import com.neon.vyaan.fragments.ContsctUsFragment;
import com.neon.vyaan.fragments.MainBoard;
import com.neon.vyaan.fragments.MyPlans;
import com.neon.vyaan.fragments.TransactionHistory;
import com.neon.vyaan.fragments.ViewBills;
import com.neon.vyaan.fragments.ViewOrders;
import com.neon.vyaan.model.ProductsModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.widgets.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends BaseActivity {


    DrawerLayout drawerLayout;
    LinearLayout fragmentContainerWithToolbar;
    FrameLayout fragmentContainer;

    TextView textUserName;
    RecyclerView recyclerNavigationDrawer;
    ActionBarDrawerToggle actionBarDrawerToggle;

    boolean isExitable;

    String tag;

    BaseFragment frag;

    SideMenuListAdapter sideMenuListAdapter;
    LinearLayoutManager managerSideMenu;

    int fragmentType = 0;

    Fragment chatFragment;


    CircularImageView circleImageDrawerProfile;

    String userProfileUrl;


    LinearLayout containerImage;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //   super.onSaveInstanceState(outState);
    }

    @Override
    protected void initViews() {


        if (getIntent().hasExtra(DASHBOARD_FRAGMENT_TYPE)) {
            fragmentType = getIntent().getIntExtra(DASHBOARD_FRAGMENT_TYPE, 0);
        }

        if (getIntent().hasExtra(KEY_PAY_FOR_SUBSCRIPTION)) {
            fragmentType = 3;
        }

        if (getIntent().hasExtra(KEY_PAY_FOR_ORDER)) {
            fragmentType = 2;
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        fragmentContainerWithToolbar = (LinearLayout) findViewById(R.id.fragmentContainerWithToolbar);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
        circleImageDrawerProfile = (CircularImageView) findViewById(R.id.circleImageDrawerProfile);
        textUserName = (TextView) findViewById(R.id.textUserName);
        recyclerNavigationDrawer = (RecyclerView) findViewById(R.id.recyclerNavigationDrawer);
        circleImageDrawerProfile = (CircularImageView) findViewById(R.id.circleImageDrawerProfile);
        containerImage = (LinearLayout) findViewById(R.id.containerImage);
        //  circleImageDrawerProfile.setOnClickListener(this);
        containerImage.setOnClickListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name) {

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                drawerLayout.openDrawer(Gravity.LEFT);

                return true;
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                toHideKeyboard();

            }

            @Override
            public void onDrawerStateChanged(int newState) {

                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    toHideKeyboard();

                }
            }

        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setSelection(fragmentType);


        sideMenuListAdapter = new SideMenuListAdapter(currentActivity);
        managerSideMenu = new LinearLayoutManager(currentActivity);

        recyclerNavigationDrawer.setLayoutManager(managerSideMenu);
        recyclerNavigationDrawer.setAdapter(sideMenuListAdapter);

    }

    @Override
    protected void initContext() {
        currentActivity = Dashboard.this;
        context = Dashboard.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
    }

    @Override
    public void onAlertClicked(int alertType) {
        if (alertType == ALERT_TYPE_LOGOUT) {
            logout();
        }

    }


    public void logout() {


        SharedPreferenceUtils.getInstance(currentActivity).clearALl();
        startActivity(currentActivity, AppAccessActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
        finish();

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.containerImage: {
                startActivity(currentActivity, MyProfileActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        userProfileUrl = SharedPreferenceUtils.getInstance(currentActivity).getString(USER_IMAGE);

        if (userProfileUrl != null && !userProfileUrl.equals("")) {
            Picasso.with(currentActivity).load(BASE_URL_IMAGES + "/" + userProfileUrl).into(circleImageDrawerProfile);
        }
        Picasso.with(currentActivity).load(BASE_URL_IMAGES + "/" + userProfileUrl).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                containerImage.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                containerImage.refreshDrawableState();


            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }


            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
        textUserName.setText(SharedPreferenceUtils.getInstance(currentActivity).getString(USER_PROFILE_NAME));
    }


    public void setSelection(int position) {
        fragmentType = position;
        drawerLayout.closeDrawer(Gravity.LEFT);


        frag = null;
        boolean isAddedBackStack = false;

        boolean isReplace = false;

        switch (position) {

            case MAINBOARD: {
                frag = new MainBoard();
                settingTitle(getResources().getString(R.string.title_main_board));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }


            case MY_PLANS: {

                frag = new MyPlans();
                settingTitle(getResources().getString(R.string.title_my_plans));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }


            case VIEW_ORDERS: {
                frag = new ViewOrders();
                settingTitle(getResources().getString(R.string.title_view_orders));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }

            case VIEW_BILLS: {
                frag = new ViewBills();
                settingTitle(getResources().getString(R.string.title_view_bills));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
       /*     case TRANSACTION_HISTORY: {
                frag = new TransactionHistory();
                settingTitle(getResources().getString(R.string.title_transaction_history));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }*/
            case ABOUT_US: {
                frag = new AboutUsFragment();
                settingTitle(getResources().getString(R.string.title_about_us));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
            case CONTACT_US: {
                frag = new ContsctUsFragment();
                settingTitle(getResources().getString(R.string.title_contact_us));
                removingHomeButton();
                isAddedBackStack = true;
                break;
            }
            case RATE_US: {

                btnRateAppOnClick();
                isAddedBackStack = true;
                break;
            }
            case LOGOUT: {
                alert(currentActivity, getResources().getString(R.string.alert_title_logout), getResources().getString(R.string.alert_message_logout), getResources().getString(R.string.alert_ok_button_text_no_network), getResources().getString(R.string.alert_cancel_button_text_no_network), true, true, ALERT_TYPE_LOGOUT);

                break;
            }


        }

        if (frag != null) {
            switchContent(frag, false, isReplace, frag.getClass().getName());
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        actionBarDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onBackPressed() {

        if (isExitable) {
            super.onBackPressed();
        } else {
            toast(getResources().getString(R.string.message_app_exit), true);
            isExitable = true;


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExitable = false;
                }
            }, APP_EXIT_TIME);
        }


    }


    //On click event for rate this app button
    public void btnRateAppOnClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=com.neon.vyan"));
        if (!MyStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.neon.vyan"));
            if (!MyStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("cart click ", "yes");
        switch (item.getItemId()) {
            case R.id.action_cart: {
                Log.e("cart click ", "yes");
                startActivity(currentActivity, CartActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void myPlans() {
        //   ((MyPlans) frag).myPlans();
        setSelection(3);

        //  super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAG_MY_PLANS) {
            if (resultCode == RESULT_OK) {
                myPlans();
            }
        }
        //  super.onActivityResult(requestCode, resultCode, data);
    }
}
