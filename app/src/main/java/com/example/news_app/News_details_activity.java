package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;



import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.net.URL;

public class News_details_activity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private ImageView imageView ;
    private TextView appbar_title , appbar_subtitle , date , time , title;
    private boolean isHideTollbar_view =false;
    FrameLayout data_behavior;
    LinearLayout title_app_bar;
    AppBarLayout appBarLayout;
    Toolbar toolbar ;
    String mUrl ,mImage ,mTitle ,mData , mSource , mAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details_activity);
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.backdrop);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        appbar_title = findViewById(R.id.title_on_appbar);
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        data_behavior = findViewById(R.id.date_behavior);
        title_app_bar = findViewById(R.id.title_appbar);
        appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);


        Intent intent = getIntent();
        mUrl= intent.getStringExtra("url");
        mTitle= intent.getStringExtra("title");
        mImage= intent.getStringExtra("img");
        mData= intent.getStringExtra("date");
        mSource= intent.getStringExtra("source");
        mAuthor= intent.getStringExtra("author");

        RequestOptions requestOptions =new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        Glide.with(this).load(mImage).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        appbar_title.setText(mSource);
        appbar_subtitle.setText(mUrl);
        date.setText(Utils.DateFormat(mData));
        title.setText(mTitle);
        String author = null ;
        if (mAuthor!=null||mAuthor!=""){
            mAuthor = " \u2020 " + mAuthor;
        }
        else {
            author ="";
        }
        time.setText(mSource + author + " \u2020 "+ Utils.DateToTimeFormat(mData) );
        intiate_web_view(mUrl);


    }

    void intiate_web_view(String url){
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int max_scroll = appBarLayout.getTotalScrollRange();
        float percentage =(float) Math.abs(i) / (float) max_scroll;
        if (percentage==1f&&isHideTollbar_view){
            data_behavior.setVisibility(View.GONE);
            title_app_bar.setVisibility(View.VISIBLE);
            isHideTollbar_view=!isHideTollbar_view;
        }
        else if (percentage<1f&&!isHideTollbar_view){
            data_behavior.setVisibility(View.VISIBLE);
            title_app_bar.setVisibility(View.GONE);
            isHideTollbar_view=!isHideTollbar_view;
        }
    }
}
