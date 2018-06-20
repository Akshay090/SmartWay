package com.idealtechcontrivance.ashish.examcracker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.R;

public class SplashActivity extends Activity implements Animation.AnimationListener {

    Animation animFadeIn;
    RelativeLayout relativeLayout;

    SharedPreferences preferences;
    final static String PREFS_NAME = "userInfo";

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_splash);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_fade_in);

        // set animation listener
        animFadeIn.setAnimationListener(this);

        // animation for image
        relativeLayout = findViewById(R.id.splashLayout);

        // start the animation
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(animFadeIn);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //under Implementation
    }

    public void onAnimationEnd(Animation animation) {

        // check if user is already logged in
        // i.e. auth token is present or not
        //String token = preferences.getString("userEmail", DEFAULT);
        // means user is logged in token was found
        if (session.isLoggedIn()) {
            Intent intent = new Intent(SplashActivity.this,DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        //under Implementation
    }
}
