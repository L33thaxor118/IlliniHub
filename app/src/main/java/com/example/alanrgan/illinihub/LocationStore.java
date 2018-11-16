package com.example.alanrgan.illinihub;

import com.example.alanrgan.illinihub.util.Observable;
import com.example.alanrgan.illinihub.util.Observer;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class LocationStore {
  private Observable<LatLng> locationObservable = new Observable<>();

  public void onDataUpdated(Observer<LatLng> observable) {
    locationObservable.addObserver(observable);
  }

  public void run() {
    // Create a thread to simulate delayed asynchronous data loading
    Thread th =
        new Thread(
            () -> {
              try {
                Thread.sleep(3000);
                locationObservable.notifyObservers(new LatLng(40.107560, -88.228163));
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });

    th.start();
  }
}
