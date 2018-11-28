package com.example.alanrgan.illinihub;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class EventPreviewFragment {

  public static AlertDialog show(AppCompatActivity parent, Event event) {
    AlertDialog.Builder builder = new AlertDialog.Builder(parent);
    LayoutInflater inflater = parent.getLayoutInflater();
    View view = inflater.inflate(R.layout.event_preview_dialog, null);

    TextView titleView = view.findViewById(R.id.eventPreviewTitle);
    titleView.setText(event.title);

    TextView description = view.findViewById(R.id.eventPreviewDescription);
    description.setText(event.description);

    TextView date = view.findViewById(R.id.eventPreviewDate);
    date.setText(String.format("Until %s", new SimpleDateFormat("EEE MMM dd, hh:mm aaa").format(event.endTime)));

    builder.setView(view);
    final AlertDialog alert = builder.create();

    Button button = view.findViewById(R.id.eventPreviewDetailsButton);
    button.setOnClickListener(evt -> {
      alert.dismiss();

      EventDetailsFragment fragment = EventDetailsFragment.newInstance(event);

      FragmentTransaction transaction = parent.getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.main_view, fragment, "event_details@" + event.eventId);
      transaction.addToBackStack(null);
      transaction.commit();

      // Hide FABs

      FloatingActionButton createEventButton = parent.findViewById(R.id.createEventButton);
      createEventButton.hide();

      FloatingActionButton recenterButton = parent.findViewById(R.id.recenterButton);
      recenterButton.hide();
    });

    Button closeButton = view.findViewById(R.id.eventPreviewCloseButton);
    closeButton.setOnClickListener(v -> alert.dismiss());

    alert.show();
    return alert;
  }
}
