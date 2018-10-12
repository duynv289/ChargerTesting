package com.example.liz.chargertesting;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liz.chargertesting.screen.FragmentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgGit, imgMenu, imgHistory;
    private TextView txtFirstTip, txtSecondTip, txtButtonAction;
    private DrawerLayout mDrawerLayout;
    private Chronometer chronometer;
    private RelativeLayout relativeButtonAction;
    private ChargingReceiver receiver;
    private IntentFilter filter;
    private boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initNavigation();
        receiver = new ChargingReceiver();
        filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
        imgMenu.setOnClickListener(this);
        relativeButtonAction.setOnClickListener(this);
        imgHistory.setOnClickListener(this);
    }

    private void initView() {
        imgGit = findViewById(R.id.img_gif_tip);
        imgMenu = findViewById(R.id.imgMenu);
        imgHistory = findViewById(R.id.imgHistory);
        txtFirstTip = findViewById(R.id.txtFirstTip);
        txtSecondTip = findViewById(R.id.txtSecondTip);
        relativeButtonAction = findViewById(R.id.relative_button_action);
        chronometer = findViewById(R.id.chronometer);
        txtButtonAction = findViewById(R.id.txtButtonAction);
    }

    private void initNavigation() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgMenu:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.relative_button_action:
                boolean isCharging = receiver.isCharging();
                if (isCharging) {
                    if (!state) {
                        registerReceiver(receiver, filter);
                        long systemCurrTime = SystemClock.elapsedRealtime();
                        chronometer.setVisibility(View.VISIBLE);
                        txtFirstTip.setText(getResources().getString(R.string.first_tip_running));
                        txtSecondTip.setText(getResources().getString(R.string.second_tip_running));
                        imgGit.setImageResource(R.drawable.testing);
                        chronometer.setBase(systemCurrTime);
                        chronometer.start();
                        txtButtonAction.setText(getResources().getString(R.string.stop));
                        relativeButtonAction.setBackground(getResources().getDrawable(R.drawable.button_shape_running));
                        state = true;
                    } else {
                        chronometer.setVisibility(View.INVISIBLE);
                        txtFirstTip.setText(getResources().getString(R.string.first_tip_waiting));
                        txtSecondTip.setText(getResources().getString(R.string.second_tip_waiting));
                        imgGit.setImageResource(R.drawable.tip);
                        chronometer.start();
                        txtButtonAction.setText(getResources().getString(R.string.start));
                        relativeButtonAction.setBackground(getResources().getDrawable(R.drawable.button_shape_waiting));
                        state = false;
                    }
                } else {
                    Toast.makeText(this, "Please plugin", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgHistory:
                addFragment(FragmentResult.getInstance());
                break;
        }
    }

    private void addFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main,fragment,"1");
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        ft.commitAllowingStateLoss();
    }

    private void replaceFragment(Fragment fragment, int resId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(resId, fragment);
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
