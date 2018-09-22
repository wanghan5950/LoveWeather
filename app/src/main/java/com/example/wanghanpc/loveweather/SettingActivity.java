package com.example.wanghanpc.loveweather;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.wanghanpc.loveweather.service.AutoUpdateInformationService;
import com.example.wanghanpc.loveweather.service.NotificationService;

public class SettingActivity extends AppCompatActivity {

    private Switch autoUpdateSwitch;
    private Switch allowNoticeSwitch;
    private LinearLayout settingAboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        autoUpdateSwitch = (Switch) findViewById(R.id.setting_update_switch);
        allowNoticeSwitch = (Switch) findViewById(R.id.setting_notice_switch);
        settingAboutButton = (LinearLayout) findViewById(R.id.setting_about_button);

        getAllSwitchState();
        setAllSwitchStateListener();

        settingAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取Switch开关状态
     */
    private void getAllSwitchState(){
        try {
            SharedPreferences preferences = getSharedPreferences("autoUpdateSwitchState",MODE_PRIVATE);
            boolean autoUpdate = preferences.getBoolean("autoUpdate",false);
            autoUpdateSwitch.setChecked(autoUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            SharedPreferences preferences = getSharedPreferences("allowNoticeSwitchState",MODE_PRIVATE);
            boolean allowNotice = preferences.getBoolean("allowNotice",false);
            allowNoticeSwitch.setChecked(allowNotice);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Switch状态监听
     */
    private void setAllSwitchStateListener(){
        autoUpdateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    Intent starIntent = new Intent(SettingActivity.this, AutoUpdateInformationService.class);
                    startService(starIntent);
                }else {
                    Intent stopIntent = new Intent(SettingActivity.this,AutoUpdateInformationService.class);
                    stopService(stopIntent);
                }
                SharedPreferences.Editor editor = getSharedPreferences("autoUpdateSwitchState",MODE_PRIVATE).edit();
                editor.putBoolean("autoUpdate",isChecked);
                editor.apply();
            }
        });
        allowNoticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    Intent starIntent = new Intent(SettingActivity.this, NotificationService.class);
                    startService(starIntent);
                }else {
                    Intent stopIntent = new Intent(SettingActivity.this, NotificationService.class);
                    stopService(stopIntent);
                }
                SharedPreferences.Editor editor = getSharedPreferences("allowNoticeSwitchState",MODE_PRIVATE).edit();
                editor.putBoolean("allowNotice",isChecked);
                editor.apply();
            }
        });
    }
}
