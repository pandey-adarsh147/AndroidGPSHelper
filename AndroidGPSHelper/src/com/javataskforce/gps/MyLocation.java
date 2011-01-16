package com.javataskforce.gps;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;

public class MyLocation implements LocationListener {
	private Context mContext;
	private GeoPoint mMyLocation;
	private volatile boolean mIsMyLocationEnabled;
	private Queue<Runnable> mRunnableFirstFixQueue = new LinkedList<Runnable>();
	private Queue<Runnable> mRunnable = new LinkedList<Runnable>();
	
	private String[] PROVIDERS_NAME = new String[] { LocationManager.GPS_PROVIDER,
			LocationManager.NETWORK_PROVIDER };

	public MyLocation(Context context) {
		mContext = context;
	}

	public void runOnFirstFix(Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException("MyLocation Exception: runnable must not be null.");
		}

		mRunnableFirstFixQueue.add(runnable);
	}

	public void runOnLocationUpdate(Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException("MyLocation Exception: runnable must not be null");
		}
		
		mRunnable.add(runnable);
	}
	
	public synchronized void enableMyLocation() {
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		Location[] candidates = new Location[PROVIDERS_NAME.length];
		int count = 0;
		for (String provider : PROVIDERS_NAME) {
			mIsMyLocationEnabled = true;
			locationManager.requestLocationUpdates(provider, 0, 0, this);
			Location location = locationManager.getLastKnownLocation(provider);
			if(location == null) {
				continue;
			}
			
			candidates[count++] = location;
		}
		
		Location firstFix = null;
		for (int i = 0; i < count; i++) {
			firstFix = candidates[i];
		}
		if(firstFix != null) {
			onLocationChanged(firstFix);
		}
	}
	
	public void disableMyLocation() {
		if(mIsMyLocationEnabled) {
			LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(this);
		}
	}
	
	public GeoPoint getMyLocation() {
		return mMyLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		mMyLocation = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
		
		//Run on first fix
		Runnable runnable;
		while ((runnable = mRunnableFirstFixQueue.poll()) != null) {
			new Thread(runnable).start();
		}
		
		//Run on any location updates.
		Iterator<Runnable> runnables = mRunnable.iterator();
		while (runnables.hasNext()) {
			Runnable updateRunnable = (Runnable) runnables.next();
			new Thread(updateRunnable).start();
		}
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
