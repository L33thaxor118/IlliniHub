package com.example.alanrgan.illinihub;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class EventPreviewFragment {

  public static void show(Activity parent, Event event) {
    AlertDialog.Builder builder = new AlertDialog.Builder(parent);
    LayoutInflater inflater = parent.getLayoutInflater();
    View view = inflater.inflate(R.layout.event_preview_dialog, null);

    TextView titleView = view.findViewById(R.id.eventPreviewTitle);
    titleView.setText(event.title);

    TextView description = view.findViewById(R.id.eventPreviewDescription);
    description.setText(event.description);

    TextView date = view.findViewById(R.id.eventPreviewDate);
    date.setText(String.format("Until %s", new SimpleDateFormat("EEE MMM dd, hh:mm aaa").format(event.endTime)));

    Button button = view.findViewById(R.id.eventPreviewDetailsButton);
    button.setOnClickListener(evt -> {
      System.out.println("Details button clicked");
    });

    builder.setView(view);
    final AlertDialog alert = builder.create();

    Button closeButton = view.findViewById(R.id.eventPreviewCloseButton);
    closeButton.setOnClickListener(v -> alert.dismiss());

    alert.show();
  }
}
