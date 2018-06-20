package com.idealtechcontrivance.ashish.smartway.Activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.idealtechcontrivance.ashish.smartway.Fragment.AboutUsFragment;
import com.idealtechcontrivance.ashish.smartway.Fragment.ContactUsFragment;
import com.idealtechcontrivance.ashish.smartway.Fragment.FavouritesFragment;
import com.idealtechcontrivance.ashish.smartway.Fragment.HomeFragment;
import com.idealtechcontrivance.ashish.smartway.Fragment.MyCartFragment;
import com.idealtechcontrivance.ashish.smartway.R;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View headerView;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    private static final String COMMON_TAG = "OrientationChange";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();
        initObjects();

        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            addHomeFragment();
        }

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount()>0){
                    toggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }
                else {
                    toggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInDialog();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void showSignInDialog() {
        final android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
        builder1.setCancelable(true);

        LayoutInflater inflater =LayoutInflater.from(this);
        final View layout_phone_auth = inflater.inflate(R.layout.layout_phone_authentication,null);



        builder1.setView(layout_phone_auth);
        builder1.show();
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initObjects() {
        headerView = navigationView.getHeaderView(0);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private void addHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_location), getResources().getString(R.string.action_location)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_map), getResources().getString(R.string.action_offer_near_me)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search){

        }
        else if (id == R.id.action_cart){

        }

        switch (id) {
            case 1:
                showChangeLocationDialog();
                return true;
            case 2:
                showOffersNearMeDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showOffersNearMeDialog() {

    }

    private void showChangeLocationDialog() {

    }

    private CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            HomeFragment homeFragment = new HomeFragment();
//            getSupportFragmentManager().beginTransaction()
//                    .add(new HomeFragment(),"HomeFragment")
//                    .addToBackStack("HomeFragment")
//                    .replace(R.id.mainContainer, homeFragment)
//                    .commit();
        }
        else if (id == R.id.nav_cart) {
            MyCartFragment myCartFragment = new MyCartFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, myCartFragment)
                    .commit();
        }
        else if (id == R.id.nav_favourites) {
            FavouritesFragment favouritesFragment = new FavouritesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, favouritesFragment)
                    .commit();
        }
        else if (id == R.id.nav_about_us) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, aboutUsFragment)
                    .commit();
        }
        else if (id == R.id.nav_contact_us) {
            ContactUsFragment contactUsFragment = new ContactUsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.mainContainer, contactUsFragment)
                    .commit();
        }
        else if (id == R.id.nav_rate_app) {
            try {
                Uri uri = Uri.parse("market://details?id=com.idealtechcontrivance.ashish.examcracker");
                Intent rateApp = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(rateApp);
            }catch (ActivityNotFoundException e){
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.idealtechcontrivance.ashish.examcracker");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_share_app) {
            Intent shareApp = new Intent();
            shareApp.setAction(Intent.ACTION_SEND);
            String shareSubject = "Let me recommend you this application";
            String shareBody = "https://play.google.com/store/apps/details?id=com.idealtechcontrivance.ashish.examcracker";
            shareApp.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
            shareApp.putExtra(Intent.EXTRA_TEXT,shareBody);
            shareApp.setType("text/plain");
            startActivity(Intent.createChooser(shareApp,"Choose App To Share Link"));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG,"MainActivity onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(COMMON_TAG,"MainActivity onSaveInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.i(COMMON_TAG,"landscape");
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.i(COMMON_TAG,"portrait");
        }
    }

}
