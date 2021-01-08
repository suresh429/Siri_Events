package com.androidtest.sirievents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.androidtest.sirievents.databinding.ActivityMainBinding;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    boolean doubleBackToExitPressedOnce = false;
    ActivityMainBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, false);
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.GONE);
                binding.webView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadResource(WebView view, String url) {

                try {
                    binding.webView.loadUrl("javascript:(function() { " +
                            "var head = document.getElementsByClassName('')[0].style.display='none'; " +
                            "})()");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: "+url);
                if(url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                if(url.startsWith("https://www.google.com/maps/dir/")){
                    Log.d(TAG, "shouldOverrideUrlLoading: "+url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                    return true;
                }

                return false;
            }
        });
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("https://siri-management.business.site/");

    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
           // super.onBackPressed();
            doubleTap();
        }

    }

    private void doubleTap(){
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}