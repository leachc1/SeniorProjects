package com.wit.edu.leachc1.discoop;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Discoop
 * Senior Project - Computer Science
 * Created by Crissy Leach and Sam Kanner
 * Wentworth Institute of Technology
 */

// About page
public class AboutDiscoop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_discoop);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // back button
    public void returnButton(View v) {
                finish();
                }
}
