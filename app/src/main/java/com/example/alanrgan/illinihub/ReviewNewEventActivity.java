package com.example.alanrgan.illinihub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.leanback.widget.HorizontalGridView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alanrgan.illinihub.util.ButtonAdapter;
import com.example.alanrgan.illinihub.util.SortedArrayList;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReviewNewEventActivity extends AppCompatActivity implements View.OnClickListener, ButtonAdapter.ClickListener {
  //Intent keys
  public static final String LOCATION_EXTRA = "final_location";
  public static final String TITLE_EXTRA = "final_title";
  public static final String DESC_EXTRA = "final_description";
  public static final String START_TIME_EXTRA = "final_start_time";
  public static final String END_TIME_EXTRA = "final_end_time";
  public static final String TAGS_EXTRA = "final_tags";

  //Intent data
  private long selectedStartDate;
  private long selectedEndDate;
  private SortedArrayList<String> selectedTags = new SortedArrayList<>();
  private double[] selectedLocation;
  private String selectedTitle;
  private String selectedDescription;

  private ButtonAdapter selectedTagsAdapter;

  EditText startDateTextEdit;
  EditText endDateTextEdit;
  EditText startTimeTextEdit;
  EditText endTimeTextEdit;
  EditText location;
  Button nextButton;
  EditText titleTextEdit;
  EditText descriptionTextEdit;
  HorizontalGridView tagView;

  String beginStartDate;
  String beginStartTime;
  String beginEndDate;
  String beginEndTime;

  private int mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute;
  private int mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTitle("Review Information");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_review_new_event);
    Intent intent = getIntent();
    selectedTitle = intent.getStringExtra(SelectTagsActivity.TITLE_EXTRA);
    selectedDescription = intent.getStringExtra(SelectTagsActivity.DESC_EXTRA);
    selectedLocation = intent.getDoubleArrayExtra(SelectTagsActivity.LOCATION_EXTRA);

    if (intent.getStringArrayListExtra((SelectTagsActivity.TAGS_EXTRA)) != null)
    selectedTags.addAll(intent.getStringArrayListExtra((SelectTagsActivity.TAGS_EXTRA)));

    selectedStartDate = intent.getLongExtra(SelectTagsActivity.START_TIME_EXTRA, 0);
    selectedEndDate = intent.getLongExtra(SelectTagsActivity.END_TIME_EXTRA, 0);
    tagView = (HorizontalGridView) findViewById(R.id.tagView);
    selectedTagsAdapter = new ButtonAdapter(this, selectedTags, this);
    tagView.setAdapter(selectedTagsAdapter);

    nextButton = (Button)findViewById(R.id.nextButton);
    startDateTextEdit = (EditText) findViewById(R.id.startDate);
    endDateTextEdit = (EditText) findViewById(R.id.endDate);
    startTimeTextEdit = (EditText) findViewById(R.id.startTime);
    endTimeTextEdit = (EditText) findViewById(R.id.endTime);
    startDateTextEdit.setOnClickListener(this);
    endDateTextEdit.setOnClickListener(this);
    startTimeTextEdit.setOnClickListener(this);
    endTimeTextEdit.setOnClickListener(this);
    nextButton.setOnClickListener(this);
    titleTextEdit = (EditText) findViewById(R.id.title);
    descriptionTextEdit = (EditText) findViewById(R.id.description);
    location = (EditText) findViewById(R.id.location);
    makeGeocodeSearch(new LatLng(selectedLocation[0],selectedLocation[1]));

    titleTextEdit.setText(selectedTitle);
    descriptionTextEdit.setText(selectedDescription);
    Date startDate = new Date(selectedStartDate);
    Date endDate = new Date(selectedEndDate);

    beginStartDate = new String(startDate.getDay() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getYear());
    beginStartTime = new String(startDate.getHours() + ":" + startDate.getMinutes());
    beginEndDate = new String(endDate.getDay() + "-" + (endDate.getMonth() + 1) + "-" + endDate.getYear());
    beginEndTime = new String(endDate.getHours() + ":" + endDate.getMinutes());

    startDateTextEdit.setText(beginStartDate);
    endDateTextEdit.setText(beginEndDate);
    startTimeTextEdit.setText(beginStartTime);
    endTimeTextEdit.setText(beginEndTime);

  }

  @Override
  public void onClick(View v) {
    if (v == startDateTextEdit) {
      final Calendar c = Calendar.getInstance();
      mStartYear = c.get(Calendar.YEAR);
      mStartMonth = c.get(Calendar.MONTH);
      mStartDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
        startDateTextEdit.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        mStartDay = dayOfMonth;
        mStartMonth = monthOfYear;
        mStartYear = year;
      }, mStartYear, mStartMonth, mStartDay);
      datePickerDialog.show();
    }
    if (v == startTimeTextEdit) {
      final Calendar c = Calendar.getInstance();
      mStartHour = c.get(Calendar.HOUR_OF_DAY);
      mStartMinute = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
        startTimeTextEdit.setText(hourOfDay + ":" + minute);
        mStartHour = hourOfDay;
        mStartMinute = minute;
      }, mStartHour, mStartMinute, false);
      timePickerDialog.show();
    }
    if (v == endDateTextEdit) {
      final Calendar c = Calendar.getInstance();
      mEndYear = c.get(Calendar.YEAR);
      mEndMonth = c.get(Calendar.MONTH);
      mEndDay = c.get(Calendar.DAY_OF_MONTH);

      DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
        endDateTextEdit.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        mEndDay = dayOfMonth;
        mEndMonth = monthOfYear;
        mEndYear = year;
      }, mEndYear, mEndMonth, mEndDay);
      datePickerDialog.show();
    }
    if (v == endTimeTextEdit) {
      final Calendar c = Calendar.getInstance();
      mEndHour = c.get(Calendar.HOUR_OF_DAY);
      mEndMinute = c.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
        endTimeTextEdit.setText(hourOfDay + ":" + minute);
        mEndHour = hourOfDay;
        mEndMinute = minute;
      }, mEndHour, mEndMinute, false);
      timePickerDialog.show();
    }
    if (v == nextButton){
      done();
    }
  }

  public void done(){
    Intent intent = new Intent();
    if (!titleTextEdit.getText().toString().equals(selectedTitle)){
      intent.putExtra(TITLE_EXTRA, titleTextEdit.getText().toString());
    } else intent.putExtra(TITLE_EXTRA, selectedTitle);

    if (!descriptionTextEdit.getText().toString().equals(selectedDescription)){
      intent.putExtra(DESC_EXTRA, descriptionTextEdit.getText().toString());
    } else intent.putExtra(DESC_EXTRA, selectedDescription);

    if (beginStartDate.equals(startDateTextEdit.getText().toString()) & beginStartTime.equals(startTimeTextEdit.getText().toString())){
      intent.putExtra(START_TIME_EXTRA, selectedStartDate);
    } else intent.putExtra(START_TIME_EXTRA, new Date(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute).getTime());

    if (beginEndDate.equals(endDateTextEdit.getText().toString()) & beginEndTime.equals(endTimeTextEdit.getText().toString())){
      intent.putExtra(END_TIME_EXTRA, selectedEndDate);
    } else intent.putExtra(END_TIME_EXTRA, new Date(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute).getTime());

    intent.putExtra(LOCATION_EXTRA, selectedLocation);
    intent.putExtra(TAGS_EXTRA, selectedTags);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 9) {
      if (resultCode == RESULT_OK) {

      }
    }
  }

  @Override
  public void onButtonAdapterItemClick(ButtonAdapter adapter, String item, int position) {

  }


  private void makeGeocodeSearch(LatLng latLng) {
    try {

      MapboxGeocoding client = MapboxGeocoding.builder()
              .accessToken(getString(R.string.mapbox_access_token))
              .query(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()))
              .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
              .mode(GeocodingCriteria.MODE_PLACES)
              .build();
      client.enqueueCall(new Callback<GeocodingResponse>() {
        @Override
        public void onResponse(Call<GeocodingResponse> call,
                               Response<GeocodingResponse> response) {
          List<CarmenFeature> results = response.body().features();
          if (results.size() > 0) {
            // Get the first Feature from the successful geocoding response
            CarmenFeature feature = results.get(0);
            location.setText(feature.placeName());
            Log.i("Carmen",feature.placeName());
            //animateCameraToNewPosition(latLng);
          } else {
            Toast.makeText(ReviewNewEventActivity.this, "No Results",
                    Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
          Log.e("geocode","Geocoding Failure: " + throwable.getMessage());
        }
      });
    } catch (ServicesException servicesException) {
      Log.e("geocode","Error geocoding: " + servicesException.toString());
      servicesException.printStackTrace();
    }
  }

}
