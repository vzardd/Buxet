package com.vzard.buxet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout mytab;
    ViewPager2 viewpager;
    ConstraintLayout splash_screen;
    final static String NOTES_FILE="com.vzard.buxet.notes";
    public static String TO_DO_FILE="com.vzard.buxet.todo";
    public static String REMINDER_FILE="com.vzard.buxet.reminder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setElevation(0);
        setTitleBar();

       if(!ApplicationClass.splashShown)
        showSplash();

        mytab=findViewById(R.id.mytab);
        viewpager=findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyViewPagerAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mytab, viewpager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0: tab.setText("Notes");
                    break;
                    case 1: tab.setText("Bucket List");
                    break;
                    case 2: tab.setText("Reminder");
                    break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

    private void showSplash() {
        splash_screen=findViewById(R.id.splash_screen);
        splash_screen.setVisibility(View.VISIBLE);
        getSupportActionBar().hide();
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash_screen.setVisibility(View.GONE);
                getSupportActionBar().show();
            }
        },2000);
        ApplicationClass.splashShown=true;
    }

    private void setTitleBar() {
        ActionBar actionbar = getSupportActionBar();
        TextView title = new TextView(this);
        title.setText("Buxet");
        title.setGravity(1);
        title.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
        title.setTextSize(23);
        title.setTypeface(ResourcesCompat.getFont(this,R.font.audiowide));
        title.setTextColor(Color.parseColor("#FFFFFF"));
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(title);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences(NOTES_FILE,MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(ApplicationClass.applicationNotes);
        editor.putString("notes",json);
        //Toast.makeText(MainActivity.this,json,Toast.LENGTH_SHORT).show();
        editor.apply();
    }

}