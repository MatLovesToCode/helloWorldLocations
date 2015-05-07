package com.training.android.helloworldlocations;

import android.location.Location;

/**
 * Created by mwszedybyl on 5/6/15.
 */
public interface MapListener
{
    void doOnGetCurrentLocation(Location location);

}
