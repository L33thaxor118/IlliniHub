package com.example.alanrgan.illinihub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectTagsActivity extends AppCompatActivity implements FilterDrawerFragment.OnFragmentInteractionListener, View.OnClickListener {
  //Intent keys
  public static final String LOCATION_EXTRA = "select_tags_location";
  public static final String TITLE_EXTRA = "select_tags_title";
  public static final String DESC_EXTRA = "select_tags_description";
  public static final String START_TIME_EXTRA = "select_tags_start_time";
  public static final String END_TIME_EXTRA = "select_tags_end_time";
  public static final String TAGS_EXTRA = "select_tags_tags";

  //Intent data
  private long selectedStartDate;
  private long selectedEndDate;
  private double[] selectedLocation;
  private String selectedTitle;
  private String selectedDescription;

  private ArrayList<String> selectedTags;
  private Button nextButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTitle("Select Event Tags");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_tags);

    Intent intent = getIntent();
    selectedTitle = (intent.getStringExtra(SelectLocationActivity.TITLE_EXTRA));
    selectedDescription = (intent.getStringExtra(SelectLocationActivity.DESC_EXTRA));
    selectedLocation = intent.getDoubleArrayExtra(SelectLocationActivity.LOCATION_EXTRA);
    selectedStartDate = intent.getLongExtra(SelectLocationActivity.START_TIME_EXTRA, 0);
    selectedEndDate = intent.getLongExtra(SelectLocationActivity.END_TIME_EXTRA, 0);

    nextButton = (Button) findViewById(R.id.nextButton);
    nextButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view){
    if (view == nextButton) {
      Intent intent = new Intent(this, ReviewNewEventActivity.class);
      intent.putExtra(TITLE_EXTRA, selectedTitle);
      intent.putExtra(DESC_EXTRA, selectedDescription);
      intent.putExtra(START_TIME_EXTRA, selectedStartDate);
      intent.putExtra(END_TIME_EXTRA, selectedEndDate);
      intent.putExtra(LOCATION_EXTRA, selectedLocation);
      intent.putExtra(TAGS_EXTRA, selectedTags);
      startActivityForResult(intent,4);
    }
  }

  @Override
  public void updateFilter(List<String> selectedTags){
    if (selectedTags.size() == 0) this.selectedTags.clear();
    else this.selectedTags = new ArrayList<String>(selectedTags);
  }

  public void done(){
    Intent intent = new Intent();
    intent.putExtra(TITLE_EXTRA, selectedTitle);
    intent.putExtra(DESC_EXTRA, selectedDescription);
    intent.putExtra(START_TIME_EXTRA, selectedStartDate);
    intent.putExtra(END_TIME_EXTRA, selectedEndDate);
    intent.putExtra(LOCATION_EXTRA, selectedLocation);
    intent.putExtra(TAGS_EXTRA, selectedTags);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 4) {
      if (resultCode == RESULT_OK) {
        selectedLocation = data.getDoubleArrayExtra(ReviewNewEventActivity.LOCATION_EXTRA);
        selectedTitle = data.getStringExtra(ReviewNewEventActivity.TITLE_EXTRA);
        selectedDescription = data.getStringExtra(ReviewNewEventActivity.DESC_EXTRA);
        selectedStartDate = data.getLongExtra(ReviewNewEventActivity.START_TIME_EXTRA, 0);
        selectedEndDate = data.getLongExtra(ReviewNewEventActivity.END_TIME_EXTRA, 0);
        selectedTags = data.getStringArrayListExtra(ReviewNewEventActivity.TAGS_EXTRA);
        done();
      }
    }
  }
}
