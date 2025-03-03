package com.flying.grass.seen.zhshow;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.Keep;

import com.flying.grass.seen.zload.NtLoad;

@Keep
public class Fces7t extends Handler {
    public Fces7t() {

    }
    @Override
    public void handleMessage(Message message) {
        int r0 = message.what;
        NtLoad.UserCanTxt(r0);
    }
}

