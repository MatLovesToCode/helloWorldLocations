package com.training.android.helloworldlocations;

import android.content.SharedPreferences;

/**
 * Created by mwszedybyl on 5/4/15.
 */
public class HWApplication extends android.app.Application
{
    private static HWApplication instance;
    private static SharedPreferences sharedPreferences;

    public HWApplication() {
        super();

    }

    public static HWApplication getInstance() {
        if(instance == null) {
            instance = new HWApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LocationHelper.getInstance();
        sharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

    }

    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
