package com.training.android.helloworldlocations;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.android.helloworldlocations.models.HWLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LocationsActivity extends FragmentActivity implements OnMapReadyCallback, GetLocationListener
{

    public static final String HELLO_WORLD_URL = "http://www.helloworld.com/helloworld_locations.json";
    private ArrayList<HWLocation> helloWorldLocations;
    private ListView locationListView;
    private LocationAdapter adapter;
    private GoogleMap map;
    private boolean mapIsReady;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        helloWorldLocations = new ArrayList<>();
        locationListView = (ListView) findViewById(R.id.locations_listview);
        mapIsReady = false;

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);
        new NearbyLocationLoader().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locations, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        mapIsReady = true;
        this.map = map;
        map.setMyLocationEnabled(true);


//        for (HWLocation l : helloWorldLocations)
//        {
//            LatLng location = new LatLng(l.getLatitude(), l.getLongitude());
//            map.addMarker(new MarkerOptions().position(location).snippet("hi"));
//        }

    }

    private class NearbyLocationLoader extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
                ConnectivityManager connMgr = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected())
                {
                    try
                    {
                        URL url = new URL(HELLO_WORLD_URL);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type", "application/json");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestMethod("GET");
                        // Starts the query
                        conn.connect();

                        // Get response
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = br.readLine()) != null)
                        {
                            sb.append(line + "\n");
                        }
                        br.close();

                        JSONObject jsonObject = new JSONObject(sb.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("locations");

                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++)
                        {
                            JSONObject jsonLocation = (JSONObject) jsonArray.get(i);
                            HWLocation hwLocation = new HWLocation();
                            hwLocation.setName(jsonLocation.getString("name"));
                            hwLocation.setAddress(jsonLocation.getString("address"));
                            hwLocation.setAddress2(jsonLocation.getString("address2"));
                            hwLocation.setCity(jsonLocation.getString("city"));
                            hwLocation.setState(jsonLocation.getString("state"));
                            hwLocation.setZip(jsonLocation.getString("zip_postal_code"));
                            hwLocation.setPhoneNumber(jsonLocation.getString("phone"));
                            hwLocation.setFax(jsonLocation.getString("fax"));
                            hwLocation.setLatitude(jsonLocation.getDouble("latitude"));
                            hwLocation.setLongitude(jsonLocation.getDouble("longitude"));
                            hwLocation.setPictureLink(jsonLocation.getString("office_image"));
                            helloWorldLocations.add(hwLocation);
                        }
                    } catch (Exception e)
                    {
                        Log.d("exception", "the error = " + e.getMessage());
                    }
                }
            return null;
        }

        protected void onPostExecute (Void result)
        {
            if(mapIsReady)
            {
                for (HWLocation l : helloWorldLocations)
                {
                    LatLng location = new LatLng(l.getLatitude(), l.getLongitude());
                    map.addMarker(new MarkerOptions().position(location).snippet(""+l.getName()));
                }
            }

            while(LocationHelper.getLastLocation()==null){

            }

            adapter = new LocationAdapter(getBaseContext(), R.layout.location_list_item, helloWorldLocations);
            locationListView.setAdapter(adapter);
            LatLng currentLatLng = new LatLng(LocationHelper.getLastLocation().getLatitude(), LocationHelper.getLastLocation().getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));


        }
    }

    public void doOnComplete()
    {

    }

}
