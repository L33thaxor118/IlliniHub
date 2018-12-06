package com.example.alanrgan.illinihub;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alanrgan.illinihub.db.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.geojson.Point;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EventDetailsFragment extends Fragment {
  private Event event;
  private EventDetailsListener mListener;

  public static EventDetailsFragment newInstance(Event event) {
    EventDetailsFragment fragment = new EventDetailsFragment();
    Bundle args = new Bundle();
    args.putSerializable("event", event);
    fragment.setArguments(args);
    return fragment;
  }

  public static void show(AppCompatActivity parent, Event event) {
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
    closeButton.setOnClickListener(evt -> {
      getActivity().getSupportFragmentManager().popBackStack();

      // Show FABs again

      FloatingActionButton createEventButton = getActivity().findViewById(R.id.createEventButton);
      createEventButton.show();

      FloatingActionButton recenterButton = getActivity().findViewById(R.id.recenterButton);
      recenterButton.show();
    });

    TextView titleView = getView().findViewById(R.id.event_details_title);
    titleView.setText(event.title);

    TextView hostnameText = getView().findViewById(R.id.event_details_hostname);
    hostnameText.setText(String.format("Hosted by: %s", event.hostname));

    TextView descriptionText = getView().findViewById(R.id.event_details_description);
    descriptionText.setText(event.description);

    TextView dateLabel = getView().findViewById(R.id.event_date_label);
    String dateString = new SimpleDateFormat("EEEE, MMMM dd").format(event.startTime);
    dateLabel.setText(dateString);

    TextView thumbsUpCtLabel = getView().findViewById(R.id.event_thumbs_up_count);
    thumbsUpCtLabel.setText(Integer.toString(event.thumbsCt));

    ImageView thumbsImage = getView().findViewById(R.id.event_thumbs_up);
    thumbsImage.setOnClickListener(evt -> {
      event.thumbsCt += 1;
      mListener.setThumbCount(event.eventId, event.thumbsCt + 1);
      thumbsUpCtLabel.setText(Integer.toString(event.thumbsCt + 1));
    });

    TextView timeLabel = getView().findViewById(R.id.event_time_label);
    SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm aaa");
    timeLabel.setText(String.format("%s - %s",
        timeFormatter.format(event.startTime),
        timeFormatter.format(event.endTime))
    );

    // Create static map image
    Point eventPt = Point.fromLngLat(event.longitude, event.latitude);

    MapboxStaticMap staticImage = MapboxStaticMap.builder()
        .accessToken(getString(R.string.mapbox_access_token))
        .styleId(StaticMapCriteria.LIGHT_STYLE)
        .cameraPoint(Point.fromLngLat(event.longitude, event.latitude))
        .cameraZoom(18)
        .geoJson(eventPt)
        .width(620)
        .height(520)
        .build();

    ImageView staticMapView = getView().findViewById(R.id.event_location_image);

    String imageUrl = staticImage.url().toString();

    Picasso.Builder builder = new Picasso.Builder(getContext());
    builder.listener((picasso, uri, exception) -> {
      exception.printStackTrace();
    });

    builder.build()
        .load(imageUrl)
        .into(staticMapView);

    DatabaseHelper dbHelper = new DatabaseHelper(getContext());

    List<String> tags = dbHelper.getTagsForEvent(event.eventId);
    String tagString = String.join(", ", tags);

    TextView tagLabel = getView().findViewById(R.id.event_tag_label);
    tagLabel.setText(tagString);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.event_details, container, false);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof EventDetailsListener) {
      mListener = (EventDetailsListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement EventDetailsListener");
    }
  }

  public interface EventDetailsListener {
    void setThumbCount(int eventId, int thumbCount);
  }
}
