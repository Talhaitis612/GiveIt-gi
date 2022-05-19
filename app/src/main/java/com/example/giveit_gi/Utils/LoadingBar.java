package com.example.giveit_gi.Utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingBar {
    static ProgressDialog progressBar;
    public static void showLoadingBar(Context context, String title, String description){
        progressBar = new ProgressDialog(context);
        progressBar.setTitle(title);
        progressBar.setMessage(description);
        progressBar.show();

    }
    public static void hideLoadingBar(){
        progressBar.dismiss();
    }
}
