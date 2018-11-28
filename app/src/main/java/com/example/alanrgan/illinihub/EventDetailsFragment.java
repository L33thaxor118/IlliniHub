package com.example.alanrgan.illinihub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EventDetailsFragment extends Fragment {
  private Event event;

  public static EventDetailsFragment newInstance(Event event) {
    EventDetailsFragment fragment = new EventDetailsFragment();
    Bundle args = new Bundle();
    args.putSerializable("event", event);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      event = (Event) getArguments().getSerializable("event");
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    ImageView closeButton = getView().findViewById(R.id.close_btn);
    closeButton.setOnClickListener(evt -> getActivity().getSupportFragmentManager().popBackStack());

    TextView titleView = getView().findViewById(R.id.event_details_title);
    titleView.setText(event.title);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.event_details, container, false);
  }
}
