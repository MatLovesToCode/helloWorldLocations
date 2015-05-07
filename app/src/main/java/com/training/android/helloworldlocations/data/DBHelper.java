package com.training.android.helloworldlocations.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.training.android.helloworldlocations.models.HWLocation;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mwszedybyl on 5/6/15.
 */
public class DBHelper extends SQLiteOpenHelper
{


    private static final String DATABASE_NAME = "appdata";
    private SQLiteDatabase mDatabase;
    private final Context context;
    private String databasePath;
    private static final int DATABASE_VERSION = 1;
    private static final String LOCATION_TABLE_NAME = "locations";
    private static DBHelper instance;


    private static final String LOCATION_TABLE_CREATE =
            "CREATE TABLE " + LOCATION_TABLE_NAME + " ( ID INTEGER PRIMARY KEY , NAME TEXT UNIQUE, ADDRESS TEXT UNIQUE, " +
                    "ADDRESS2 TEXT, CITY TEXT, ZIP TEXT, STATE TEXT, PHONENUMBER TEXT, FAX TEXT, " +
                    "LATITUDE INTEGER, LONGITUDE INTEGER, PICTURELINK TEXT );";

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getApplicationInfo().dataDir + "/databases/" + DATABASE_NAME;
    }


    interface Table
    {
        // Tables
        public static final String LOCATIONS = "locations";
    }

    interface LocationTable
    {
        public static final String ID = "id"; // INTEGER
        public static final String NAME = "name"; // TEXT
        public static final String ADDRESS = "address"; // INTEGER
        public static final String ADDRESS2 = "address2"; // INTEGER
        public static final String CITY = "city"; // TEXT
        public static final String ZIP = "zip"; // TEXT
        public static final String STATE = "state"; // TEXT
        public static final String PHONENUMBER = "phonenumber"; // TEXT
        public static final String FAX = "fax"; // TEXT
        public static final String LATITUDE = "latitude"; // TEXT
        public static final String LONGITUDE = "longitude"; // TEXT
        public static final String PICTURELINK = "picturelink"; // TEXT
    }

    public static final String[] Location_TABLE_COLUMNS =
            {
                    LocationTable.ID,
                    LocationTable.NAME,
                    LocationTable.ADDRESS,
                    LocationTable.ADDRESS2,
                    LocationTable.CITY,
                    LocationTable.ZIP,
                    LocationTable.STATE,
                    LocationTable.PHONENUMBER,
                    LocationTable.FAX,
                    LocationTable.LATITUDE,
                    LocationTable.LONGITUDE,
                    LocationTable.PICTURELINK
            };


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(LOCATION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public void createOrOpenDatabase()
    {
        // if we already have a db, then we're good
        if (mDatabase != null && mDatabase.isOpen())
            return;

        if (mDatabase == null)
        {
            // call getWritableDatabase first to create all the folders to the
            // final path for our database
            mDatabase = getReadableDatabase();
            mDatabase.setVersion(DATABASE_VERSION);
        }

        try
        {
            mDatabase = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);

            if (mDatabase.getVersion() < DATABASE_VERSION)
            {
                mDatabase = null;
            }
        } catch (SQLException e)
        {
            // it's okay, just means db doesn't exist yet
            mDatabase = null;
        }

    }

    @Override
    public synchronized void close()
    {
        super.close();
        if (mDatabase != null && mDatabase.isOpen())
            mDatabase.close();
    }


    public void commitLocations(List<HWLocation> locationList)
    {
        mDatabase.beginTransaction();
        try
        {
            for (HWLocation l : locationList)
            {
                commitLocation(l);
            }
            mDatabase.setTransactionSuccessful();
        } finally
        {
            mDatabase.endTransaction();
        }
    }

    private void commitLocation(HWLocation location)
    {
        ContentValues cv = new ContentValues();
        cv.put(LocationTable.NAME, location.getName());
        cv.put(LocationTable.ADDRESS, location.getAddress());
        cv.put(LocationTable.ADDRESS2, location.getAddress2());
        cv.put(LocationTable.CITY, location.getCity());
        cv.put(LocationTable.ZIP, location.getZip());
        cv.put(LocationTable.STATE, location.getState());
        cv.put(LocationTable.PHONENUMBER, location.getPhoneNumber());
        cv.put(LocationTable.FAX, location.getFax());
        cv.put(LocationTable.LATITUDE, location.getLatitude());
        cv.put(LocationTable.LONGITUDE, location.getLongitude());
        cv.put(LocationTable.PICTURELINK, location.getPictureLink());
        mDatabase.insert(Table.LOCATIONS, null, cv);
    }

    public HashMap<Integer, HWLocation> getAllLocations()
    {
        final Cursor cursor = mDatabase.query(Table.LOCATIONS,
                Location_TABLE_COLUMNS, null, null, null, null, null);
        final HashMap<Integer, HWLocation> locations = locationFromCursor(cursor);
        cursor.close();

        return locations;

    }

    public static HashMap<Integer, HWLocation> locationFromCursor(Cursor cursor)
    {
        final HashMap<Integer, HWLocation> locs = new HashMap<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HWLocation l = new HWLocation();
            l.setName(cursor.getString(1));
            l.setAddress(cursor.getString(2));
            l.setAddress2(cursor.getString(3));
            l.setCity(cursor.getString(4));
            l.setZip(cursor.getString(5));
            l.setState(cursor.getString(6));
            l.setPhoneNumber(cursor.getString(7));
            l.setFax(cursor.getString(8));
            l.setLatitude(cursor.getDouble(9));
            l.setLongitude(cursor.getDouble(10));
            l.setPictureLink(cursor.getString(11));

            locs.put(cursor.getPosition(), l);
            cursor.moveToNext();
        }

        return locs;
    }

}

