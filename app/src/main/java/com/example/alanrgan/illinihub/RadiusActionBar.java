package com.example.alanrgan.illinihub;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alanrgan.illinihub.util.Observable;
import com.example.alanrgan.illinihub.util.Observer;

public class RadiusActionBar extends RelativeLayout {
  private Button plusButton;
  private Button minusButton;
  private TextView radiusLabel;

  private Observable<Double> radiusChangeObservable = new Observable<>();

  private final double[] radii = {0.3, 0.5, 1.0, 1.3, 2.0, 2.5, 3.0};

  // index of radius = 1.0
  private int radiusIdx = 2;

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
    plusButton.setOnClickListener(event -> incrementAndUpdateLabel());

    minusButton = (Button) findViewById(R.id.minusButton);
    minusButton.setOnClickListener(event -> decrementAndUpdateLabel());

    radiusLabel = (TextView) findViewById(R.id.radiusLabel);
    updateRadiusLabel();
  }

  public void onRadiusChange(Observer<Double> observer) {
    radiusChangeObservable.addObserver(observer);
  }

  private void updateRadiusLabel() {
    radiusLabel.setText(String.format("Radius:\n%1.1f", radii[radiusIdx]));
    radiusChangeObservable.notifyObservers(radii[radiusIdx]);
  }

  private void incrementAndUpdateLabel() {
    radiusIdx = Math.min(radiusIdx + 1, radii.length - 1);
    updateRadiusLabel();
  }

  private void decrementAndUpdateLabel() {
    radiusIdx = Math.max(radiusIdx - 1, 0);
    updateRadiusLabel();
  }

}
