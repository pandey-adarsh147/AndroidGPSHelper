package com.javataskforce;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.javataskforce.gps.MyLocation;

public class MyLocationTestActivity extends Activity {
    /** Called when the activity is first created. */
	private Handler mHandler = new Handler();
	private TextView mLatitude;
	private TextView mLongitude;
	private MyLocation mMyLocation;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        
        mMyLocation = new MyLocation(this);
        Runnable runnable = new Runnable() {
        	@Override
        	public void run() {
        		mHandler.post(new Runnable() {
					@Override
					public void run() {
						GeoPoint geoPoint = mMyLocation.getMyLocation();
						mLatitude.setText(geoPoint.getLatitudeE6()+"");
						mLongitude.setText(geoPoint.getLongitudeE6()+"");
					}
				});
        	}
        };
        mMyLocation.runOnFirstFix(runnable);
        mMyLocation.runOnLocationUpdate(runnable);
        mMyLocation.enableMyLocation();
    }
	
	@Override
	protected void onResume() {
		mMyLocation.enableMyLocation();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mMyLocation.disableMyLocation();
		super.onPause();
	}
}