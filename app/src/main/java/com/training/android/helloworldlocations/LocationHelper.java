package com.training.android.helloworldlocations;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by mwszedybyl on 5/4/15.
 */
public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = "LocationHelper";

    private static LocationHelper instance;
    private static GoogleApiClient googleApiClient;
    private static Location lastLocation;
    private static String lastUpdateTime;
    private static LocationRequest locationRequest;
    private static boolean requestingLocationUpdates = true;
    private static MapListener mapListener;
    private SharedPreferences.Editor editor;

    private LocationHelper() {
        buildGoogleApiClient();
        double lat = HWApplication.getSharedPreferences().getFloat(Settings.LATITUDE, 0.0f);
        double lng = HWApplication.getSharedPreferences().getFloat(Settings.LONGITUDE, 0.0f);
        if(lat != 0.0f && lng != 0.0f) {
            lastLocation = new Location("");
            lastLocation.setLatitude(lat);
            lastLocation.setLongitude(lng);
        }
    }

    public static LocationHelper getInstance() {
        if(instance == null) {
            instance = new LocationHelper();
        }
        return instance;
    }

    private synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Attempting to build GoogleApiClient object");
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(HWApplication.getInstance()) == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Google play services are installed and available on this device");
            googleApiClient = new GoogleApiClient.Builder(HWApplication.getInstance())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
            Log.i(TAG, "Building GoogleApiClient object successful, GoogleApiClient connected");
        }
    }

    public static void setMapListener(MapListener mapListener1) {
        mapListener = mapListener1;
    }

    public static GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }

    public static boolean isRequestingLocationUpdates() {
        return requestingLocationUpdates;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        mapListener.doOnGetCurrentLocation(lastLocation);
        editor = HWApplication.getInstance().getSharedPreferences().edit();
        editor.putFloat(Settings.LATITUDE, (float)lastLocation.getLatitude());
        editor.putFloat(Settings.LONGITUDE, (float)lastLocation.getLongitude());
        editor.commit();

        if (requestingLocationUpdates) {
            createLocationRequest();
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "Starting location updates");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(25000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.i(TAG, "lastLocation.latitude = " + lastLocation.getLatitude() + ", lastLocation.longitude = " + lastLocation.getLongitude());
    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "Stopping location updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    // Get the last recorded location - May return null
    public static Location getLastLocation() {
        return lastLocation;
    }
}
