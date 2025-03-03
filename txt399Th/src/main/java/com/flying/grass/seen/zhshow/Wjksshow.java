package com.flying.grass.seen.zhshow;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Keep;

import com.flying.grass.seen.adtool.ShowDataTool;
import com.flying.grass.seen.zload.NtLoad;

@Keep
public class Wjksshow extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);

        if (i10 == 100) {
            ShowDataTool.INSTANCE.showLog(" onPageStarted=url=");
            NtLoad.UserCanTxt(i10);
        }
    }
}
