package com.example.wanghanpc.loveweather.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.service.AutoUpdateService;
import com.example.wanghanpc.loveweather.service.NotificationService;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private Switch autoUpdateSwitch;
    private Switch allowNoticeSwitch;
    private boolean updateSwitchState = false;
    private boolean noticeSwitchState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        autoUpdateSwitch = (Switch) findViewById(R.id.setting_update_switch);
        allowNoticeSwitch = (Switch) findViewById(R.id.setting_notice_switch);
        LinearLayout autoUpdateButton = (LinearLayout) findViewById(R.id.setting_update_button);
        LinearLayout allowNoticeButton = (LinearLayout) findViewById(R.id.setting_notice_button);
        LinearLayout settingAboutButton = (LinearLayout) findViewById(R.id.setting_about_button);
        LinearLayout settingFeedbackButton = (LinearLayout) findViewById(R.id.setting_feedback_button);
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getAllSwitchState();
        setAllSwitchStateListener();
        autoUpdateButton.setOnClickListener(this);
        allowNoticeButton.setOnClickListener(this);
        settingAboutButton.setOnClickListener(this);
        settingFeedbackButton.setOnClickListener(this);
    }

    /**
     * 获取Switch开关状态
     */
    private void getAllSwitchState(){
        try {
            SharedPreferences preferences = getSharedPreferences("autoUpdateSwitchState",MODE_PRIVATE);
            boolean autoUpdate = preferences.getBoolean("autoUpdate",false);
            autoUpdateSwitch.setChecked(autoUpdate);
            updateSwitchState = autoUpdate;
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            SharedPreferences preferences = getSharedPreferences("allowNoticeSwitchState",MODE_PRIVATE);
            boolean allowNotice = preferences.getBoolean("allowNotice",false);
            allowNoticeSwitch.setChecked(allowNotice);
            noticeSwitchState = allowNotice;
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
                    Intent starIntent = new Intent(SettingActivity.this, AutoUpdateService.class);
                    startService(starIntent);
                }else {
                    Intent stopIntent = new Intent(SettingActivity.this,AutoUpdateService.class);
                    stopService(stopIntent);
                }
                SharedPreferences.Editor editor = getSharedPreferences("autoUpdateSwitchState",MODE_PRIVATE).edit();
                editor.putBoolean("autoUpdate",isChecked);
                editor.apply();
                updateSwitchState = isChecked;
            }
        });
        allowNoticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    Intent starIntent = new Intent(SettingActivity.this, NotificationService.class);
                    startService(starIntent);
                    judgeSystemNotificationSwitch();
                }else {
                    Intent stopIntent = new Intent(SettingActivity.this, NotificationService.class);
                    stopService(stopIntent);
                }
                SharedPreferences.Editor editor = getSharedPreferences("allowNoticeSwitchState",MODE_PRIVATE).edit();
                editor.putBoolean("allowNotice",isChecked);
                editor.apply();
                noticeSwitchState = isChecked;
            }
        });
    }

    /**
     * 判断系统设置中的通知开关是否打开
     */
    private void judgeSystemNotificationSwitch(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (manager != null){
                NotificationChannel channel = manager.getNotificationChannel("weatherNotification");
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE){
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID,channel.getId());
                    startActivity(intent);
                    Toast.makeText(this,"请手动将通知打开",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_update_button:
                if (updateSwitchState){
                    autoUpdateSwitch.setChecked(false);
                }else {
                    autoUpdateSwitch.setChecked(true);
                }
                break;
            case R.id.setting_notice_button:
                if (noticeSwitchState){
                    allowNoticeSwitch.setChecked(false);
                }else {
                    allowNoticeSwitch.setChecked(true);
                }
                break;
            case R.id.setting_about_button:
                Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_feedback_button:
                Uri uri = Uri.parse("mailto:wanghan5950@163.com");
                String[] email = {"wanghan5950@163.com"};
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri);
                intent1.putExtra(Intent.EXTRA_CC, email);// 抄送人
                intent1.putExtra(Intent.EXTRA_SUBJECT,"我的反馈");// 主题
                intent1.putExtra(Intent.EXTRA_TEXT,"我的建议如下：");// 正文
                startActivity(Intent.createChooser(intent1,"请选择邮件类应用"));
                break;
            default:
                break;
        }
    }
}
