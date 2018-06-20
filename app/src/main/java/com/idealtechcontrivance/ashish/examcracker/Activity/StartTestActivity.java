package com.idealtechcontrivance.ashish.examcracker.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.google.android.gms.common.api.GoogleApiClient;
import com.idealtechcontrivance.ashish.examcracker.Adapter.NewsAdapter;
import com.idealtechcontrivance.ashish.examcracker.Adapter.StartTestAdapter;
import com.idealtechcontrivance.ashish.examcracker.Common.Common;
import com.idealtechcontrivance.ashish.examcracker.Fragment.AboutUsFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.CoursesFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.DashboardFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.FAQFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.HomeFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.NotificationFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.QuestionPaletteFragment;
import com.idealtechcontrivance.ashish.examcracker.Fragment.TestSeriesFragment;
import com.idealtechcontrivance.ashish.examcracker.Helper.BottomNavigationViewHelper;
import com.idealtechcontrivance.ashish.examcracker.Helper.SessionManager;
import com.idealtechcontrivance.ashish.examcracker.Helper.SharedPref;
import com.idealtechcontrivance.ashish.examcracker.Model.News;
import com.idealtechcontrivance.ashish.examcracker.Model.StartTest;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.idealtechcontrivance.ashish.examcracker.Helper.BottomNavigationViewHelper.disableShiftMode;

