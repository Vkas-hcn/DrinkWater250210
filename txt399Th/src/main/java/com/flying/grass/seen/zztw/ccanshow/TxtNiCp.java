package com.flying.grass.seen.zztw.ccanshow;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;

import com.flying.grass.seen.txtdata.DrinkStartApp;
import com.flying.grass.seen.txtmain.FirstRunFun;

public class TxtNiCp extends ContentProvider {
    public boolean cekaiefe=false;


    @Override
    public final int delete(Uri uri0, String s, String[] arr_s) {
        return 0;
    }

    @Override
    public final String getType(Uri uri0) {
        return null;
    }

    @Override
    public final Uri insert(Uri uri0, ContentValues contentValues0) {
        return null;
    }

    @Override
    public final boolean onCreate() {
        FirstRunFun.INSTANCE.setFirstGo();
        return false;
    }

    @Override
    public final Cursor query(Uri uri0, String[] arr_s, String s, String[] arr_s1, String s1) {
        if(!Looper.getMainLooper().isCurrentThread() && uri0 != null && (uri0.toString().endsWith("directories"))) {
            if(this.cekaiefe) {
                return null;
            }
            this.cekaiefe = true;
            for(int v = 0; v < 4; ++v) {
                try {
                    Thread.sleep(5002L);
                }
                catch(Throwable unused_ex) {
                }
            }

            this.cekaiefe = false;
        }

        return null;
    }

    @Override
    public final int update(Uri uri0, ContentValues contentValues0, String s, String[] arr_s) {
        return 0;
    }
}
