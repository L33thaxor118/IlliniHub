package com.example.alanrgan.illinihub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class EventDetailsFragment extends Fragment {
  private Event event;

  public static EventDetailsFragment newInstance(Event event) {
    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
    Bundle args = new Bundle();
    args.putSerializable("event", event);

    eventDetailsFragment.setArguments(args);
    return eventDetailsFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      event = (Event) getArguments().getSerializable("event");
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.event_details, container, false);
  }
}
