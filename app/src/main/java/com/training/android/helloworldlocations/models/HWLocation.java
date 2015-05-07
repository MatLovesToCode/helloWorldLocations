package com.training.android.helloworldlocations.models;

import android.location.Location;

import com.training.android.helloworldlocations.LocationHelper;

import java.util.Comparator;

/**
 * Created by mwszedybyl on 5/4/15.
 */
public class HWLocation
{
    private String name;
    private String address;
    private String address2;
    private String city;
    private String zip;
    private String state;
    private String phoneNumber;
    private String fax;
    private double latitude;
    private double longitude;
    private String pictureLink;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPictureLink()
    {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink)
    {
        this.pictureLink = pictureLink;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public String fullAddress() {
        return address + " " + address2 + ", " + city + " " + state + " " + zip;
    }

    public static class DistanceAwayComparator implements Comparator<HWLocation>
    {
        @Override
        public int compare(HWLocation locA, HWLocation locB) {
            Location locationA = new Location("");
            locationA.setLatitude(locA.getLatitude());
            locationA.setLongitude(locA.getLongitude());
            Location locationB = new Location("");
            locationB.setLatitude(locB.getLatitude());
            locationB.setLongitude(locB.getLongitude());

            return (int) (LocationHelper.getLastLocation().distanceTo(locationA) - LocationHelper.getLastLocation().distanceTo(locationB));
        }
    }
}
