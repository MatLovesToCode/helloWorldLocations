package com.training.android.helloworldlocations;

import android.content.SharedPreferences;

import com.training.android.helloworldlocations.data.DBHelper;

/**
 * Created by mwszedybyl on 5/4/15.
 */
public class HWApplication extends android.app.Application
{
    private static HWApplication instance;
    private static SharedPreferences sharedPreferences;
    private static DBHelper dbHelper;

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
        sharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);
        LocationHelper.getInstance();

        dbHelper = DBHelper.getInstance(this);
        dbHelper.createOrOpenDatabase();
        dbHelper.close();

    }

    public static DBHelper getDBHelper()
    {
        if (dbHelper == null) {
            dbHelper = DBHelper.getInstance(instance);
        }
        return dbHelper;
    }

    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
