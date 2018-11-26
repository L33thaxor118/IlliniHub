package com.example.alanrgan.illinihub;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alanrgan.illinihub.util.Observable;
import com.example.alanrgan.illinihub.util.Observer;

public class RadiusActionBar extends RelativeLayout {
  public static final double MAXIMUM_ALLOWED_RADIUS = 3.0;

  private Button plusButton;
  private Button minusButton;
  private TextView radiusLabel;

  private Observable<Double> radiusChangeObservable = new Observable<>();

  // Discovery radius values in miles
  private final double[] radii = {0.05, 0.1, 0.15, 0.2, 0.3, 0.5, 1.0, 1.3, 2.0, 2.5, 3.0};

  // Set initial radius to be 0.15mi
  private double radius = radii[2];

  public RadiusActionBar(Context context) {
    super(context, null);
  }

  public RadiusActionBar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RadiusActionBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    //Inflate and attach your child XML
    LayoutInflater.from(context).inflate(R.layout.radius_action_bar, this);
    //Do any more custom init you would like to access children and do setup
    plusButton = (Button) findViewById(R.id.plusButton);
    plusButton.setOnClickListener(event -> snapToNearestRadius(true));

    minusButton = (Button) findViewById(R.id.minusButton);
    minusButton.setOnClickListener(event -> snapToNearestRadius(false));

    radiusLabel = (TextView) findViewById(R.id.radiusLabel);
    updateRadius();
  }

  public void onRadiusChange(Observer<Double> observer) {
    radiusChangeObservable.addObserver(observer);
  }

  public double getRadius() {
    return radius;
  }

  public void updateRadius() {
    updateRadius(radius);
  }

  public void updateRadius(double rad) {
    radiusLabel.setText(String.format("Radius:\n%1.2f mi", rad));
    radius = rad;
  }

  private int findNearestRadiusIndex(boolean roundUp) {
    int lo = 0;
    int hi = radii.length - 1;
    int mid;

    while (lo <= hi) {
      mid = (lo + hi) / 2;
      if (radii[mid] > radius) {
        hi = mid - 1;
      } else if (radii[mid] < radius) {
        lo = mid + 1;
      } else {
        return roundUp ? Math.min(mid + 1, radii.length - 1) : Math.max(0, mid - 1);
      }
    }

    return roundUp ? lo : Math.max(hi, 0);
  }

  private void snapToNearestRadius(boolean roundUp) {
    int radiusIdx = findNearestRadiusIndex(roundUp);
    updateRadius(radii[radiusIdx]);
    radiusChangeObservable.notifyObservers(radii[radiusIdx]);
  }
}
