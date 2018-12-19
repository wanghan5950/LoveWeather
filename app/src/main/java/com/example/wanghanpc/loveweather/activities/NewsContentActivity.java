package com.example.wanghanpc.loveweather.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.wanghanpc.loveweather.R;

public class NewsContentActivity extends AppCompatActivity {

    private static final String URL_DATA = "URL_DATA";
    private FrameLayout webViewContainer;
    private WebView webView;
    private String url;
    private boolean webViewIsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        webViewContainer = (FrameLayout) findViewById(R.id.webView_container);
        webView = new WebView(getApplicationContext());
        webViewContainer.addView(webView);

        initToolbar();

        Intent intent = getIntent();
        url = intent.getStringExtra(URL_DATA);

    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.news_content_toolbar);
        toolbar.inflateMenu(R.menu.toolar_news_content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news_share:
                        String shareTitle = "分享";
                        String shareSubject = "";
                        String shareContent = url;
                        shareNewsUrl(shareTitle,shareSubject,shareContent);
                        break;
                    default:
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolar_news_content,menu);
        return true;
    }

    private void shareNewsUrl(String title,String subject,String content){
        if (content == null || "".equals(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, content);
        // 设置弹出框标题
        if (title != null && !"".equals(title)) { // 自定义标题
            startActivity(Intent.createChooser(intent, title));
        } else { // 系统默认标题
            startActivity(intent);
        }
    }

    private void destroyWebView(){
        webViewContainer.removeAllViews();
        if (webView != null){
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.pauseTimers();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
        if (!webViewIsReady){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
            webViewIsReady = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
