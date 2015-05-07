package com.training.android.helloworldlocations.locationdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.training.android.helloworldlocations.R;


/**
 * Created by mwszedybyl on 5/6/15.
 */
public class LocationDetailActivity extends FragmentActivity {

    private FragmentManager fm;

    private Fragment createDetailsFragment() {
        return new LocationDetailFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        fm = getSupportFragmentManager();

        Fragment detailsFragment = fm.findFragmentById(R.id.fragment_container);
        if (detailsFragment == null) {
            detailsFragment = createDetailsFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, detailsFragment)
                    .commit();
        }
    }

}
