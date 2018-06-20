package com.idealtechcontrivance.ashish.examcracker.Activity;

import android.annotation.SuppressLint;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.idealtechcontrivance.ashish.examcracker.Fragment.AboutUsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.ContactUsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.CoursesFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.DashboardFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.FAQFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.HomeFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.NotificationFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.SignInFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.TestSeriesFragment;
import com.idealtechcontrivance.ashish.examcracker.Helper.SharedPref;
import com.idealtechcontrivance.ashish.examcracker.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private View headerView;

    private SharedPref sharedpref;
    private boolean on;

    private static final String COMMON_TAG = "OrientationChange";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        loadLocal();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        // Call the function callInstamojo to start payment here

        initViews();
        initListeners();
        initObjects();
        printHashKey();

        if (savedInstanceState == null){
            addHomeFragment();
        }

        setSupportActionBar(toolbar);
        Log.i(COMMON_TAG,"MainActivity onCreate");

        if (sharedpref.loadNightModeState()) {
            on=true;
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
                addSignInFragment();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }

    private void addSignInFragment() {
        SignInFragment signInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .add(new HomeFragment(),"HomeFragment")
                .addToBackStack("HomeFragment")
                .replace(R.id.mainContainer, signInFragment)
                .commit();
    }

    private void initObjects() {
        headerView = navigationView.getHeaderView(0);
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(this);
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

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.idealtechcontrivance.ashish.examcracker",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_translate), getResources().getString(R.string.language)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_visibility), getResources().getString(R.string.night_mode)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            NotificationFragment notificationFragment = new NotificationFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(new DashboardFragment(),"DashboardFragment")
                    .addToBackStack("DashboardFragment")
                    .replace(R.id.mainContainer, notificationFragment)
                    .commit();
        }

        switch (id) {
            case 1:
                showChangeLanguageDialog();
                return true;
            case 2:
                showChangeNightModeDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        int id = item.getItemId();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        switch(id) {
            case R.id.nav_home:
//                HomeFragment homeFragment = new HomeFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .add(new HomeFragment(),"HomeFragment")
//                        .addToBackStack("HomeFragment")
//                        .replace(R.id.mainContainer, homeFragment)
//                        .commit();
                break;
            case R.id.nav_test_series:
                TestSeriesFragment testSeriesFragment = new TestSeriesFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, testSeriesFragment)
                        .commit();
                break;
            case R.id.nav_courses:
                CoursesFragment coursesFragment = new CoursesFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, coursesFragment)
                        .commit();
                break;
            case R.id.nav_faq:
                FAQFragment faqFragment = new FAQFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, faqFragment)
                        .commit();
                break;
            case R.id.nav_about_us:
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, aboutUsFragment)
                        .commit();
                break;
            case R.id.nav_contact_us:
                ContactUsFragment contactUsFragment= new ContactUsFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, contactUsFragment)
                        .commit();
                break;
            case R.id.nav_rate_app:
                try {
                    Uri uri = Uri.parse("market://details?id=com.idealtechcontrivance.ashish.examcracker");
                    Intent rateApp = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(rateApp);
                }catch (ActivityNotFoundException e){
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.idealtechcontrivance.ashish.examcracker");
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
                break;
            case R.id.nav_share_app:
                Intent shareApp = new Intent();
                shareApp.setAction(Intent.ACTION_SEND);
                String shareSubject = "Let me recommend you this application";
                String shareBody = "https://play.google.com/store/apps/details?id=com.idealtechcontrivance.ashish.examcracker";
                shareApp.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                shareApp.putExtra(Intent.EXTRA_TEXT,shareBody);
                shareApp.setType("text/plain");
                startActivity(Intent.createChooser(shareApp,"Choose App To Share Link"));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    @Override
    public void onStart() {
        super.onStart();
        disconnectFromFacebook();
    }

    private void showChangeLanguageDialog() {
        final String[] listItems ={"Hindi","English"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language...");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    setLocate("hi");
                    recreate();
                }
                else if (which == 1){
                    setLocate("en");
                    recreate();
                }
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setLocate(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    public void loadLocal(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language  = preferences.getString("My_Lang","");
        setLocate(language);
    }

    private void showChangeNightModeDialog() {
        final String[] listItems ={"Turn Off","Turn On"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Night Mode...");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    if (on){
                        sharedpref.setNightModeState(false);
                        recreate();
                    }
                }
                else if (which == 1){
                    sharedpref.setNightModeState(true);
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
                Log.d("Activity", "ON RESULT CALLED");
            }
        }catch (Exception e){
            Log.d("ERROR", e.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
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
