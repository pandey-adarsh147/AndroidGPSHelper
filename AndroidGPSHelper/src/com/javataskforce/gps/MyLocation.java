package com.javataskforce.gps;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocation implements LocationListener {
	private Context mContext;
	private volatile boolean mIsMyLocationEnabled;
	private Queue<Runnable> runnableQueue = new LinkedList<Runnable>();

	private String[] providerStrings = new String[] { LocationManager.GPS_PROVIDER,
			LocationManager.NETWORK_PROVIDER };

	public MyLocation(Context context) {
		mContext = context;
	}

	public void runOnFirstFix(Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException("MyLocation Exception: runnable must not be null.");
		}

		runnableQueue.add(runnable);
	}

	public synchronized void enableMyLocation() {
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		 
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
