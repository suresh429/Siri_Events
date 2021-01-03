package com.androidtest.sirievents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidtest.sirievents.databinding.ActivityMainBinding;
import com.androidtest.sirievents.databinding.ActivitySplashBinding;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.GONE);
                binding.webView.setVisibility(View.VISIBLE);

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
            super.onBackPressed();
        }

    }
}