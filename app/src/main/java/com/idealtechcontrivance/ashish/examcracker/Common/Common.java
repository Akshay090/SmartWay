package com.idealtechcontrivance.ashish.examcracker.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null) {

                for (int i=0; i<info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static final String slider="http://www.examcrackerst.in/android/slider.php";

    public static final String package_category="http://www.examcrackerst.in/android/package_category.php";

    public static final String all_packages="http://www.examcrackerst.in/android/all_packages.php";
    public static final String selected_packages="http://www.examcrackerst.in/android/selected_package.php";
    public static final String free_packages="http://www.examcrackerst.in/android/free_packages.php";
    public static final String paid_packages="http://www.examcrackerst.in/android/paid_packages.php";
    public static final String latest_packages="http://www.examcrackerst.in/android/latest_packages.php";
    public static final String similar_packages="http://www.examcrackerst.in/android/similar_packages.php";

    public static final String free_full_length_test="http://www.examcrackerst.in/android/free_full_length_test.php";

    public static final String features="http://www.examcrackerst.in/android/features.php";
    public static final String testimonials="http://www.examcrackerst.in/android/testimonials.php";

    public static final String sign_up_tbl="http://www.examcrackerst.in/android/signup.php";
    public static final String sign_in_tbl="http://www.examcrackerst.in/android/signin.php";
    public static final String forgot_password="http://www.examcrackerst.in/android/forgot_password.php";
    public static final String terms_condition = "http://examcrackerst.in/terms-conditions.html";

    public static final String load_profile="http://www.examcrackerst.in/android/load_profile.php";
    public static final String change_profile="http://www.examcrackerst.in/android/change_profile.php";

    public static final String faq_question_tbl="http://www.examcrackerst.in/android/faq_question.php";
    public static final String tbl_our_office = "http://www.examcrackerst.in/android/our_office.php";
    public static final String about_us = "http://examcrackerst.in/about.html";
    public static final String privacy_policy = "http://examcrackerst.in/privacy-policy.html";

    public static final String testimonial_tbl="http://www.examcrackerst.in/android/testimonial.php";
    public static final String state_tbl="http://www.examcrackerst.in/android/state.php";

    public static final String change_password_tbl="http://www.examcrackerst.in/android/change_password.php";
    public static final String tbl_composemail = "http://www.examcrackerst.in/android/list_inbox.php";
    public static final String news_updates = "http://www.examcrackerst.in/android/news_updates.php";

    public static final String apply_coupon = "http://www.examcrackerst.in/android/apply_coupon.php";

    public static final String start_test = "http://www.examcrackerst.in/android/start_test.php";
    public static final String question_paper = "http://www.examcrackerst.in/android/question_paper.php";

    public static final String EMAIL ="wesoft1001@gmail.com";
    public static final String PASSWORD ="Isoft1001";

}