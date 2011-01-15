package com.javataskforce.gps;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.maps.GeoPoint;

public class MyLocation implements LocationListener {
	private Context mContext;
	private GeoPoint mMyLocation;
	private volatile boolean mIsMyLocationEnabled;
	private Queue<Runnable> mRunnableFirstFixQueue = new LinkedList<Runnable>();
	private Queue<Runnable> mRunnable = new LinkedList<Runnable>();
	
	private String[] providerStrings = new String[] { LocationManager.GPS_PROVIDER,
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
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(location != null) {
			onLocationChanged(location);
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
