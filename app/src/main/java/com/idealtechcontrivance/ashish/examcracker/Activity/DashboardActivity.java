package com.idealtechcontrivance.ashish.examcracker.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Fragment.BookmarkFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.ChangePasswordFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.CoursesFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.DashboardFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.HomeFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.ImportantDocumentFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.InboxFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.MyCoursesFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.MyTestFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.MyWalletFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.NewsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.NotificationFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.MyProfileFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.ReportsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.StudyMaterialFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.TestSeriesFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.TestimonialFragment;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.Helper.SharedPref;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean on;
    private SharedPref sharedpref;

    View headerView;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private String name, profile_pic, username, email, password, mobile, birthday, qualification, address, city, zipcode, gender, state;
    String strUserName, strUserEmail, strUserImage, strUserBirthday, strUserGender;
    TextView txtUserName;
    CircleImageView imgUserImage;

    CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;

    private SessionManager session;
    private ProgressDialog progressDialog;
    private static final String COMMON_TAG = "OrientationChange";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        setContentView(R.layout.activity_dashboard);

        initViews();
        initListeners();
        initObjects();
        initSession();

        if (savedInstanceState ==  null){
            addDashboardFragment();
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

        if (googleApiClient != null && googleApiClient.isConnected() || AccessToken.getCurrentAccessToken() != null){
            initSession();
        }
        else {
            if (Common.isConnectedToInternet(this)) {
                loadProfile();
            }
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileFragment();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void loadProfile() {
        if (googleApiClient == null || AccessToken.getCurrentAccessToken() == null){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Common.load_profile+ "?username=" + username,null,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try
                    {
                        profile_pic = "https://www.examcrackerst.in/upload/"+response.getString("profile_pic");
                        name =response.getString("name");
                        email =response.getString("email");
                        mobile =response.getString("mobile");
                        gender = response.getString("gender");
                        qualification = response.getString("qualification");
                        address = response.getString("address");
                        state = response.getString("state");
                        city = response.getString("city");
                        zipcode = response.getString("zipcode");

                        txtUserName.setText(name);
                        Picasso.with(getApplicationContext()).load(profile_pic).into(imgUserImage);

                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("PROFILE_PIC",profile_pic);
                        editor.putString("NAME",name);
                        editor.putString("EMAIL",email);
                        editor.putString("MOBILE",mobile);
                        editor.putString("GENDER",gender);
                        editor.putString("QUALIFICATION",qualification);
                        editor.putString("ADDRESS",address);
                        editor.putString("STATE",state);
                        editor.putString("CITY",city);
                        editor.putString("ZIPCODE",zipcode);
                        editor.apply();

                        //PD.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //PD.dismiss();
                    //Toast.makeText(DashboardActivity.this,""+error, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            MySingleton.getInstance(DashboardActivity.this).addToRequestque(jsonObjectRequest);
        }
    }

    private void addProfileFragment() {
        MyProfileFragment myProfileFragment = new MyProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .add(new DashboardFragment(),"DashboardFragment")
                .addToBackStack("DashboardFragment")
                .replace(R.id.mainContainer, myProfileFragment)
                .commit();
    }

    private void initSession() {
        headerView = navigationView.getHeaderView(0);
        txtUserName = (TextView)headerView.findViewById(R.id.txtUserName);
        imgUserImage = (CircleImageView) headerView.findViewById(R.id.imgUserImage);

        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);
        strUserName = user.get(SessionManager.KEY_NAME);
        strUserEmail= user.get(SessionManager.KEY_EMAIL);
        strUserImage = user.get(SessionManager.KEY_PROFILE_PIC);

        txtUserName.setText(strUserName);
        Picasso.with(getApplicationContext()).load(strUserImage).into(imgUserImage);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void initObjects() {
        callbackManager = CallbackManager.Factory.create();
        session = new SessionManager(getApplicationContext());
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addDashboardFragment() {
        DashboardFragment dashboardFragment = new DashboardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, dashboardFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_translate), getResources().getString(R.string.language)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_visibility), getResources().getString(R.string.night_mode)));
        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_inbox), getResources().getString(R.string.inbox)));
        menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_tv), getResources().getString(R.string.news)));
        menu.add(0, 5, 5, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_bookmark), getResources().getString(R.string.bookmark)));
        menu.add(0, 6, 6, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_lock), getResources().getString(R.string.change_password)));
        menu.add(0, 7, 7, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_logout), getResources().getString(R.string.logout)));
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
            case 3:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                InboxFragment inboxFragment = new InboxFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, inboxFragment)
                        .commit();
                return true;
            case 4:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                NewsFragment newsFragment = new NewsFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, newsFragment)
                        .commit();
                return true;
            case 5:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                BookmarkFragment bookmarkFragment = new BookmarkFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, bookmarkFragment)
                        .commit();
                return true;
            case 6:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, changePasswordFragment)
                        .commit();
                return true;
            case 7:
                showChangeLogoutDialog();
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

    private void showChangeLanguageDialog() {
        final String[] listItems ={"Hindi","English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
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

    private void showChangeLogoutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("LOGOUT");
        alertDialog.setMessage("Are you sure you want to logout?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                if (googleApiClient != null && googleApiClient.isConnected()){
                    signOutGoogle();
                }
                else if (AccessToken.getCurrentAccessToken() != null) {
                    signOutFacebook();
                }
                else {
                    session.logoutUser();
                }
                SharedPreferences preferences = getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch(id) {
            case R.id.nav_dashboard:
//                HomeFragment homeFragment = new HomeFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .add(new HomeFragment(),"HomeFragment")
//                        .addToBackStack("HomeFragment")
//                        .replace(R.id.mainContainer, homeFragment)
//                        .commit();
                break;
            case R.id.nav_my_test:
                MyTestFragment myTestFragment = new MyTestFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, myTestFragment)
                        .commit();
                break;
            case R.id.nav_my_course:
                MyCoursesFragment myCoursesFragment = new MyCoursesFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, myCoursesFragment)
                        .commit();
                break;
            case R.id.nav_reports:
                ReportsFragment reportsFragment = new ReportsFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, reportsFragment)
                        .commit();
                break;
            case R.id.nav_wallet:
                MyWalletFragment myWalletFragment = new MyWalletFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, myWalletFragment)
                        .commit();
                break;
            case R.id.nav_e_study_material:
                StudyMaterialFragment studyMaterialFragment = new StudyMaterialFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, studyMaterialFragment)
                        .commit();
                break;
            case R.id.nav_important_document:
                ImportantDocumentFragment importantDocumentFragment= new ImportantDocumentFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new HomeFragment(),"HomeFragment")
                        .addToBackStack("HomeFragment")
                        .replace(R.id.mainContainer, importantDocumentFragment)
                        .commit();
                break;
            case R.id.nav_testimonial:
                TestimonialFragment testimonialFragment = new TestimonialFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, testimonialFragment)
                        .commit();
                break;
            case R.id.nav_buy_test_series:
                TestSeriesFragment TestSeriesFragment = new TestSeriesFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, TestSeriesFragment)
                        .commit();
                break;
            case R.id.nav_buy_courses:
                CoursesFragment CoursesFragment = new CoursesFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(new DashboardFragment(),"DashboardFragment")
                        .addToBackStack("DashboardFragment")
                        .replace(R.id.mainContainer, CoursesFragment)
                        .commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                session.logoutUser();
            }
        });
    }

    public void signOutFacebook() {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                session.logoutUser();
            }
        }).executeAsync();
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
