package com.idealtechcontrivance.ashish.examcracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.idealtechcontrivance.ashish.examcracker.Model.StartTest;
import com.idealtechcontrivance.ashish.examcracker.Model.Testimonials;
import com.idealtechcontrivance.ashish.examcracker.R;
import com.idealtechcontrivance.ashish.examcracker.Utility.TextJustification;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartTestAdapter extends RecyclerView.Adapter<StartTestAdapter.ViewHolder> {

    private Context context;
    private String strLanguage;
    private String count;

    private String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
    private Pattern pattern;
    private Matcher matcherPassage, matcherQuestion, matcherHindiPassage, matcherHindiQuestion, matcherOpt1, matcherOpt2, matcherOpt3, matcherOpt4 , matcherOpt5, matcherHindiOpt1, matcherHindiOpt2, matcherHindiOpt3, matcherHindiOpt4, matcherHindiOpt5;

    private String strPassage, strSolution, strQuestion, strHindiPassage, strHindiQuestion, strHindiSolution;
    private String strImgPassage, strImgQuestion, strImgHindiPassage, strImgHindiQuestion, strImgOpt1, strImgOpt2, strImgOpt3, strImgOpt4 , strImgOpt5, strImgHindiOpt1, strImgHindiOpt2, strImgHindiOpt3, strImgHindiOpt4, strImgHindiOpt5;
    private String strOpt1, strOpt2, strOpt3, strOpt4 , strOpt5, strHindiOpt1, strHindiOpt2, strHindiOpt3, strHindiOpt4, strHindiOpt5;

    private List<StartTest> startTestList;

    int selectedPosition;
    SparseBooleanArray sparseBooleanArray; // for identifying: in list which items are selected

    public StartTestAdapter(Context context, List<StartTest> startTestList){
        super();
        this.context = context;
        this.startTestList = startTestList;
        sparseBooleanArray = new SparseBooleanArray();
    }

    @Override
    public StartTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_start_test, parent, false);
        StartTestAdapter.ViewHolder viewHolder = new StartTestAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StartTestAdapter.ViewHolder holder, final int position) {

        StartTest startTest =  startTestList.get(position);

        selectedPosition = position;

        count = String.valueOf(position+1);
        holder.txtNumber.setText(count);

        pattern = Pattern.compile(imgRegex);

        holder.setPassage(startTest.getPassage());
        holder.setQuestion(startTest.getQuestion());
        holder.setOptions(startTest, position);

        holder.setHindiPassage(startTest.getHindi_passage());
        holder.setHindiQuestion(startTest.getHindi_question());
        holder.setHindiOptions(startTest, position);

    }

    @Override
    public int getItemCount() {
        return startTestList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtNumber;

        LinearLayout viewEnglish, viewHindi, enLayout1, enLayout2, enLayout3, enLayout4, enLayout5, hiLayout1, hiLayout2 , hiLayout3 ,hiLayout4, hiLayout5;
        View enView, hiView;

        ImageView imgPassage, imgQuestion, imgOpt1, imgOpt2, imgOpt3, imgOpt4, imgOpt5, imgHindiPassage, imgHindiQuestion, imgHindiOpt1, imgHindiOpt2, imgHindiOpt3, imgHindiOpt4, imgHindiOpt5;
        TextView txtPassage, txtSolution, txtQuestion, txtHindiPassage, txtHindiQuestion, txtHindiSolution;

        TextView opt1, opt2, opt3, opt4 , opt5, hindiOpt1, hindiOpt2, hindiOpt3, hindiOpt4, hindiOpt5;
        TextView enIcon1, enIcon2, enIcon3, enIcon4 , enIcon5, hiIcon1, hiIcon2, hiIcon3, hiIcon4 , hiIcon5;

        public ViewHolder(View itemView) {
            super(itemView);
            loadLocal();

            viewEnglish = (LinearLayout) itemView.findViewById(R.id.viewEnglish);
            viewHindi = (LinearLayout) itemView.findViewById(R.id.viewHindi);

            if (strLanguage.equals("en")){
                viewHindi.setVisibility(View.GONE);
            }
            else if (strLanguage.equals("hi")){
                viewEnglish.setVisibility(View.GONE);
            }

            enLayout1 = itemView.findViewById(R.id.enLayout1);
            enLayout2 = itemView.findViewById(R.id.enLayout2);
            enLayout3 = itemView.findViewById(R.id.enLayout3);
            enLayout4 = itemView.findViewById(R.id.enLayout4);
            enLayout5 = itemView.findViewById(R.id.enLayout5);

            hiLayout1 = itemView.findViewById(R.id.hiLayout1);
            hiLayout2 = itemView.findViewById(R.id.hiLayout2);
            hiLayout3 = itemView.findViewById(R.id.hiLayout3);
            hiLayout4 = itemView.findViewById(R.id.hiLayout4);
            hiLayout5 = itemView.findViewById(R.id.hiLayout5);

            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);

            enView = itemView.findViewById(R.id.enView);
            txtPassage=(TextView)itemView.findViewById(R.id.txtPassage);
            txtQuestion=(TextView)itemView.findViewById(R.id.txtQuestion);

            enIcon1=itemView.findViewById(R.id.enIcon1);
            enIcon2=itemView.findViewById(R.id.enIcon2);
            enIcon3=itemView.findViewById(R.id.enIcon3);
            enIcon4=itemView.findViewById(R.id.enIcon4);
            enIcon5=itemView.findViewById(R.id.enIcon5);

            opt1=itemView.findViewById(R.id.opt1);
            opt2=itemView.findViewById(R.id.opt2);
            opt3=itemView.findViewById(R.id.opt3);
            opt4=itemView.findViewById(R.id.opt4);
            opt5=itemView.findViewById(R.id.opt5);

            imgPassage =(ImageView) itemView.findViewById(R.id.imgPassage);
            imgQuestion =(ImageView) itemView.findViewById(R.id.imgQuestion);

            imgOpt1 =(ImageView) itemView.findViewById(R.id.imgOpt1);
            imgOpt2 =(ImageView) itemView.findViewById(R.id.imgOpt2);
            imgOpt3 =(ImageView) itemView.findViewById(R.id.imgOpt3);
            imgOpt4 =(ImageView) itemView.findViewById(R.id.imgOpt4);
            imgOpt5 =(ImageView) itemView.findViewById(R.id.imgOpt5);

            hiView = itemView.findViewById(R.id.hiView);
            txtHindiPassage=itemView.findViewById(R.id.txtHindiPassage);
            txtHindiQuestion=itemView.findViewById(R.id.txtHindiQuestion);

            hiIcon1=itemView.findViewById(R.id.hiIcon1);
            hiIcon2=itemView.findViewById(R.id.hiIcon2);
            hiIcon3=itemView.findViewById(R.id.hiIcon3);
            hiIcon4=itemView.findViewById(R.id.hiIcon4);
            hiIcon5=itemView.findViewById(R.id.hiIcon5);

            hindiOpt1=itemView.findViewById(R.id.hindiOpt1);
            hindiOpt2=itemView.findViewById(R.id.hindiOpt2);
            hindiOpt3=itemView.findViewById(R.id.hindiOpt3);
            hindiOpt4=itemView.findViewById(R.id.hindiOpt4);
            hindiOpt5=itemView.findViewById(R.id.hindiOpt5);

            imgHindiPassage = itemView.findViewById(R.id.imgHindiPassage);
            imgHindiQuestion = itemView.findViewById(R.id.imgHindiQuestion);
            imgHindiOpt1 = itemView.findViewById(R.id.imgHindiOpt1);
            imgHindiOpt2 = itemView.findViewById(R.id.imgHindiOpt2);
            imgHindiOpt3 = itemView.findViewById(R.id.imgHindiOpt3);
            imgHindiOpt4 = itemView.findViewById(R.id.imgHindiOpt4);
            imgHindiOpt5 = itemView.findViewById(R.id.imgHindiOpt5);
        }

        private void loadLocal(){
            SharedPreferences preferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            strLanguage = preferences.getString("My_Lang","");
        }

        void setPassage(String passage) {
            if (!passage.equals("null")){
                matcherPassage = pattern.matcher(passage);
                if (matcherPassage.find()){
                    strPassage = passage.replaceAll("\"","@@@");

                    String[] arrayString = strPassage.split("@@@");
                    strImgPassage = arrayString[1];

                    Picasso.with(context).load(strImgPassage).into(imgPassage);
                    imgPassage.setVisibility(View.VISIBLE);
                    enView.setVisibility(View.VISIBLE);
                    txtPassage.setVisibility(View.GONE);
                }
                else {
                    if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    {
                        txtPassage.setText(Html.fromHtml(passage,Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        txtPassage.setText(Html.fromHtml(passage));
                    }
                    txtPassage.setVisibility(View.VISIBLE);
                    enView.setVisibility(View.VISIBLE);
                    imgPassage.setVisibility(View.GONE);
                }
            }
            else {
                txtPassage.setVisibility(View.GONE);
                imgPassage.setVisibility(View.GONE);
                enView.setVisibility(View.GONE);
            }
        }

        void setQuestion(String question) {
            matcherQuestion = pattern.matcher(question);
            if (matcherQuestion.find()){
                strQuestion = question.replaceAll("\"","@@@");

                String[] arrayString = strQuestion.split("@@@");
                strImgQuestion = arrayString[1];

                Picasso.with(context).load(strImgQuestion).into(imgQuestion);
                imgQuestion.setVisibility(View.VISIBLE);
                txtQuestion.setVisibility(View.GONE);
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    txtQuestion.setText(Html.fromHtml(question,Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    txtQuestion.setText(Html.fromHtml(question));
                }
                txtQuestion.setVisibility(View.VISIBLE);
                imgQuestion.setVisibility(View.GONE);
            }

        }

        void setOptions(StartTest startTest, int position) {

            matcherOpt1 = pattern.matcher(startTest.getOpt1());
            if (matcherOpt1.find()){
                strOpt1 = startTest.getOpt1().replaceAll("\"","@@@");

                String[] arrayString = strOpt1.split("@@@");
                strImgOpt1 = arrayString[1];

                Picasso.with(context).load(strImgOpt1).into(imgOpt1);
                imgOpt1.setVisibility(View.VISIBLE);
                opt1.setVisibility(View.GONE);
                if (sparseBooleanArray.get(selectedPosition))
                {
                    enLayout1.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout1.setBackgroundColor(Color.WHITE);
                }
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    opt1.setText(Html.fromHtml(startTest.getOpt1(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    opt1.setText(Html.fromHtml(startTest.getOpt1()));
                }
                opt1.setVisibility(View.VISIBLE);
                imgOpt1.setVisibility(View.GONE);
                if (sparseBooleanArray.get(selectedPosition))
                {
                    enLayout1.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout1.setBackgroundColor(Color.WHITE);
                }
            }

            matcherOpt2 = pattern.matcher(startTest.getOpt2());
            if (matcherOpt2.find()){
                strOpt2 = startTest.getOpt2().replaceAll("\"","@@@");

                String[] arrayString = strOpt2.split("@@@");
                strImgOpt2 = arrayString[1];

                Picasso.with(context).load(strImgOpt2).into(imgOpt2);
                imgOpt2.setVisibility(View.VISIBLE);
                opt2.setVisibility(View.GONE);
                if (sparseBooleanArray.get(selectedPosition))
                {
                    enLayout2.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout2.setBackgroundColor(Color.WHITE);
                }
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    opt2.setText(Html.fromHtml(startTest.getOpt2(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    opt2.setText(Html.fromHtml(startTest.getOpt2()));
                }
                opt2.setVisibility(View.VISIBLE);
                imgOpt2.setVisibility(View.GONE);
                if (sparseBooleanArray.get(selectedPosition))
                {
                    enLayout2.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout2.setBackgroundColor(Color.WHITE);
                }
            }

            matcherOpt3 = pattern.matcher(startTest.getOpt3());
            if (matcherOpt3.find()){
                strOpt3 = startTest.getOpt3().replaceAll("\"","@@@");

                String[] arrayString = strOpt3.split("@@@");
                strImgOpt3 = arrayString[1];

                Picasso.with(context).load(strImgOpt3).into(imgOpt3);
                imgOpt3.setVisibility(View.VISIBLE);
                opt3.setVisibility(View.GONE);
                if (sparseBooleanArray.get(selectedPosition))
                {
                    enLayout3.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout3.setBackgroundColor(Color.WHITE);
                }
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    opt3.setText(Html.fromHtml(startTest.getOpt3(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    opt3.setText(Html.fromHtml(startTest.getOpt3()));
                }
                opt3.setVisibility(View.VISIBLE);
                imgOpt3.setVisibility(View.GONE);
                if (sparseBooleanArray.get(position))
                {
                    enLayout3.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout3.setBackgroundColor(Color.WHITE);
                }
            }

            matcherOpt4 = pattern.matcher(startTest.getOpt4());
            if (matcherOpt4.find()){
                strOpt4 = startTest.getOpt4().replaceAll("\"","@@@");

                String[] arrayString = strOpt4.split("@@@");
                strImgOpt4 = arrayString[1];

                Picasso.with(context).load(strImgOpt4).into(imgOpt4);
                imgOpt4.setVisibility(View.VISIBLE);
                opt4.setVisibility(View.GONE);
                if (sparseBooleanArray.get(position))
                {
                    enLayout4.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout4.setBackgroundColor(Color.WHITE);
                }
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    opt4.setText(Html.fromHtml(startTest.getOpt4(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    opt4.setText(Html.fromHtml(startTest.getOpt4()));
                }
                opt4.setVisibility(View.VISIBLE);
                imgOpt4.setVisibility(View.GONE);
                if (sparseBooleanArray.get(selectedPosition))
                {
                    enLayout4.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    enLayout4.setBackgroundColor(Color.WHITE);
                }
            }

            if (!startTest.getOpt5().equals("null")){
                matcherOpt5 = pattern.matcher(startTest.getOpt5());
                if (matcherOpt5.find()){
                    strOpt5 = startTest.getOpt5().replaceAll("\"","@@@");

                    String[] arrayString = strOpt5.split("@@@");
                    strImgOpt5 = arrayString[1];

                    Picasso.with(context).load(strImgOpt5).into(imgOpt5);
                    imgOpt5.setVisibility(View.VISIBLE);
                    opt5.setVisibility(View.GONE);
                    if (sparseBooleanArray.get(selectedPosition))
                    {
                        enLayout5.setBackgroundColor(Color.GREEN);
                    }
                    else
                    {
                        enLayout5.setBackgroundColor(Color.WHITE);
                    }
                }
                else {
                    if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    {
                        opt5.setText(Html.fromHtml(startTest.getOpt5(),Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        opt5.setText(Html.fromHtml(startTest.getOpt5()));
                    }
                    opt5.setVisibility(View.VISIBLE);
                    imgOpt5.setVisibility(View.GONE);
                    if (sparseBooleanArray.get(selectedPosition))
                    {
                        enLayout5.setBackgroundColor(Color.GREEN);
                    }
                    else
                    {
                        enLayout5.setBackgroundColor(Color.WHITE);
                    }
                }
            }
            else {
                enIcon5.setVisibility(View.GONE);
                opt5.setVisibility(View.GONE);
                imgOpt5.setVisibility(View.GONE);
            }

//            opt1.setOnClickListener(this);
//            opt2.setOnClickListener(this);
//            opt3.setOnClickListener(this);
//            opt4.setOnClickListener(this);
//            opt5.setOnClickListener(this);

            enLayout1.setOnClickListener(this);
        }

        void setHindiPassage(String hindi_passage) {

            if (!hindi_passage.equals("null")){
                matcherHindiPassage = pattern.matcher(hindi_passage);
                if (matcherHindiPassage.find()){
                    strHindiPassage = hindi_passage.replaceAll("\"","@@@");

                    String[] arrayString = strHindiPassage.split("@@@");
                    strImgHindiPassage = arrayString[1];

                    Picasso.with(context).load(strImgHindiPassage).into(imgHindiPassage);
                    hiView.setVisibility(View.VISIBLE);
                    imgHindiPassage.setVisibility(View.VISIBLE);
                    txtHindiPassage.setVisibility(View.GONE);
                }
                else {
                    if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    {
                        txtHindiPassage.setText(Html.fromHtml(hindi_passage,Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        txtHindiPassage.setText(Html.fromHtml(hindi_passage));
                    }
                    hiView.setVisibility(View.VISIBLE);
                    txtHindiPassage.setVisibility(View.VISIBLE);
                    imgHindiPassage.setVisibility(View.GONE);
                }
            }
            else {
                hiView.setVisibility(View.GONE);
                txtHindiPassage.setVisibility(View.GONE);
                imgHindiPassage.setVisibility(View.GONE);
            }

        }

        void setHindiQuestion(String hindi_question) {
            matcherHindiQuestion = pattern.matcher(hindi_question);
            if (matcherHindiQuestion.find()){
                strHindiQuestion = hindi_question.replaceAll("\"","@@@");

                String[] arrayString = strHindiQuestion.split("@@@");
                strImgHindiQuestion = arrayString[1];

                Picasso.with(context).load(strImgHindiQuestion).into(imgHindiQuestion);
                imgHindiQuestion.setVisibility(View.VISIBLE);
                txtHindiQuestion.setVisibility(View.GONE);
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    txtHindiQuestion.setText(Html.fromHtml(hindi_question,Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    txtHindiQuestion.setText(Html.fromHtml(hindi_question));
                }
                txtHindiQuestion.setVisibility(View.VISIBLE);
                imgHindiQuestion.setVisibility(View.GONE);
            }
        }

        void setHindiOptions(final StartTest startTest, int position) {

            matcherHindiOpt1 = pattern.matcher(startTest.getHindi_opt1());
            if (matcherHindiOpt1.find()){
                strHindiOpt1 = startTest.getHindi_opt1().replaceAll("\"","@@@");

                String[] arrayString = strHindiOpt1.split("@@@");
                strImgHindiOpt1 = arrayString[1];

                Picasso.with(context).load(strImgHindiOpt1).into(imgHindiOpt1);
                imgHindiOpt1.setVisibility(View.VISIBLE);
                hindiOpt1.setVisibility(View.GONE);
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    hindiOpt1.setText(Html.fromHtml(startTest.getHindi_opt1(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    hindiOpt1.setText(Html.fromHtml(startTest.getHindi_opt1()));
                }
                hindiOpt1.setVisibility(View.VISIBLE);
                imgHindiOpt1.setVisibility(View.GONE);
            }

            matcherHindiOpt2 = pattern.matcher(startTest.getHindi_opt2());
            if (matcherHindiOpt2.find()){
                strHindiOpt2 = startTest.getHindi_opt2().replaceAll("\"","@@@");

                String[] arrayString = strHindiOpt2.split("@@@");
                strImgHindiOpt2 = arrayString[1];

                Picasso.with(context).load(strImgHindiOpt2).into(imgHindiOpt2);
                imgHindiOpt2.setVisibility(View.VISIBLE);
                hindiOpt2.setVisibility(View.GONE);
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    hindiOpt2.setText(Html.fromHtml(startTest.getHindi_opt2(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    hindiOpt2.setText(Html.fromHtml(startTest.getHindi_opt2()));
                }
                hindiOpt2.setVisibility(View.VISIBLE);
                imgHindiOpt2.setVisibility(View.GONE);
            }

            matcherHindiOpt3 = pattern.matcher(startTest.getHindi_opt3());
            if (matcherHindiOpt3.find()){
                strHindiOpt3 = startTest.getHindi_opt3().replaceAll("\"","@@@");

                String[] arrayString = strHindiOpt3.split("@@@");
                strImgHindiOpt3 = arrayString[1];

                Picasso.with(context).load(strImgHindiOpt3).into(imgHindiOpt3);
                imgHindiOpt3.setVisibility(View.VISIBLE);
                hindiOpt3.setVisibility(View.GONE);
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    hindiOpt3.setText(Html.fromHtml(startTest.getHindi_opt3(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    hindiOpt3.setText(Html.fromHtml(startTest.getHindi_opt3()));
                }
                hindiOpt3.setVisibility(View.VISIBLE);
                imgHindiOpt3.setVisibility(View.GONE);
            }

            matcherHindiOpt4 = pattern.matcher(startTest.getHindi_opt4());
            if (matcherHindiOpt4.find()){
                strHindiOpt4 = startTest.getHindi_opt4().replaceAll("\"","@@@");

                String[] arrayString = strHindiOpt4.split("@@@");
                strImgHindiOpt4 = arrayString[1];

                Picasso.with(context).load(strImgHindiOpt4).into(imgHindiOpt4);
                imgHindiOpt4.setVisibility(View.VISIBLE);
                hindiOpt4.setVisibility(View.GONE);
            }
            else {
                if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                {
                    hindiOpt4.setText(Html.fromHtml(startTest.getHindi_opt4(),Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    hindiOpt4.setText(Html.fromHtml(startTest.getHindi_opt4()));
                }
                hindiOpt4.setVisibility(View.VISIBLE);
                imgHindiOpt4.setVisibility(View.GONE);
            }

            if (!startTest.getHindi_opt5().equals("null")){
                matcherHindiOpt5 = pattern.matcher(startTest.getHindi_opt5());
                if (matcherHindiOpt5.find()){
                    strHindiOpt5 = startTest.getHindi_opt5().replaceAll("\"","@@@");

                    String[] arrayString = strHindiOpt5.split("@@@");
                    strImgHindiOpt5 = arrayString[1];

                    Picasso.with(context).load(strImgHindiOpt5).into(imgHindiOpt5);
                    hiIcon5.setVisibility(View.VISIBLE);
                    imgHindiOpt5.setVisibility(View.VISIBLE);
                    hindiOpt5.setVisibility(View.GONE);
                }
                else {
                    if (  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                    {
                        hindiOpt5.setText(Html.fromHtml(startTest.getHindi_opt5(),Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        hindiOpt5.setText(Html.fromHtml(startTest.getHindi_opt5()));
                    }
                    hiIcon5.setVisibility(View.VISIBLE);
                    hindiOpt5.setVisibility(View.VISIBLE);
                    imgHindiOpt5.setVisibility(View.GONE);
                }
            }
            else {
                hindiOpt5.setVisibility(View.GONE);
                imgHindiOpt5.setVisibility(View.GONE);
                hiIcon5.setVisibility(View.GONE);
            }

            hindiOpt1.setOnClickListener(this);
            hindiOpt2.setOnClickListener(this);
            hindiOpt3.setOnClickListener(this);
            hindiOpt4.setOnClickListener(this);
            hindiOpt5.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.enLayout1){
                if (!sparseBooleanArray.get(getAdapterPosition()))
                {
                    sparseBooleanArray.put(getAdapterPosition(),true);
                    //selectedItemCount++;
                    //listener.selectedItemCount(selectedItemCount); // calling the method in main activity Because: in our case mainActivity our created interface for clicklisteners
                    notifyItemChanged(getAdapterPosition());
                }
                else // if clicked item is already selected
                {
                    sparseBooleanArray.put(getAdapterPosition(),false);
                    //selectedItemCount--;
                    //listener.selectedItemCount(selectedItemCount); // calling the method in main activity Because: in our case mainActivity our created interface for clicklisteners
                    notifyItemChanged(getAdapterPosition());
                }
            }
            else if (id == R.id.enLayout2) {
                if (!sparseBooleanArray.get(getAdapterPosition())) {
                    sparseBooleanArray.put(getAdapterPosition(), true);
                    //selectedItemCount++;
                    //listener.selectedItemCount(selectedItemCount); // calling the method in main activity Because: in our case mainActivity our created interface for clicklisteners
                    notifyItemChanged(getAdapterPosition());
                } else // if clicked item is already selected
                {
                    sparseBooleanArray.put(getAdapterPosition(), false);
                    //selectedItemCount--;
                    //listener.selectedItemCount(selectedItemCount); // calling the method in main activity Because: in our case mainActivity our created interface for clicklisteners
                    notifyItemChanged(getAdapterPosition());
                }
            }
            else if (id == R.id.enLayout3) {
                if (!sparseBooleanArray.get(getAdapterPosition())) {
                    sparseBooleanArray.put(getAdapterPosition(), true);
                    //selectedItemCount++;
                    //listener.selectedItemCount(selectedItemCount); // calling the method in main activity Because: in our case mainActivity our created interface for clicklisteners
                    notifyItemChanged(getAdapterPosition());
                } else // if clicked item is already selected
                {
                    sparseBooleanArray.put(getAdapterPosition(), false);
                    //selectedItemCount--;
                    //listener.selectedItemCount(selectedItemCount); // calling the method in main activity Because: in our case mainActivity our created interface for clicklisteners
                    notifyItemChanged(getAdapterPosition());
                }
            }
        }

    }
}
