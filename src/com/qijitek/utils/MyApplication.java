package com.qijitek.utils;

public final class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "handan.TTF");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "handan.TTF");
        FontsOverride.setDefaultFont(this, "SERIF", "handan.TTF");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "handan.TTF");
    }
}