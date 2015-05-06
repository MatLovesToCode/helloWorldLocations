package com.training.android.helloworldlocations;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.training.android.helloworldlocations.models.HWLocation;

import java.util.List;

/**
 * Created by mwszedybyl on 5/4/15.
 */
public class LocationAdapter extends ArrayAdapter<HWLocation>
{

    private Context context;
    private List<HWLocation> locationList;

    public LocationAdapter(Context context, int resource, List<HWLocation> locationList) {
        super(context, resource);
        this.context = context;
        this.locationList = locationList;
    }

    @Override
    public HWLocation getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.location_list_item, null);
        }

        HWLocation location = getItem(position);
        populateView(convertView, location);

        return convertView;
    }

    private void populateView(View v, final HWLocation loc)
    {
        TextView officeName = (TextView) v.findViewById(R.id.office_name);
        TextView officeAddress = (TextView) v.findViewById(R.id.office_address);
        TextView distance = (TextView) v.findViewById(R.id.distance);

        officeName.setText(loc.getName());
        officeAddress.setText(loc.getAddress() + " " + loc.getAddress2() + ", " + loc.getCity() + " " + loc.getState() + " " + loc.getZip());
        Location officeLocation = new Location("");
        officeLocation.setLatitude(loc.getLatitude());
        officeLocation.setLongitude(loc.getLongitude());
        if(LocationHelper.getLastLocation()!=null)
        {
            double distanceFromUser = LocationHelper.getLastLocation().distanceTo(officeLocation);
            distanceFromUser = distanceFromUser / 1609.34;
            distance.setText("distance: " + String.format("%.2f", distanceFromUser) + " miles away");
        }
    }


}
