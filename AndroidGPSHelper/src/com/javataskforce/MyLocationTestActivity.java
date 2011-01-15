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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        mLatitude = (TextView) findViewById(R.id.latitude);
        mLongitude = (TextView) findViewById(R.id.longitude);
        
        final MyLocation myLocation = new MyLocation(this);
        Runnable runnable = new Runnable() {
        	@Override
        	public void run() {
        		mHandler.post(new Runnable() {
					@Override
					public void run() {
						GeoPoint geoPoint = myLocation.getMyLocation();
						mLatitude.setText(geoPoint.getLatitudeE6()+"");
						mLongitude.setText(geoPoint.getLongitudeE6()+"");
					}
				});
        	}
        };
        myLocation.runOnFirstFix(runnable);
        myLocation.runOnLocationUpdate(runnable);
        myLocation.enableMyLocation();
    }
	
	
}