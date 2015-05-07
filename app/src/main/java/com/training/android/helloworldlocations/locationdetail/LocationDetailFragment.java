package com.training.android.helloworldlocations.locationdetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.training.android.helloworldlocations.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mwszedybyl on 5/6/15.
 */
public class LocationDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "JumpInFragment";

    public static final String HELLO_WORLD_URL = "http://www.helloworld.com/helloworld_locations.json";

    private View rootView;
    private ImageView staticMapIV;
    private ImageView officeIV;

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

        new GetStaticMapImageTask().execute();
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

        String URL = "http://maps.google.com/maps/api/staticmap?center=" +lat + "," + lng+ "&zoom=15&size=600x600&sensor=false";

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

    private class GetOfficeImageTask extends AsyncTask<Void, String, Bitmap>
    {

        @Override
        protected void onPostExecute(Bitmap bmp){
            staticMapIV.setImageBitmap(bmp);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String imageUrl = getActivity().getIntent().getStringExtra("photoUrl");
//            Bitmap bm = getGoogleMapThumbnail(lat, lng);
            return null;

        }

    }

}
