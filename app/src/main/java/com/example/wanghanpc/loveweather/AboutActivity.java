package com.example.wanghanpc.loveweather;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        versionText = (TextView) findViewById(R.id.about_version_text);
        toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        versionText.setText(getVersionCode(this));
    }

    /**
     * 获取版本号
     */
    private String getVersionCode(Context context){
        PackageManager packageManager = context.getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(),0);
            versionName = info.versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return versionName;
    }
}