public class StartTestActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    private SharedPref sharedpref;
    private boolean on;
    String language;
    Context context;

    private TextView txtQuestion;
    private ImageView imgQuestion;
    String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
    private Pattern pattern;
    private Matcher matcherQuestion, matcherHindiQuestion;

    String strTestId, strTestType, strTestName, strTestDuration;

    MenuItem  counter, offTimer, onTimer;
    int time;
    String timeLeftFormatted2;

    private static long START_TIME_IN_MILLIS, SECOND_START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer, mSecondCountDownTimer;
    private boolean mTimerRunning, mSecondTimerRunning;
    private long mTimeLeftInMillis, mSecondTimeLeftInMillis;

    GoogleApiClient googleApiClient;
    SessionManager session;

    //Bottom Sheet
    QuestionPaletteFragment questionPaletteFragment;

    ArrayList<StartTest> startTestList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    private ProgressDialog progressDialog;
    ProgressBar testProgressBar;

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
        setContentView(R.layout.activity_start_test);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strTestId = extras.getString("ID_KEY");
            strTestType = extras.getString("TYPE_KEY");
            strTestName = extras.getString("NAME_KEY");
            strTestDuration = extras.getString("DURATION_KEY");
            //The key argument here must match that used in the other activity
        }

        time = Integer.parseInt(strTestDuration);
        START_TIME_IN_MILLIS = time * 60 *1000;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;

        SECOND_START_TIME_IN_MILLIS = 240000;
        mSecondTimeLeftInMillis = SECOND_START_TIME_IN_MILLIS;

        initViews();
        initListeners();
        initObjects();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(strTestName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (sharedpref.loadNightModeState()) {
            on=true;
        }

        if (Common.isConnectedToInternet(this))
        {
            startTest();
        }
        else {
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_LONG).show();
        }

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.mainBottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        disableShiftMode(bottomNavigationView);

    }

    private void startTest() {
        testProgressBar.setVisibility(View.VISIBLE);
        jsonArrayRequest = new JsonArrayRequest(Common.start_test+"?testName="+strTestId+"&test_type="+strTestType,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        testProgressBar.setVisibility(View.GONE);
                        JSON_PARSE_DATA_AFTER_WEBCALL_TEST(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        testProgressBar.setVisibility(View.GONE);
                    }
                });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL_TEST(JSONArray response) {
        startTestList.clear();
        for(int i = 0; i<response.length(); i++) {
            StartTest startTest = new StartTest();
            JSONObject json = null;
            try {
                json = response.getJSONObject(i);

                startTest.setPassage(json.getString("passage"));
                startTest.setQuestion(json.getString("question"));
                startTest.setOpt1(json.getString("opt1"));
                startTest.setOpt2(json.getString("opt2"));
                startTest.setOpt3(json.getString("opt3"));
                startTest.setOpt4(json.getString("opt4"));
                startTest.setOpt5(json.getString("opt5"));

                startTest.setHindi_passage(json.getString("hindi_passage"));
                startTest.setHindi_question(json.getString("hindi_question"));
                startTest.setHindi_opt1(json.getString("hindi_opt1"));
                startTest.setHindi_opt2(json.getString("hindi_opt2"));
                startTest.setHindi_opt3(json.getString("hindi_opt3"));
                startTest.setHindi_opt4(json.getString("hindi_opt4"));
                startTest.setHindi_opt5(json.getString("hindi_opt5"));

                testProgressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startTestList.add(startTest);
        }
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (!startTestList.isEmpty()) {
            adapter = new StartTestAdapter(this,startTestList);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initListeners() {
    }

    private void initObjects() {
        questionPaletteFragment = new QuestionPaletteFragment();
        startTestList = new ArrayList<>();
        session = new SessionManager(getApplicationContext());
    }

    private void initViews() {
        testProgressBar = findViewById(R.id.testProgressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    public void onBackPressed() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (session.isLoggedIn() || googleApiClient != null && googleApiClient.isConnected() || AccessToken.getCurrentAccessToken() != null){
                    Intent intent = new Intent(StartTestActivity.this,DashboardActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(StartTestActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);

        counter = menu.findItem(R.id.action_time);
        offTimer = menu.findItem(R.id.action_timer_off);
        onTimer = menu.findItem(R.id.action_timer);

        updateCountDownText();

        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_translate), getResources().getString(R.string.language)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_visibility), getResources().getString(R.string.night_mode)));
        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_profile), getResources().getString(R.string.profile)));
        menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_info), getResources().getString(R.string.instruction)));
        menu.add(0, 5, 5, menuIconWithText(getResources().getDrawable(R.drawable.ic_action_question), getResources().getString(R.string.question_paper)));

        startTimer();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mTimerRunning && !mSecondTimerRunning) {
            onTimer.setVisible(false);
            offTimer.setVisible(true);
            if (id == R.id.action_timer){
                pauseTimer();
                startSecondTimer();
                Toast.makeText(this, "You can pause test 4 minutes", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            offTimer.setVisible(false);
            onTimer.setVisible(true);
            if (id == R.id.action_timer_off){
                if (!timeLeftFormatted2.equals("00:01")){
                    pauseSecondTimer();
                    startTimer();
                    Toast.makeText(this, "Pause Time Left : "+timeLeftFormatted2, Toast.LENGTH_LONG).show();
                }
            }
        }

        switch (id) {
            case 1:
                showChangeLanguageDialog();
                return true;
            case 2:
                showChangeNightModeDialog();
                return true;
            case 3:
                showProfileDialog();
                return true;
            case 4:
                showInstructionDialog();
                return true;
            case 5:
                showQuestionPaperDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showQuestionPaperDialog() {
        final android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
        builder1.setCancelable(true);

        LayoutInflater inflater =LayoutInflater.from(this);
        final View layout_que = inflater.inflate(R.layout.layout_que,null);

        txtQuestion = (TextView) layout_que.findViewById(R.id.txtQuestion);
        imgQuestion = (ImageView) layout_que.findViewById(R.id.imgQuestion);
        final Button btnCancel = layout_que.findViewById(R.id.btnCancel);

        loadQuestionPaper();

        builder1.setView(layout_que);

        final android.app.AlertDialog alertDialog = builder1.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadQuestionPaper() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Common.question_paper+"?testName="+strTestId+"&test_type="+strTestType,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    JSONArray jsonArray = response.getJSONArray("Question");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        pattern = Pattern.compile(imgRegex);
                        int no  = i+1;

                        if (language.equals("en")){
                            String question = object.getString("question");

                            matcherQuestion = pattern.matcher(question);
                            if (matcherQuestion.find()){
                                String strQuestion = question.replaceAll("\"","@@@");

                                String[] arrayString = strQuestion.split("@@@");
                                String subImgQuestion = arrayString[1];

                                txtQuestion.append("Q."+no+"  "+subImgQuestion+"\n\n");
                                //Picasso.with(context).load(subImgQuestion).into(imgQuestion);
                                imgQuestion.setVisibility(View.VISIBLE);
                                txtQuestion.setVisibility(View.VISIBLE);
                            }
                            else {
                                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                                {
                                    String str= String.valueOf(Html.fromHtml(question,Html.FROM_HTML_MODE_LEGACY));
                                    txtQuestion.append("Q."+no+"  "+str+"\n\n");
                                }
                                else {
                                    String str= String.valueOf(Html.fromHtml(question));
                                    txtQuestion.append("Q."+no+"  "+str+"\n\n");
                                }
                                txtQuestion.setVisibility(View.VISIBLE);
                                imgQuestion.setVisibility(View.GONE);
                            }
                        }
                        else if (language.equals("hi")){
                            String hindiQuestion = object.getString("hindi_question");

                            matcherQuestion = pattern.matcher(hindiQuestion);
                            if (matcherQuestion.find()){
                                String strHindiQuestion = hindiQuestion.replaceAll("\"","@@@");

                                String[] arrayString = strHindiQuestion.split("@@@");
                                String subHindiImgQuestion = arrayString[1];

                                txtQuestion.append("Q."+no+"  "+subHindiImgQuestion+"\n\n");
                                Picasso.with(StartTestActivity.this).load(subHindiImgQuestion).into(imgQuestion);
                                imgQuestion.setVisibility(View.VISIBLE);
                                txtQuestion.setVisibility(View.VISIBLE);
                            }
                            else {
                                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                                {
                                    String str= String.valueOf(Html.fromHtml(hindiQuestion,Html.FROM_HTML_MODE_LEGACY));
                                    txtQuestion.append("Q."+no+"  "+str+"\n\n");
                                }
                                else {
                                    String str= String.valueOf(Html.fromHtml(hindiQuestion));
                                    txtQuestion.append("Q."+no+"  "+str+"\n\n");
                                }
                                txtQuestion.setVisibility(View.VISIBLE);
                                imgQuestion.setVisibility(View.GONE);
                            }
                        }
                    }
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
        MySingleton.getInstance(StartTestActivity.this).addToRequestque(jsonObjectRequest);

    }

    private void showInstructionDialog() {
        final android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
        builder1.setCancelable(true);

        LayoutInflater inflater =LayoutInflater.from(this);
        final View layout_info = inflater.inflate(R.layout.layout_info,null);

        final Button btnCancel = layout_info.findViewById(R.id.btnCancel);

        builder1.setView(layout_info);

        final android.app.AlertDialog alertDialog = builder1.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showProfileDialog() {
        final android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
        builder1.setCancelable(true);

        LayoutInflater inflater =LayoutInflater.from(this);
        final View layout_prof = inflater.inflate(R.layout.layout_prof,null);

        ImageView imgProfile = layout_prof.findViewById(R.id.imgProfile);
        TextView txtName = (TextView) layout_prof.findViewById(R.id.txtName);
        TextView txtEmail = (TextView) layout_prof.findViewById(R.id.txtEmail);
        TextView txtPhone = (TextView) layout_prof.findViewById(R.id.txtPhone);
        final Button btnCancel = layout_prof.findViewById(R.id.btnCancel);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("StudentProfile", Context.MODE_PRIVATE);
        Picasso.with(this).load(preferences.getString("PROFILE_PIC",null)).fit().into(imgProfile);
        txtName.setText(preferences.getString("NAME","N/A"));
        txtEmail.setText(preferences.getString("EMAIL","N/A"));
        txtPhone.setText(preferences.getString("MOBILE","N/A"));

        builder1.setView(layout_prof);

        final android.app.AlertDialog alertDialog = builder1.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(StartTestActivity.this);
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
        language  = preferences.getString("My_Lang","");
        setLocate(language);
    }

    private void showChangeNightModeDialog() {
        final String[] listItems ={"Turn Off","Turn On"};

        AlertDialog.Builder builder = new AlertDialog.Builder(StartTestActivity.this);
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

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    private void startSecondTimer() {
        mSecondCountDownTimer = new CountDownTimer(mSecondTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSecondTimeLeftInMillis = millisUntilFinished;
                updateSecondCountDownText();
            }

            @Override
            public void onFinish() {
                mSecondTimerRunning = false;
            }
        }.start();

        mSecondTimerRunning = true;
    }

    private void pauseSecondTimer() {
        mSecondCountDownTimer.cancel();
        mSecondTimerRunning = false;
    }

    private void updateSecondCountDownText() {
        int minutes = (int) (mSecondTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mSecondTimeLeftInMillis / 1000) % 60;

        timeLeftFormatted2 = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        if (timeLeftFormatted2.equals("00:01")){
            offTimer.setVisible(false);
            onTimer.setVisible(true);
            startTimer();
            Toast.makeText(this, "You cant pause timer", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        counter.setTitle(timeLeftFormatted);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        switch(id) {
            case R.id.bottom_action_review:
                break;
            case R.id.bottom_action_clear:
                break;
            case R.id.bottom_action_palette:
                questionPaletteFragment.show(getSupportFragmentManager(), questionPaletteFragment.getTag());
                break;
            case R.id.bottom_action_save:
                break;
            case R.id.bottom_action_submit:
                break;
        }
        return true;
    }

}
