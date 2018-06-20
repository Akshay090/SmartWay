package com.idealtechcontrivance.ashish.examcracker.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idealtechcontrivance.ashish.examcracker.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionPaletteFragment extends BottomSheetDialogFragment {

    View view;
    TextView opt1, opt2, opt3, opt4, opt5, opt6, opt7, opt8, opt9, opt10, opt11, opt12, opt13, opt14, opt15, opt16, opt17, opt18, opt19, opt20;
    TextView opt21, opt22, opt23, opt24, opt25, opt26, opt27, opt28, opt29, opt30, opt31, opt32, opt33, opt34, opt35, opt36, opt37, opt38, opt39, opt40;
    TextView opt41, opt42, opt43, opt44, opt45, opt46, opt47, opt48, opt49, opt50, opt51, opt52, opt53, opt54, opt55, opt56, opt57, opt58, opt59, opt60;
    TextView opt61, opt62, opt63, opt64, opt65, opt66, opt67, opt68, opt69, opt70, opt71, opt72, opt73, opt74, opt75, opt76, opt77, opt78, opt79, opt80;
    TextView opt81, opt82, opt83, opt84, opt85, opt86, opt87, opt88, opt89, opt90, opt91, opt92, opt93, opt94, opt95, opt96, opt97, opt98, opt99, opt100;
    TextView opt101, opt102, opt103, opt104, opt105, opt106, opt107, opt108, opt109, opt110, opt111, opt112, opt113, opt114, opt115, opt116, opt117, opt118, opt119, opt120;
    TextView opt121, opt122, opt123, opt124, opt125, opt126, opt127, opt128, opt129, opt130, opt131, opt132, opt133, opt134, opt135, opt136, opt137, opt138, opt139, opt140;
    TextView opt141, opt142, opt143, opt144, opt145, opt146, opt147, opt148, opt149, opt150;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_question_palette, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_question_palette, container, false);

        initViews();

        SharedPreferences preferences =getActivity().getSharedPreferences("QuestionPalette",MODE_PRIVATE);
        int question  = preferences.getInt("NoQuestion",0);

        for (int i = 0; i <= question; i++){

            if ((i) == 1)
            {
                opt1.setVisibility(View.VISIBLE);
            }
            else if ((i) == 2){
                opt2.setVisibility(View.VISIBLE);
            }
            else if ((i) == 3){
                opt3.setVisibility(View.VISIBLE);
            }
            else if ((i) == 4){
                opt4.setVisibility(View.VISIBLE);
            }
            else if ((i) == 5){
                opt5.setVisibility(View.VISIBLE);
            }
            else if ((i) == 6){
                opt6.setVisibility(View.VISIBLE);
            }
            else if ((i) == 7){
                opt7.setVisibility(View.VISIBLE);
            }
            else if ((i) == 8){
                opt8.setVisibility(View.VISIBLE);
            }
            else if ((i) == 9){
                opt9.setVisibility(View.VISIBLE);
            }
            else if ((i) == 10){
                opt10.setVisibility(View.VISIBLE);
            }
            else if ((i) == 11){
                opt11.setVisibility(View.VISIBLE);
            }
            else if ((i) == 12){
                opt12.setVisibility(View.VISIBLE);
            }
            else if ((i) == 13){
                opt13.setVisibility(View.VISIBLE);
            }
            else if ((i) == 14){
                opt14.setVisibility(View.VISIBLE);
            }
            else if ((i) == 15){
                opt15.setVisibility(View.VISIBLE);
            }
            else if ((i) == 16){
                opt16.setVisibility(View.VISIBLE);
            }
            else if ((i) == 17){
                opt17.setVisibility(View.VISIBLE);
            }
            else if ((i) == 18){
                opt18.setVisibility(View.VISIBLE);
            }
            else if ((i) == 19){
                opt19.setVisibility(View.VISIBLE);
            }
            else if ((i) == 20){
                opt20.setVisibility(View.VISIBLE);
            }
            else if ((i) == 21){
                opt21.setVisibility(View.VISIBLE);
            }
            else if ((i) == 22){
                opt22.setVisibility(View.VISIBLE);
            }
            else if ((i) == 23){
                opt23.setVisibility(View.VISIBLE);
            }
            else if ((i) == 24){
                opt24.setVisibility(View.VISIBLE);
            }
            else if ((i) == 25){
                opt25.setVisibility(View.VISIBLE);
            }
            else if ((i) == 26)
            {
                opt26.setVisibility(View.VISIBLE);
            }
            else if ((i) == 27){
                opt27.setVisibility(View.VISIBLE);
            }
            else if ((i) == 28){
                opt28.setVisibility(View.VISIBLE);
            }
            else if ((i) == 29){
                opt29.setVisibility(View.VISIBLE);
            }
            else if ((i) == 30){
                opt30.setVisibility(View.VISIBLE);
            }
            else if ((i) == 31){
                opt31.setVisibility(View.VISIBLE);
            }
            else if ((i) == 32){
                opt32.setVisibility(View.VISIBLE);
            }
            else if ((i) == 33){
                opt33.setVisibility(View.VISIBLE);
            }
            else if ((i) == 34){
                opt34.setVisibility(View.VISIBLE);
            }
            else if ((i) == 35){
                opt35.setVisibility(View.VISIBLE);
            }
            else if ((i) == 36){
                opt36.setVisibility(View.VISIBLE);
            }
            else if ((i) == 37){
                opt37.setVisibility(View.VISIBLE);
            }
            else if ((i) == 38){
                opt38.setVisibility(View.VISIBLE);
            }
            else if ((i) == 39){
                opt39.setVisibility(View.VISIBLE);
            }
            else if ((i) == 40){
                opt40.setVisibility(View.VISIBLE);
            }
            else if ((i) == 41){
                opt41.setVisibility(View.VISIBLE);
            }
            else if ((i) == 42){
                opt42.setVisibility(View.VISIBLE);
            }
            else if ((i) == 43){
                opt43.setVisibility(View.VISIBLE);
            }
            else if ((i) == 44){
                opt44.setVisibility(View.VISIBLE);
            }
            else if ((i) == 45){
                opt45.setVisibility(View.VISIBLE);
            }
            else if ((i) == 46){
                opt46.setVisibility(View.VISIBLE);
            }
            else if ((i) == 47){
                opt47.setVisibility(View.VISIBLE);
            }
            else if ((i) == 48){
                opt48.setVisibility(View.VISIBLE);
            }
            else if ((i) == 49){
                opt49.setVisibility(View.VISIBLE);
            }
            else if ((i) == 50){
                opt50.setVisibility(View.VISIBLE);
            }
            else  if ((i) == 51)
            {
                opt51.setVisibility(View.VISIBLE);
            }
            else if ((i) == 52){
                opt52.setVisibility(View.VISIBLE);
            }
            else if ((i) == 53){
                opt53.setVisibility(View.VISIBLE);
            }
            else if ((i) == 54){
                opt54.setVisibility(View.VISIBLE);
            }
            else if ((i) == 55){
                opt55.setVisibility(View.VISIBLE);
            }
            else if ((i) == 56){
                opt56.setVisibility(View.VISIBLE);
            }
            else if ((i) == 57){
                opt57.setVisibility(View.VISIBLE);
            }
            else if ((i) == 58){
                opt58.setVisibility(View.VISIBLE);
            }
            else if ((i) == 59){
                opt59.setVisibility(View.VISIBLE);
            }
            else if ((i) == 60){
                opt60.setVisibility(View.VISIBLE);
            }
            else if ((i) == 61){
                opt61.setVisibility(View.VISIBLE);
            }
            else if ((i) == 62){
                opt62.setVisibility(View.VISIBLE);
            }
            else if ((i) == 63){
                opt63.setVisibility(View.VISIBLE);
            }
            else if ((i) == 64){
                opt64.setVisibility(View.VISIBLE);
            }
            else if ((i) == 65){
                opt65.setVisibility(View.VISIBLE);
            }
            else if ((i) == 66){
                opt66.setVisibility(View.VISIBLE);
            }
            else if ((i) == 67){
                opt67.setVisibility(View.VISIBLE);
            }
            else if ((i) == 68){
                opt68.setVisibility(View.VISIBLE);
            }
            else if ((i) == 69){
                opt69.setVisibility(View.VISIBLE);
            }
            else if ((i) == 70){
                opt70.setVisibility(View.VISIBLE);
            }
            else if ((i) == 71){
                opt71.setVisibility(View.VISIBLE);
            }
            else if ((i) == 72){
                opt72.setVisibility(View.VISIBLE);
            }
            else if ((i) == 73){
                opt73.setVisibility(View.VISIBLE);
            }
            else if ((i) == 74){
                opt74.setVisibility(View.VISIBLE);
            }
            else if ((i) == 75){
                opt75.setVisibility(View.VISIBLE);
            }
            else if ((i) == 76)
            {
                opt76.setVisibility(View.VISIBLE);
            }
            else if ((i) == 77){
                opt77.setVisibility(View.VISIBLE);
            }
            else if ((i) == 78){
                opt78.setVisibility(View.VISIBLE);
            }
            else if ((i) == 79){
                opt79.setVisibility(View.VISIBLE);
            }
            else if ((i) == 80){
                opt80.setVisibility(View.VISIBLE);
            }
            else if ((i) == 81){
                opt81.setVisibility(View.VISIBLE);
            }
            else if ((i) == 82){
                opt82.setVisibility(View.VISIBLE);
            }
            else if ((i) == 83){
                opt83.setVisibility(View.VISIBLE);
            }
            else if ((i) == 84){
                opt84.setVisibility(View.VISIBLE);
            }
            else if ((i) == 85){
                opt85.setVisibility(View.VISIBLE);
            }
            else if ((i) == 86){
                opt86.setVisibility(View.VISIBLE);
            }
            else if ((i) == 87){
                opt87.setVisibility(View.VISIBLE);
            }
            else if ((i) == 88){
                opt88.setVisibility(View.VISIBLE);
            }
            else if ((i) == 89){
                opt89.setVisibility(View.VISIBLE);
            }
            else if ((i) == 90){
                opt90.setVisibility(View.VISIBLE);
            }
            else if ((i) == 91){
                opt91.setVisibility(View.VISIBLE);
            }
            else if ((i) == 92){
                opt92.setVisibility(View.VISIBLE);
            }
            else if ((i) == 93){
                opt93.setVisibility(View.VISIBLE);
            }
            else if ((i) == 94){
                opt94.setVisibility(View.VISIBLE);
            }
            else if ((i) == 95){
                opt95.setVisibility(View.VISIBLE);
            }
            else if ((i) == 96){
                opt96.setVisibility(View.VISIBLE);
            }
            else if ((i) == 97){
                opt97.setVisibility(View.VISIBLE);
            }
            else if ((i) == 98){
                opt98.setVisibility(View.VISIBLE);
            }
            else if ((i) == 99){
                opt99.setVisibility(View.VISIBLE);
            }
            else if ((i) == 100){
                opt100.setVisibility(View.VISIBLE);
            }
            else if ((i) == 101)
            {
                opt101.setVisibility(View.VISIBLE);
            }
            else if ((i) == 102){
                opt102.setVisibility(View.VISIBLE);
            }
            else if ((i) == 103){
                opt103.setVisibility(View.VISIBLE);
            }
            else if ((i) == 104){
                opt104.setVisibility(View.VISIBLE);
            }
            else if ((i) == 105){
                opt105.setVisibility(View.VISIBLE);
            }
            else if ((i) == 106){
                opt106.setVisibility(View.VISIBLE);
            }
            else if ((i) == 107){
                opt107.setVisibility(View.VISIBLE);
            }
            else if ((i) == 108){
                opt108.setVisibility(View.VISIBLE);
            }
            else if ((i) == 109){
                opt109.setVisibility(View.VISIBLE);
            }
            else if ((i) == 110){
                opt110.setVisibility(View.VISIBLE);
            }
            else if ((i) == 111){
                opt111.setVisibility(View.VISIBLE);
            }
            else if ((i) == 112){
                opt112.setVisibility(View.VISIBLE);
            }
            else if ((i) == 113){
                opt113.setVisibility(View.VISIBLE);
            }
            else if ((i) == 114){
                opt114.setVisibility(View.VISIBLE);
            }
            else if ((i) == 115){
                opt115.setVisibility(View.VISIBLE);
            }
            else if ((i) == 116){
                opt116.setVisibility(View.VISIBLE);
            }
            else if ((i) == 117){
                opt117.setVisibility(View.VISIBLE);
            }
            else if ((i) == 118){
                opt118.setVisibility(View.VISIBLE);
            }
            else if ((i) == 119){
                opt119.setVisibility(View.VISIBLE);
            }
            else if ((i) == 120){
                opt120.setVisibility(View.VISIBLE);
            }
            else if ((i) == 121){
                opt121.setVisibility(View.VISIBLE);
            }
            else if ((i) == 122){
                opt122.setVisibility(View.VISIBLE);
            }
            else if ((i) == 123){
                opt123.setVisibility(View.VISIBLE);
            }
            else if ((i) == 124){
                opt124.setVisibility(View.VISIBLE);
            }
            else if ((i) == 125){
                opt125.setVisibility(View.VISIBLE);
            }
            else if ((i) == 126)
            {
                opt126.setVisibility(View.VISIBLE);
            }
            else if ((i) == 127){
                opt127.setVisibility(View.VISIBLE);
            }
            else if ((i) == 128){
                opt128.setVisibility(View.VISIBLE);
            }
            else if ((i) == 129){
                opt129.setVisibility(View.VISIBLE);
            }
            else if ((i) == 130){
                opt130.setVisibility(View.VISIBLE);
            }
            else if ((i) == 131){
                opt131.setVisibility(View.VISIBLE);
            }
            else if ((i) == 132){
                opt132.setVisibility(View.VISIBLE);
            }
            else if ((i) == 133){
                opt133.setVisibility(View.VISIBLE);
            }
            else if ((i) == 134){
                opt134.setVisibility(View.VISIBLE);
            }
            else if ((i) == 135){
                opt135.setVisibility(View.VISIBLE);
            }
            else if ((i) == 136){
                opt136.setVisibility(View.VISIBLE);
            }
            else if ((i) == 137){
                opt137.setVisibility(View.VISIBLE);
            }
            else if ((i) == 138){
                opt138.setVisibility(View.VISIBLE);
            }
            else if ((i) == 139){
                opt139.setVisibility(View.VISIBLE);
            }
            else if ((i) == 140){
                opt140.setVisibility(View.VISIBLE);
            }
            else if ((i) == 141){
                opt141.setVisibility(View.VISIBLE);
            }
            else if ((i) == 142){
                opt142.setVisibility(View.VISIBLE);
            }
            else if ((i) == 143){
                opt143.setVisibility(View.VISIBLE);
            }
            else if ((i) == 144){
                opt144.setVisibility(View.VISIBLE);
            }
            else if ((i) == 145){
                opt145.setVisibility(View.VISIBLE);
            }
            else if ((i) == 146){
                opt146.setVisibility(View.VISIBLE);
            }
            else if ((i) == 147){
                opt147.setVisibility(View.VISIBLE);
            }
            else if ((i) == 148){
                opt148.setVisibility(View.VISIBLE);
            }
            else if ((i) == 149){
                opt149.setVisibility(View.VISIBLE);
            }
            else if ((i) == 150){
                opt150.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    private void initViews() {
        opt1 = view.findViewById(R.id.opt1);
        opt2 = view.findViewById(R.id.opt2);
        opt3 = view.findViewById(R.id.opt3);
        opt4 = view.findViewById(R.id.opt4);
        opt5 = view.findViewById(R.id.opt5);
        opt6 = view.findViewById(R.id.opt6);
        opt7 = view.findViewById(R.id.opt7);
        opt8 = view.findViewById(R.id.opt8);
        opt9 = view.findViewById(R.id.opt9);
        opt10 = view.findViewById(R.id.opt10);

        opt11 = view.findViewById(R.id.opt11);
        opt12 = view.findViewById(R.id.opt12);
        opt13 = view.findViewById(R.id.opt13);
        opt14 = view.findViewById(R.id.opt14);
        opt15 = view.findViewById(R.id.opt15);
        opt16 = view.findViewById(R.id.opt16);
        opt17 = view.findViewById(R.id.opt17);
        opt18 = view.findViewById(R.id.opt18);
        opt19 = view.findViewById(R.id.opt19);
        opt20 = view.findViewById(R.id.opt20);

        opt21 = view.findViewById(R.id.opt21);
        opt22 = view.findViewById(R.id.opt22);
        opt23 = view.findViewById(R.id.opt23);
        opt24 = view.findViewById(R.id.opt24);
        opt25 = view.findViewById(R.id.opt25);
        opt26 = view.findViewById(R.id.opt26);
        opt27 = view.findViewById(R.id.opt27);
        opt28 = view.findViewById(R.id.opt28);
        opt29 = view.findViewById(R.id.opt29);
        opt30 = view.findViewById(R.id.opt30);

        opt31 = view.findViewById(R.id.opt31);
        opt32 = view.findViewById(R.id.opt32);
        opt33 = view.findViewById(R.id.opt33);
        opt34 = view.findViewById(R.id.opt34);
        opt35 = view.findViewById(R.id.opt35);
        opt36 = view.findViewById(R.id.opt36);
        opt37 = view.findViewById(R.id.opt37);
        opt38 = view.findViewById(R.id.opt38);
        opt39 = view.findViewById(R.id.opt39);
        opt40 = view.findViewById(R.id.opt40);

        opt41 = view.findViewById(R.id.opt41);
        opt42 = view.findViewById(R.id.opt42);
        opt43 = view.findViewById(R.id.opt43);
        opt44 = view.findViewById(R.id.opt44);
        opt45 = view.findViewById(R.id.opt45);
        opt46 = view.findViewById(R.id.opt46);
        opt47 = view.findViewById(R.id.opt47);
        opt48 = view.findViewById(R.id.opt48);
        opt49 = view.findViewById(R.id.opt49);
        opt50 = view.findViewById(R.id.opt50);

        opt51 = view.findViewById(R.id.opt51);
        opt52 = view.findViewById(R.id.opt52);
        opt53 = view.findViewById(R.id.opt53);
        opt54 = view.findViewById(R.id.opt54);
        opt55 = view.findViewById(R.id.opt55);
        opt56 = view.findViewById(R.id.opt56);
        opt57 = view.findViewById(R.id.opt57);
        opt58 = view.findViewById(R.id.opt58);
        opt59 = view.findViewById(R.id.opt59);
        opt60 = view.findViewById(R.id.opt60);

        opt61 = view.findViewById(R.id.opt61);
        opt62 = view.findViewById(R.id.opt62);
        opt63 = view.findViewById(R.id.opt63);
        opt64 = view.findViewById(R.id.opt64);
        opt65 = view.findViewById(R.id.opt65);
        opt66 = view.findViewById(R.id.opt66);
        opt67 = view.findViewById(R.id.opt67);
        opt68 = view.findViewById(R.id.opt68);
        opt69 = view.findViewById(R.id.opt69);
        opt70 = view.findViewById(R.id.opt70);

        opt71 = view.findViewById(R.id.opt71);
        opt72 = view.findViewById(R.id.opt72);
        opt73 = view.findViewById(R.id.opt73);
        opt74 = view.findViewById(R.id.opt74);
        opt75 = view.findViewById(R.id.opt75);
        opt76 = view.findViewById(R.id.opt76);
        opt77 = view.findViewById(R.id.opt77);
        opt78 = view.findViewById(R.id.opt78);
        opt79 = view.findViewById(R.id.opt79);
        opt80 = view.findViewById(R.id.opt80);

        opt81 = view.findViewById(R.id.opt81);
        opt82 = view.findViewById(R.id.opt82);
        opt83 = view.findViewById(R.id.opt83);
        opt84 = view.findViewById(R.id.opt84);
        opt85 = view.findViewById(R.id.opt85);
        opt86 = view.findViewById(R.id.opt86);
        opt87 = view.findViewById(R.id.opt87);
        opt88 = view.findViewById(R.id.opt88);
        opt89 = view.findViewById(R.id.opt89);
        opt90 = view.findViewById(R.id.opt90);

        opt91 = view.findViewById(R.id.opt91);
        opt92 = view.findViewById(R.id.opt92);
        opt93 = view.findViewById(R.id.opt93);
        opt94 = view.findViewById(R.id.opt94);
        opt95 = view.findViewById(R.id.opt95);
        opt96 = view.findViewById(R.id.opt96);
        opt97 = view.findViewById(R.id.opt97);
        opt98 = view.findViewById(R.id.opt98);
        opt99 = view.findViewById(R.id.opt99);
        opt100 = view.findViewById(R.id.opt100);

        opt101 = view.findViewById(R.id.opt101);
        opt102 = view.findViewById(R.id.opt102);
        opt103 = view.findViewById(R.id.opt103);
        opt104 = view.findViewById(R.id.opt104);
        opt105 = view.findViewById(R.id.opt105);
        opt106 = view.findViewById(R.id.opt106);
        opt107 = view.findViewById(R.id.opt107);
        opt108 = view.findViewById(R.id.opt108);
        opt109 = view.findViewById(R.id.opt109);
        opt110 = view.findViewById(R.id.opt110);

        opt111 = view.findViewById(R.id.opt111);
        opt112 = view.findViewById(R.id.opt112);
        opt113 = view.findViewById(R.id.opt113);
        opt114 = view.findViewById(R.id.opt114);
        opt115 = view.findViewById(R.id.opt115);
        opt116 = view.findViewById(R.id.opt116);
        opt117 = view.findViewById(R.id.opt117);
        opt118 = view.findViewById(R.id.opt118);
        opt119 = view.findViewById(R.id.opt119);
        opt120 = view.findViewById(R.id.opt120);

        opt121 = view.findViewById(R.id.opt121);
        opt122 = view.findViewById(R.id.opt122);
        opt123 = view.findViewById(R.id.opt123);
        opt124 = view.findViewById(R.id.opt124);
        opt125 = view.findViewById(R.id.opt125);
        opt126 = view.findViewById(R.id.opt126);
        opt127 = view.findViewById(R.id.opt127);
        opt128 = view.findViewById(R.id.opt128);
        opt129 = view.findViewById(R.id.opt129);
        opt130 = view.findViewById(R.id.opt130);

        opt131 = view.findViewById(R.id.opt131);
        opt132 = view.findViewById(R.id.opt132);
        opt133 = view.findViewById(R.id.opt133);
        opt134 = view.findViewById(R.id.opt134);
        opt135 = view.findViewById(R.id.opt135);
        opt136 = view.findViewById(R.id.opt136);
        opt137 = view.findViewById(R.id.opt137);
        opt138 = view.findViewById(R.id.opt138);
        opt139 = view.findViewById(R.id.opt139);
        opt140 = view.findViewById(R.id.opt140);

        opt141 = view.findViewById(R.id.opt141);
        opt142 = view.findViewById(R.id.opt142);
        opt143 = view.findViewById(R.id.opt143);
        opt144 = view.findViewById(R.id.opt144);
        opt145 = view.findViewById(R.id.opt145);
        opt146 = view.findViewById(R.id.opt146);
        opt147 = view.findViewById(R.id.opt147);
        opt148 = view.findViewById(R.id.opt148);
        opt149 = view.findViewById(R.id.opt149);
        opt150 = view.findViewById(R.id.opt150);
    }

}
