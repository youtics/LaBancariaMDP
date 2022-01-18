package com.youtics.labancamdp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    String url="http://controlbeach.com.ar/bancaria/";
    SwipeRefreshLayout mySwipeRefreshLayout;
    WebView labancaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Oculto la barra superior
        getSupportActionBar().hide();
        labancaria = (WebView) findViewById(R.id.webview);
        assert labancaria != null;
        WebSettings websetting = labancaria.getSettings();
        websetting.setJavaScriptEnabled(true);
        websetting.setAllowFileAccess(true);
        if(Build.VERSION.SDK_INT>=21)
        {
            websetting.setMixedContentMode(0);
            labancaria.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if (Build.VERSION.SDK_INT>=19)
        {
            labancaria.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if (Build.VERSION.SDK_INT<19)
        {
            labancaria.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        labancaria.setWebViewClient(new WebViewClient());
        labancaria.setWebChromeClient(new WebChromeClient());
        labancaria.setWebViewClient(new MyWebViewClient());
        labancaria.setVerticalScrollBarEnabled(false);
        labancaria.loadUrl(url);
        mySwipeRefreshLayout = this.findViewById(R.id.swipeContainer);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        findViewById(R.id.loadeWebView).setVisibility(View.VISIBLE);
                        labancaria.reload();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted (WebView view, String url, Bitmap favicom)
        {
            super.onPageStarted(view,url,favicom);
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
            findViewById(R.id.loadeWebView).setVisibility(View.GONE);
            findViewById(R.id.webview).setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if(url.indexOf("tel:") > -1)
            {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                return true;
            }else if(url.indexOf("out:") > -1)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("out:","" ))));
                return true;
            }else if(url.indexOf("mailito:") > -1)
            {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                return true;
            }else if(url.startsWith("https://www.youtube.com"))
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }else
            {
                view.loadUrl(url);
                return true;
            }
        }
    }
}