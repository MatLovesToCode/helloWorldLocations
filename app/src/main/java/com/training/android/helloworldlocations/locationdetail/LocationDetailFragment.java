package com.training.android.helloworldlocations.locationdetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.helloworldlocations.LocationHelper;
import com.training.android.helloworldlocations.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mwszedybyl on 5/6/15.
 */
public class LocationDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LocationDetailFragment";

    public static final String HELLO_WORLD_URL = "http://www.helloworld.com/helloworld_locations.json";

    private View rootView;
    private ImageView staticMapIV;
    private ImageView officeIV;
    private TextView officeNameTV;
    private TextView officeAddressTV;
    private TextView distanceToOfficeTV;
    private Button callBtn;
    private Button directionsBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        rootView = inflater.inflate(R.layout.location_detail_fragment, parent, false);
        setupViews();

        return rootView;
    }

    private void setupViews(){
        staticMapIV = (ImageView) rootView.findViewById(R.id.static_map);
        officeIV = (ImageView) rootView.findViewById(R.id.office_image);

        final Intent intent = getActivity().getIntent();
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lng = intent.getDoubleExtra("long", 0.0);
        final Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lng);

        officeNameTV = (TextView) rootView.findViewById(R.id.office_name);
        officeAddressTV = (TextView) rootView.findViewById(R.id.office_address);
        distanceToOfficeTV = (TextView) rootView.findViewById(R.id.distance_to_office);

        officeNameTV.setText(intent.getStringExtra("name"));
        officeAddressTV.setText(intent.getStringExtra("address"));

        if(LocationHelper.getLastLocation()!=null)
        {
            double distanceFromUser = LocationHelper.getLastLocation().distanceTo(loc);
            distanceFromUser = distanceFromUser / 1609.34;
            distanceToOfficeTV.setText("distance: " + String.format("%.2f", distanceFromUser) + " miles away");
        }

        directionsBtn = (Button) rootView.findViewById(R.id.directions_btn);
        directionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LocationHelper.getLastLocation() != null) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr=" + LocationHelper.getLastLocation().getLatitude() + "," + LocationHelper.getLastLocation().getLongitude() + "&daddr=" + loc.getLatitude() + "," + loc.getLongitude()));
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Current location undefined", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        callBtn = (Button) rootView.findViewById(R.id.call_btn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + intent.getStringExtra("phone").trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        new GetStaticMapImageTask().execute();
        new GetOfficeImageTask().execute();
    }

    @Override
    public void onClick(View v) {
        if(v == v) {

        } else if (v == v) {

        }
    }

    private class GetStaticMapImageTask extends AsyncTask<Void, String, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bmp){
            staticMapIV.setImageBitmap(bmp);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            double lat = getActivity().getIntent().getDoubleExtra("lat", 0.00);
            double lng = getActivity().getIntent().getDoubleExtra("long", 0.00);
            Bitmap bm = getGoogleMapThumbnail(lat, lng);
            return bm;

        }
    }

    public static Bitmap getGoogleMapThumbnail(double lat, double lng){

        String URL = "http://maps.google.com/maps/api/staticmap?center=" +lat + "," + lng+ "&zoom=15&size=600x600&sensor=false?&markers=color:blue%7Clabel:H%7C" + lat + "," + lng;

        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in = null;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }

    private class GetOfficeImageTask extends AsyncTask<Void, String, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bmp) {
            officeIV.setImageBitmap(bmp);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String imageUrl = getActivity().getIntent().getStringExtra("photoUrl");
            Bitmap bm = getBitmapFromURL(imageUrl);
            return bm;
        }

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
