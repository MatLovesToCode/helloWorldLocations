package com.training.android.helloworldlocations;

/**
 * Created by mwszedybyl on 5/4/15.
 */
public class HWApplication extends android.app.Application
{
    private static HWApplication instance;

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
    }
}
